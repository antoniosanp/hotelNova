package services;

import dao.ReservationDAO;
import dao.RoomDAO;
import dao.impl.ReservationDAOImpl;
import dao.impl.RoomDAOImpl;
import model.Reservation;
import model.Room;
import services.interfaces.IExportService;
import utils.AppLogger;
import utils.CsvUtil;

import java.util.List;
import java.util.stream.Collectors;

public class ExportService implements IExportService {

    private final RoomDAO roomDAO;
    private final ReservationDAO reservationDAO;

    public ExportService() {
        this(new RoomDAOImpl(), new ReservationDAOImpl());
    }

    public ExportService(RoomDAO roomDAO, ReservationDAO reservationDAO) {
        this.roomDAO = roomDAO;
        this.reservationDAO = reservationDAO;
    }

    @Override
    public void exportRoomsCsv(String fileName) {
        AppLogger.http("GET", "/exports/rooms");
        List<String[]> rows = roomDAO.findAll().stream()
                .map(this::roomToRow)
                .collect(Collectors.toList());
        CsvUtil.writeCsv(fileName, new String[]{"id", "tipo", "numero", "capacidad", "precioPorNoche", "estado", "activo", "createdAt"}, rows);
    }

    @Override
    public void exportActiveReservationsCsv(String fileName) {
        AppLogger.http("GET", "/exports/reservations-active");
        List<String[]> rows = reservationDAO.findAll().stream()
                .filter(r -> r.isCheck_in() && !r.isCheck_out())
                .map(this::reservationToRow)
                .collect(Collectors.toList());
        CsvUtil.writeCsv(fileName, new String[]{"id", "idRoom", "idGuest", "totalNights", "dayIn", "dayOut", "checkIn", "checkOut"}, rows);
    }

    private String[] roomToRow(Room room) {
        return new String[]{
                String.valueOf(room.getId()),
                room.getRoom_type(),
                String.valueOf(room.getRoom_number()),
                String.valueOf(room.getRoom_capacity()),
                String.valueOf(room.getRoom_price()),
                room.getRoom_state(),
                room.isActive() ? "ACTIVO" : "INACTIVO",
                room.getCreatedAt() == null ? "" : room.getCreatedAt().toString()
        };
    }

    private String[] reservationToRow(Reservation reservation) {
        return new String[]{
                String.valueOf(reservation.getId()),
                String.valueOf(reservation.getId_room()),
                String.valueOf(reservation.getId_guest()),
                String.valueOf(reservation.getTotal_nights()),
                String.valueOf(reservation.getDay_in()),
                String.valueOf(reservation.getDay_out()),
                String.valueOf(reservation.isCheck_in()),
                String.valueOf(reservation.isCheck_out())
        };
    }
}
