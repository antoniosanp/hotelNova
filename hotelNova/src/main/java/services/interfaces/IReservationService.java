package services.interfaces;

import model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface IReservationService {
    Reservation checkIn(int roomId, int guestId, LocalDate dayIn, LocalDate dayOut);
    Reservation updateReservation(Reservation reservation);
    boolean deleteReservation(int reservationId);
    double checkOut(int reservationId);
    List<Reservation> listReservations();
    List<Reservation> listReservationsByRoom(int roomId);
}
