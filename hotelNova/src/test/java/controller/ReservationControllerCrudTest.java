package controller;

import model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.interfaces.IReservationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservationControllerCrudTest {

    private ReservationController controller;
    private FakeReservationService service;

    @BeforeEach
    void setUp() {
        service = new FakeReservationService();
        controller = new ReservationController(service);
    }

    @Test
    void debeCrearReservaConCheckIn() {
        Reservation created = controller.checkIn(1, 10, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 4));

        assertTrue(created.getId() > 0);
        assertEquals(3, created.getTotal_nights());
        assertEquals(1, service.listReservations().size());
    }

    @Test
    void debeListarReservas() {
        controller.checkIn(1, 10, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 3));
        controller.checkIn(2, 11, LocalDate.of(2026, 5, 6), LocalDate.of(2026, 5, 7));

        String table = controller.listReservationsAsTable();

        assertTrue(table.contains("ROOM"));
        assertTrue(table.contains("1"));
        assertTrue(table.contains("2"));
    }

    @Test
    void debeActualizarReserva() {
        Reservation created = controller.checkIn(1, 10, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 3));
        created.setDay_out(LocalDate.of(2026, 5, 5));
        created.setTotal_nights(4);

        Reservation updated = controller.updateReservation(created);

        assertEquals(LocalDate.of(2026, 5, 5), updated.getDay_out());
        assertEquals(4, updated.getTotal_nights());
    }

    @Test
    void debeEliminarReserva() {
        Reservation created = controller.checkIn(1, 10, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 2));
        created.setCheck_in(false);
        service.updateReservation(created);

        boolean deleted = controller.deleteReservation(created.getId());

        assertTrue(deleted);
        assertEquals(0, service.listReservations().size());
    }

    @Test
    void debeProcesarCheckOut() {
        Reservation created = controller.checkIn(1, 10, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 3));

        double total = controller.checkOut(created.getId());

        assertTrue(total > 0);
        assertTrue(service.findById(created.getId()).isCheck_out());
    }

    @Test
    void noDebeEliminarReservaActiva() {
        Reservation created = controller.checkIn(1, 10, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 2));

        boolean deleted = controller.deleteReservation(created.getId());

        assertFalse(deleted);
    }

    private static class FakeReservationService implements IReservationService {
        private final List<Reservation> reservations = new ArrayList<>();
        private int seq = 1;

        @Override
        public Reservation checkIn(int roomId, int guestId, LocalDate dayIn, LocalDate dayOut) {
            Reservation reservation = new Reservation(roomId, guestId, (int) (dayOut.toEpochDay() - dayIn.toEpochDay()), dayIn, dayOut);
            reservation.setId(seq++);
            reservation.setCheck_in(true);
            reservation.setCheck_out(false);
            reservations.add(reservation);
            return reservation;
        }

        @Override
        public Reservation updateReservation(Reservation reservation) {
            for (int i = 0; i < reservations.size(); i++) {
                if (reservations.get(i).getId() == reservation.getId()) {
                    reservations.set(i, reservation);
                    return reservation;
                }
            }
            throw new RuntimeException("Reserva no encontrada");
        }

        @Override
        public boolean deleteReservation(int reservationId) {
            Iterator<Reservation> it = reservations.iterator();
            while (it.hasNext()) {
                Reservation reservation = it.next();
                if (reservation.getId() == reservationId) {
                    if (reservation.isCheck_in() && !reservation.isCheck_out()) {
                        return false;
                    }
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        @Override
        public double checkOut(int reservationId) {
            Reservation reservation = findById(reservationId);
            reservation.setCheck_out(true);
            return reservation.getTotal_nights() * 100.0;
        }

        @Override
        public List<Reservation> listReservations() {
            return new ArrayList<>(reservations);
        }

        @Override
        public List<Reservation> listReservationsByRoom(int roomId) {
            List<Reservation> filtered = new ArrayList<>();
            for (Reservation reservation : reservations) {
                if (reservation.getId_room() == roomId) {
                    filtered.add(reservation);
                }
            }
            return filtered;
        }

        private Reservation findById(int id) {
            for (Reservation reservation : reservations) {
                if (reservation.getId() == id) {
                    return reservation;
                }
            }
            throw new RuntimeException("Reserva no encontrada");
        }
    }
}
