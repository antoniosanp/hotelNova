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
                    "HotelNova\n1) Login\n2) Listar habitaciones\n3) Listar huéspedes\n4) Listar usuarios\n5) Listar reservas\n6) Exportar CSV\n0) Salir",
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
                    case "2" -> JOptionPane.showMessageDialog(null, roomController.listRoomsAsTable());
                    case "3" -> JOptionPane.showMessageDialog(null, guestController.listGuestsAsTable());
                    case "4" -> JOptionPane.showMessageDialog(null, userController.listUsersAsTable());
                    case "5" -> JOptionPane.showMessageDialog(null, reservationController.listReservationsAsTable());
                    case "6" -> {
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
