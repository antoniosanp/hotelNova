package controller;

import model.Room;
import services.RoomService;
import services.interfaces.IRoomService;
import utils.TableFormatterUtil;

import javax.swing.JOptionPane;
import java.util.List;
import java.util.stream.Collectors;

public class RoomController {

    private final IRoomService roomService;

    public RoomController() {
        this(new RoomService());
    }

    public RoomController(IRoomService roomService) {
        this.roomService = roomService;
    }

    public Room createRoom(int roomNumber, int capacity, double price, String state) {
        return roomService.createRoom(roomNumber, capacity, price, state);
    }

    public boolean updateRoom(Room room) {
        return roomService.updateRoom(room);
    }

    public boolean deleteRoom(int roomId) {
        return roomService.deleteRoom(roomId);
    }

    public boolean toggleRoomActive(int roomId, boolean active) {
        return roomService.toggleActive(roomId, active);
    }

    public String listRoomsAsTable() {
        List<String[]> rows = roomService.listRooms().stream()
                .map(this::toRow)
                .collect(Collectors.toList());
        return TableFormatterUtil.format(
                new String[]{"ID", "NUMERO", "CAP", "PRECIO", "ESTADO", "ACTIVO"},
                rows
        );
    }

    public String listRoomsByStateAsTable(String state) {
        List<String[]> rows = roomService.listRoomsByState(state).stream()
                .map(this::toRow)
                .collect(Collectors.toList());
        return TableFormatterUtil.format(
                new String[]{"ID", "NUMERO", "CAP", "PRECIO", "ESTADO", "ACTIVO"},
                rows
        );
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private String[] toRow(Room room) {
        return new String[]{
                String.valueOf(room.getId()),
                String.valueOf(room.getRoom_number()),
                String.valueOf(room.getRoom_capacity()),
                String.valueOf(room.getRoom_price()),
                "[" + room.getRoom_state() + "]",
                room.isActive() ? "[ACTIVO]" : "[INACTIVO]"
        };
    }
}
