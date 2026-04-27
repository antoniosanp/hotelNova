package controller;

import model.Reservation;
import services.ReservationService;
import services.interfaces.IReservationService;
import utils.TableFormatterUtil;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationController {

    private final IReservationService reservationService;

    public ReservationController() {
        this(new ReservationService());
    }

    public ReservationController(IReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public Reservation checkIn(int roomId, int guestId, LocalDate dayIn, LocalDate dayOut) {
        return reservationService.checkIn(roomId, guestId, dayIn, dayOut);
    }

    public double checkOut(int reservationId) {
        return reservationService.checkOut(reservationId);
    }

    public String listReservationsAsTable() {
        List<String[]> rows = reservationService.listReservations().stream()
                .map(this::toRow)
                .collect(Collectors.toList());
        return TableFormatterUtil.format(headers(), rows);
    }

    public String listReservationsByRoomAsTable(int roomId) {
        List<String[]> rows = reservationService.listReservationsByRoom(roomId).stream()
                .map(this::toRow)
                .collect(Collectors.toList());
        return TableFormatterUtil.format(headers(), rows);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private String[] headers() {
        return new String[]{"ID", "ROOM", "GUEST", "NOCHES", "DAY_IN", "DAY_OUT", "CHECK_IN", "CHECK_OUT"};
    }

    private String[] toRow(Reservation reservation) {
        return new String[]{
                String.valueOf(reservation.getId()),
                String.valueOf(reservation.getId_room()),
                String.valueOf(reservation.getId_guest()),
                String.valueOf(reservation.getTotal_nights()),
                String.valueOf(reservation.getDay_in()),
                String.valueOf(reservation.getDay_out()),
                String.valueOf(reservation.isCheck_in()),
                String.valueOf(reservation.isCheck_out())
        };
    }
}
