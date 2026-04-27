package dao;

import model.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationDAO extends GenericDAO<Reservation,Integer> {

    List<Reservation> findByRoom(int id_room);
    Optional<Reservation> findActiveByRoom(int id_room);
    Optional<Reservation> findActiveById(int id_reservation);
    boolean hasOverlappingReservation(int id_room, LocalDate day_in, LocalDate day_out);

}
