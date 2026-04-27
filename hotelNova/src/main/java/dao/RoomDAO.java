package dao;

import model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomDAO extends  GenericDAO<Room, Integer>{
    Optional<Room> findByNumber(int id_room);
    List<Room> findByType(String roomType);
    List<Room> findByState(String roomState);
    boolean updateState(int id_room, String new_state);
    boolean updateIsActive(int id_room, boolean new_active);

}
