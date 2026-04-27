package dao;

import model.Room;

import java.util.Optional;

public interface RoomDAO extends  GenericDAO{
    Optional<Room> findByNumber(int id_room);
    boolean updateState(int id_room, String new_state);

}
