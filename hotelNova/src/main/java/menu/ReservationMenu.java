package menu;

import controller.ReservationController;
import model.Reservation;

import javax.swing.JOptionPane;
import java.time.LocalDate;

public class ReservationMenu {

    private final ReservationController reservationController;

    public ReservationMenu() {
        this.reservationController = new ReservationController();
    }

    public void open() {
        while (true) {
            String option = JOptionPane.showInputDialog(
                    null,
                    "Reservation Menu\n1) Crear reserva (check-in)\n2) Listar reservas\n3) Actualizar reserva\n4) Eliminar reserva\n5) Check-out\n0) Volver",
                    "Reservation Menu",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (option == null || "0".equals(option.trim())) {
                return;
            }

            try {
                switch (option.trim()) {
                    case "1" -> createReservation();
                    case "2" -> JOptionPane.showMessageDialog(null, reservationController.listReservationsAsTable());
                    case "3" -> updateReservation();
                    case "4" -> deleteReservation();
                    case "5" -> checkOut();
                    default -> JOptionPane.showMessageDialog(null, "Opción inválida");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createReservation() {
        int roomId = askInt("Id de habitación:");
        int guestId = askInt("Id de huésped:");
        LocalDate dayIn = askDate("Fecha check-in (YYYY-MM-DD):");
        LocalDate dayOut = askDate("Fecha check-out (YYYY-MM-DD):");
        Reservation reservation = reservationController.checkIn(roomId, guestId, dayIn, dayOut);
        JOptionPane.showMessageDialog(null, "Reserva creada con id " + reservation.getId());
    }

    private void updateReservation() {
        int id = askInt("Id de reserva:");
        int roomId = askInt("Id de habitación:");
        int guestId = askInt("Id de huésped:");
        LocalDate dayIn = askDate("Fecha check-in (YYYY-MM-DD):");
        LocalDate dayOut = askDate("Fecha check-out (YYYY-MM-DD):");
        int totalNights = askInt("Total de noches:");
        String checkInValue = JOptionPane.showInputDialog("Check-in realizado (true/false):");
        String checkOutValue = JOptionPane.showInputDialog("Check-out realizado (true/false):");

        Reservation reservation = new Reservation(id, roomId, guestId, totalNights, dayIn, dayOut);
        reservation.setCheck_in(Boolean.parseBoolean(checkInValue));
        reservation.setCheck_out(Boolean.parseBoolean(checkOutValue));

        Reservation updated = reservationController.updateReservation(reservation);
        JOptionPane.showMessageDialog(null, "Reserva actualizada id " + updated.getId());
    }

    private void deleteReservation() {
        int id = askInt("Id de reserva a eliminar:");
        boolean deleted = reservationController.deleteReservation(id);
        JOptionPane.showMessageDialog(null, deleted ? "Reserva eliminada" : "No se pudo eliminar");
    }

    private void checkOut() {
        int id = askInt("Id de reserva para check-out:");
        double total = reservationController.checkOut(id);
        JOptionPane.showMessageDialog(null, "Check-out completado. Total a pagar: " + total);
    }

    private int askInt(String message) {
        String value = JOptionPane.showInputDialog(message);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Dato numérico obligatorio");
        }
        return Integer.parseInt(value.trim());
    }

    private LocalDate askDate(String message) {
        String value = JOptionPane.showInputDialog(message);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Fecha obligatoria");
        }
        return LocalDate.parse(value.trim());
    }
}
