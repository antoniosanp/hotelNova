package menu;

import controller.RoomController;
import model.Room;

import javax.swing.JOptionPane;

public class RoomMenu {

    private final RoomController roomController;

    public RoomMenu() {
        this.roomController = new RoomController();
    }

    public void open() {
        while (true) {
            String option = JOptionPane.showInputDialog(
                    null,
                    "Room Menu\n1) Crear habitación\n2) Listar habitaciones\n3) Actualizar habitación\n4) Eliminar habitación\n5) Activar/Desactivar habitación\n6) Filtrar por tipo\n7) Filtrar por estado\n0) Volver",
                    "Room Menu",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (option == null || "0".equals(option.trim())) {
                return;
            }

            try {
                switch (option.trim()) {
                    case "1" -> createRoom();
                    case "2" -> JOptionPane.showMessageDialog(null, roomController.listRoomsAsTable());
                    case "3" -> updateRoom();
                    case "4" -> deleteRoom();
                    case "5" -> toggleRoom();
                    case "6" -> filterByType();
                    case "7" -> filterByState();
                    default -> JOptionPane.showMessageDialog(null, "Opción inválida");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, rootMessage(ex), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createRoom() {
        int number = askInt("Número de habitación:");
        String type = JOptionPane.showInputDialog("Tipo de habitación (SINGLE/DOUBLE/SUITE):");
        int capacity = askInt("Capacidad:");
        double price = askDouble("Precio por noche:");
        String state = JOptionPane.showInputDialog("Estado (DISPONIBLE/OCUPADA):");
        Room room = roomController.createRoom(number, type, capacity, price, state);
        JOptionPane.showMessageDialog(null, "Habitación creada con id " + room.getId());
    }

    private void updateRoom() {
        int id = askInt("Id de habitación:");
        int number = askInt("Nuevo número de habitación:");
        String type = JOptionPane.showInputDialog("Nuevo tipo de habitación:");
        int capacity = askInt("Nueva capacidad:");
        double price = askDouble("Nuevo precio:");
        String state = JOptionPane.showInputDialog("Nuevo estado (DISPONIBLE/OCUPADA):");
        String activeValue = JOptionPane.showInputDialog("¿Activa? (true/false):");
        boolean active = Boolean.parseBoolean(activeValue);

        Room room = new Room(id, type, number, capacity, price, state);
        room.setActive(active);
        boolean updated = roomController.updateRoom(room);
        JOptionPane.showMessageDialog(null, updated ? "Habitación actualizada" : "No se pudo actualizar");
    }

    private void deleteRoom() {
        int id = askInt("Id de habitación a eliminar:");
        boolean deleted = roomController.deleteRoom(id);
        JOptionPane.showMessageDialog(null, deleted ? "Habitación eliminada" : "No se pudo eliminar");
    }

    private void toggleRoom() {
        int id = askInt("Id de habitación:");
        String value = JOptionPane.showInputDialog("Nuevo estado activo (true/false):");
        boolean active = Boolean.parseBoolean(value);
        boolean updated = roomController.toggleRoomActive(id, active);
        JOptionPane.showMessageDialog(null, updated ? "Estado actualizado" : "No se pudo actualizar");
    }

    private void filterByType() {
        String type = JOptionPane.showInputDialog("Tipo a filtrar:");
        JOptionPane.showMessageDialog(null, roomController.listRoomsByTypeAsTable(type));
    }

    private void filterByState() {
        String state = JOptionPane.showInputDialog("Estado a filtrar (DISPONIBLE/OCUPADA):");
        JOptionPane.showMessageDialog(null, roomController.listRoomsByStateAsTable(state));
    }

    private int askInt(String message) {
        String value = JOptionPane.showInputDialog(message);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Dato numérico obligatorio");
        }
        return Integer.parseInt(value.trim());
    }

    private double askDouble(String message) {
        String value = JOptionPane.showInputDialog(message);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Dato decimal obligatorio");
        }
        return Double.parseDouble(value.trim());
    }

    private String rootMessage(Throwable ex) {
        Throwable current = ex;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current.getMessage() == null ? ex.getMessage() : current.getMessage();
    }
}
