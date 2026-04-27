package menu;

import controller.GuestController;
import model.Guest;

import javax.swing.JOptionPane;

public class GuestMenu {

    private final GuestController guestController;

    public GuestMenu() {
        this.guestController = new GuestController();
    }

    public void open() {
        while (true) {
            String option = JOptionPane.showInputDialog(
                    null,
                    "Guest Menu\n1) Crear huésped\n2) Listar huéspedes\n3) Actualizar huésped\n4) Eliminar huésped\n5) Activar/Desactivar huésped\n6) Buscar por email\n7) Listar huéspedes activos\n0) Volver",
                    "Guest Menu",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (option == null || "0".equals(option.trim())) {
                return;
            }

            try {
                switch (option.trim()) {
                    case "1" -> createGuest();
                    case "2" -> JOptionPane.showMessageDialog(null, guestController.listGuestsAsTable());
                    case "3" -> updateGuest();
                    case "4" -> deleteGuest();
                    case "5" -> toggleGuest();
                    case "6" -> findByEmail();
                    case "7" -> JOptionPane.showMessageDialog(null, guestController.listActiveGuestsAsTable());
                    default -> JOptionPane.showMessageDialog(null, "Opción inválida");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, rootMessage(ex), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createGuest() {
        String name = JOptionPane.showInputDialog("Nombre del huésped:");
        String email = JOptionPane.showInputDialog("Email del huésped:");
        Guest guest = guestController.createGuest(name, email);
        JOptionPane.showMessageDialog(null, "Huésped creado con id " + guest.getId());
    }

    private void updateGuest() {
        int id = askInt("Id del huésped a actualizar:");
        String active = JOptionPane.showInputDialog("¿Activo? (true/false):");
        String name = JOptionPane.showInputDialog("Nuevo nombre:");
        String email = JOptionPane.showInputDialog("Nuevo email:");
        boolean updated = guestController.updateGuest(new Guest(id, Boolean.parseBoolean(active), name, email));
        JOptionPane.showMessageDialog(null, updated ? "Huésped actualizado" : "No se pudo actualizar");
    }

    private void deleteGuest() {
        int id = askInt("Id del huésped a eliminar:");
        boolean deleted = guestController.deleteGuest(id);
        JOptionPane.showMessageDialog(null, deleted ? "Huésped eliminado" : "No se pudo eliminar");
    }

    private void toggleGuest() {
        int id = askInt("Id del huésped:");
        String activeValue = JOptionPane.showInputDialog("Nuevo estado activo (true/false):");
        boolean active = Boolean.parseBoolean(activeValue);
        boolean updated = guestController.toggleGuestActive(id, active);
        JOptionPane.showMessageDialog(null, updated ? "Estado actualizado" : "No se pudo actualizar");
    }

    private void findByEmail() {
        String email = JOptionPane.showInputDialog("Email del huésped:");
        JOptionPane.showMessageDialog(null, guestController.findGuestByEmailAsTable(email));
    }

    private int askInt(String message) {
        String value = JOptionPane.showInputDialog(message);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Dato numérico obligatorio");
        }
        return Integer.parseInt(value.trim());
    }

    private String rootMessage(Throwable ex) {
        Throwable current = ex;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current.getMessage() == null ? ex.getMessage() : current.getMessage();
    }
}
