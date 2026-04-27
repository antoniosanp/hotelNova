package menu;

import javax.swing.JOptionPane;

public class AdminMenu {

    private final GuestMenu guestMenu;
    private final RoomMenu roomMenu;
    private final ReservationMenu reservationMenu;

    public AdminMenu() {
        this.guestMenu = new GuestMenu();
        this.roomMenu = new RoomMenu();
        this.reservationMenu = new ReservationMenu();
    }

    public void open() {
        while (true) {
            String option = JOptionPane.showInputDialog(
                    null,
                    "Admin Menu\n1) Guest menu\n2) Room menu\n3) Reservation menu\n0) Cerrar sesión",
                    "Admin Menu",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (option == null || "0".equals(option.trim())) {
                return;
            }

            try {
                switch (option.trim()) {
                    case "1" -> guestMenu.open();
                    case "2" -> roomMenu.open();
                    case "3" -> reservationMenu.open();
                    default -> JOptionPane.showMessageDialog(null, "Opción inválida");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
