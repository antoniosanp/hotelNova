package dao;

import model.Reservation;

import java.util.List;

public interface ReservationDAO extends GenericDAO<Reservation,Integer> {

    List<Reservation> findByRoom(int id_room);

}
