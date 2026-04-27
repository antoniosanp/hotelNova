import controller.AuthController;
import menu.AdminMenu;
import model.User;

import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {
        AuthController authController = new AuthController();
        AdminMenu adminMenu = new AdminMenu();


        while (true) {
            String option = JOptionPane.showInputDialog(
                    null,
                    "HotelNova\n1) Login\n2) Registrar usuario\n0) Salir",
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
                        if ("ADMIN".equalsIgnoreCase(user.getRol())) {
                            JOptionPane.showMessageDialog(null, "Bienvenido " + user.getName() + " (ADMIN)");
                            adminMenu.open();
                        } else {
                            JOptionPane.showMessageDialog(null, "Bienvenido " + user.getName() + " (" + user.getRol() + ")");
                        }
                    }
                    case "2" -> {
                        String name = JOptionPane.showInputDialog("Nombre:");
                        String email = JOptionPane.showInputDialog("Email:");
                        String password = JOptionPane.showInputDialog("Password:");
                        String rol = JOptionPane.showInputDialog("Rol (ADMIN/RECEPCIONISTA):");
                        User registered = authController.register(email, password, rol, name);
                        JOptionPane.showMessageDialog(null, "Usuario registrado con id " + registered.getId());
                    }
                    default -> JOptionPane.showMessageDialog(null, "Opción inválida");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
