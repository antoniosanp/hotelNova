package services;

import config.AppConfig;
import dao.GuestDAO;
import dao.ReservationDAO;
import dao.RoomDAO;
import dao.impl.GuestDAOImpl;
import dao.impl.ReservationDAOImpl;
import dao.impl.RoomDAOImpl;
import db.ConnectionManager;
import exceptions.BusinessException;
import exceptions.NotFoundException;
import model.Reservation;
import model.Room;
import services.interfaces.IReservationService;
import utils.AppLogger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ReservationService implements IReservationService {

    private static final String STATE_DISPONIBLE = "DISPONIBLE";
    private static final String STATE_OCUPADA = "OCUPADA";
    private static final String INSERT_RESERVATION_SQL =
            "INSERT INTO reservation (id_room, id_guest, total_nights, day_in, day_out, check_in, check_out) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_reservation";
    private static final String UPDATE_ROOM_STATE_SQL =
            "UPDATE room SET room_state=? WHERE id_room=?";
    private static final String CHECK_OUT_SQL =
            "UPDATE reservation SET check_out=true WHERE id_reservation=? AND check_in=true AND check_out=false";

    private final ReservationDAO reservationDAO;
    private final RoomDAO roomDAO;
    private final GuestDAO guestDAO;
    private final ConnectionManager connectionManager;
    private final AppConfig appConfig;

    public ReservationService() {
        this(new ReservationDAOImpl(), new RoomDAOImpl(), new GuestDAOImpl(), ConnectionManager.getInstance(), AppConfig.getInstance());
    }

    public ReservationService(ReservationDAO reservationDAO,
                              RoomDAO roomDAO,
                              GuestDAO guestDAO,
                              ConnectionManager connectionManager,
                              AppConfig appConfig) {
        this.reservationDAO = reservationDAO;
        this.roomDAO = roomDAO;
        this.guestDAO = guestDAO;
        this.connectionManager = connectionManager;
        this.appConfig = appConfig;
    }

    @Override
    public Reservation checkIn(int roomId, int guestId, LocalDate dayIn, LocalDate dayOut) {
        AppLogger.http("POST", "/reservations/check-in");
        validateDates(dayIn, dayOut);

        Room room = roomDAO.findById(roomId).orElseThrow(() -> new NotFoundException("Habitación no encontrada"));
        guestDAO.findById(guestId).orElseThrow(() -> new NotFoundException("Huésped no encontrado"));

        if (!room.isActive()) {
            throw new BusinessException("La habitación está inactiva");
        }
        if (!STATE_DISPONIBLE.equalsIgnoreCase(room.getRoom_state())) {
            throw new BusinessException("La habitación no está disponible");
        }
        if (reservationDAO.hasOverlappingReservation(roomId, dayIn, dayOut)) {
            throw new BusinessException("No se permite solapamiento de reservas para la misma habitación");
        }

        int totalNights = Math.toIntExact(ChronoUnit.DAYS.between(dayIn, dayOut));
        Reservation reservation = new Reservation(roomId, guestId, totalNights, dayIn, dayOut);
        reservation.setCheck_in(true);
        reservation.setCheck_out(false);

        try (Connection conn = connectionManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement insertReservation = conn.prepareStatement(INSERT_RESERVATION_SQL)) {
                    insertReservation.setInt(1, reservation.getId_room());
                    insertReservation.setInt(2, reservation.getId_guest());
                    insertReservation.setInt(3, reservation.getTotal_nights());
                    insertReservation.setDate(4, Date.valueOf(reservation.getDay_in()));
                    insertReservation.setDate(5, Date.valueOf(reservation.getDay_out()));
                    insertReservation.setBoolean(6, true);
                    insertReservation.setBoolean(7, false);

                    try (ResultSet rs = insertReservation.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("No fue posible crear la reserva");
                        }
                        reservation.setId(rs.getInt("id_reservation"));
                    }
                }

                try (PreparedStatement updateRoom = conn.prepareStatement(UPDATE_ROOM_STATE_SQL)) {
                    updateRoom.setString(1, STATE_OCUPADA);
                    updateRoom.setInt(2, roomId);
                    if (updateRoom.executeUpdate() == 0) {
                        throw new SQLException("No fue posible actualizar el estado de la habitación");
                    }
                }
                conn.commit();
                return reservation;
            } catch (SQLException ex) {
                conn.rollback();
                throw new RuntimeException("Error en transacción de check-in", ex);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("No fue posible procesar check-in", e);
        }
    }

    @Override
    public Reservation updateReservation(Reservation reservation) {
        AppLogger.http("PATCH", "/reservations/" + reservation.getId());
        validateDates(reservation.getDay_in(), reservation.getDay_out());

        Reservation existing = reservationDAO.findById(reservation.getId())
                .orElseThrow(() -> new NotFoundException("Reserva no encontrada"));
        roomDAO.findById(reservation.getId_room()).orElseThrow(() -> new NotFoundException("Habitación no encontrada"));
        guestDAO.findById(reservation.getId_guest()).orElseThrow(() -> new NotFoundException("Huésped no encontrado"));

        boolean overlap = reservationDAO.findByRoom(reservation.getId_room()).stream()
                .filter(r -> r.getId() != reservation.getId())
                .anyMatch(r -> datesOverlap(r.getDay_in(), r.getDay_out(), reservation.getDay_in(), reservation.getDay_out()));
        if (overlap) {
            throw new BusinessException("No se permite solapamiento de reservas para la misma habitación");
        }

        if (reservation.getTotal_nights() <= 0) {
            reservation.setTotal_nights(Math.toIntExact(ChronoUnit.DAYS.between(reservation.getDay_in(), reservation.getDay_out())));
        }
        if (existing.isCheck_out()) {
            throw new BusinessException("No se puede editar una reserva finalizada");
        }
        return reservationDAO.update(reservation) ? reservation : existing;
    }

    @Override
    public boolean deleteReservation(int reservationId) {
        AppLogger.http("DELETE", "/reservations/" + reservationId);
        Reservation existing = reservationDAO.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reserva no encontrada"));
        if (existing.isCheck_in() && !existing.isCheck_out()) {
            throw new BusinessException("No se puede eliminar una reserva activa");
        }
        return reservationDAO.deleteById(reservationId);
    }

    @Override
    public double checkOut(int reservationId) {
        AppLogger.http("PATCH", "/reservations/check-out/" + reservationId);
        Reservation active = reservationDAO.findActiveById(reservationId)
                .orElseThrow(() -> new BusinessException("No existe una reserva activa para realizar check-out"));

        Room room = roomDAO.findById(active.getId_room())
                .orElseThrow(() -> new NotFoundException("Habitación no encontrada para la reserva"));

        double total = calculateTotal(active.getTotal_nights(), room.getRoom_price(), appConfig.getIva());

        try (Connection conn = connectionManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement checkOutUpdate = conn.prepareStatement(CHECK_OUT_SQL)) {
                    checkOutUpdate.setInt(1, reservationId);
                    if (checkOutUpdate.executeUpdate() == 0) {
                        throw new SQLException("No fue posible registrar el check-out");
                    }
                }

                try (PreparedStatement updateRoom = conn.prepareStatement(UPDATE_ROOM_STATE_SQL)) {
                    updateRoom.setString(1, STATE_DISPONIBLE);
                    updateRoom.setInt(2, active.getId_room());
                    if (updateRoom.executeUpdate() == 0) {
                        throw new SQLException("No fue posible liberar la habitación");
                    }
                }

                conn.commit();
                return total;
            } catch (SQLException ex) {
                conn.rollback();
                throw new RuntimeException("Error en transacción de check-out", ex);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("No fue posible procesar check-out", e);
        }
    }

    @Override
    public List<Reservation> listReservations() {
        AppLogger.http("GET", "/reservations");
        return reservationDAO.findAll();
    }

    @Override
    public List<Reservation> listReservationsByRoom(int roomId) {
        AppLogger.http("GET", "/reservations/room/" + roomId);
        return reservationDAO.findByRoom(roomId);
    }

    public double calculateTotal(int nights, double roomPrice, double iva) {
        if (nights <= 0) {
            throw new BusinessException("Las noches deben ser mayores a 0");
        }
        double base = nights * roomPrice;
        return base + (base * iva);
    }

    private void validateDates(LocalDate dayIn, LocalDate dayOut) {
        if (dayIn == null || dayOut == null) {
            throw new BusinessException("Las fechas son obligatorias");
        }
        if (!dayIn.isBefore(dayOut)) {
            throw new BusinessException("Fecha inválida: check-in debe ser menor que check-out");
        }
    }

    private boolean datesOverlap(LocalDate startA, LocalDate endA, LocalDate startB, LocalDate endB) {
        return startA.isBefore(endB) && startB.isBefore(endA);
    }
}
