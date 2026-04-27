package services.interfaces;

import model.Room;

import java.util.List;

public interface IRoomService {
    Room createRoom(int roomNumber, int capacity, double price, String state);
    boolean updateRoom(Room room);
    boolean deleteRoom(int roomId);
    boolean toggleActive(int roomId, boolean active);
    List<Room> listRooms();
    List<Room> listRoomsByState(String state);
}
