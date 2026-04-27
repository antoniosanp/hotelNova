package dao;

import model.Reservation;

import java.util.List;

public interface ReservationDAO extends GenericDAO {

    List<Reservation> findByRoom(int id_room);

}
