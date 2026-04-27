import controller.AuthController;
import controller.ExportController;
import controller.GuestController;
import controller.ReservationController;
import controller.RoomController;
import controller.UserController;
import model.User;

import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {
        AuthController authController = new AuthController();
        RoomController roomController = new RoomController();
        GuestController guestController = new GuestController();
        UserController userController = new UserController();
        ReservationController reservationController = new ReservationController();
        ExportController exportController = new ExportController();


        while (true) {
            String option = JOptionPane.showInputDialog(
                    null,
                    "HotelNova\n1) Login\n2) Registrar usuario\n3) Listar habitaciones\n4) Listar huéspedes\n5) Listar usuarios\n6) Listar reservas\n7) Exportar CSV\n0) Salir",
                    "Menú principal",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (option == null || "0".equals(option.trim())) {
                break;
            }

            try {
                switch (option.trim()) {
                    case "1" -> {
                        String email = JOptionPane.showInputDialog("Email:");
                        String password = JOptionPane.showInputDialog("Password:");
                        User user = authController.login(email, password);
                        JOptionPane.showMessageDialog(null, "Bienvenido " + user.getName() + " (" + user.getRol() + ")");
                    }
                    case "2" -> {
                        String name = JOptionPane.showInputDialog("Nombre:");
                        String email = JOptionPane.showInputDialog("Email:");
                        String password = JOptionPane.showInputDialog("Password:");
                        String rol = JOptionPane.showInputDialog("Rol (ADMIN/RECEPCIONISTA):");
                        User registered = authController.register(email, password, rol, name);
                        JOptionPane.showMessageDialog(null, "Usuario registrado con id " + registered.getId());
                    }
                    case "3" -> JOptionPane.showMessageDialog(null, roomController.listRoomsAsTable());
                    case "4" -> JOptionPane.showMessageDialog(null, guestController.listGuestsAsTable());
                    case "5" -> JOptionPane.showMessageDialog(null, userController.listUsersAsTable());
                    case "6" -> JOptionPane.showMessageDialog(null, reservationController.listReservationsAsTable());
                    case "7" -> {
                        exportController.exportRoomsCsv();
                        exportController.exportActiveReservationsCsv();
                        JOptionPane.showMessageDialog(null, "Exportación completada");
                    }
                    default -> JOptionPane.showMessageDialog(null, "Opción inválida");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
