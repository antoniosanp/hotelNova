package controller;

import model.Guest;
import services.GuestService;
import services.interfaces.IGuestService;
import utils.TableFormatterUtil;

import javax.swing.JOptionPane;
import java.util.List;
import java.util.stream.Collectors;

public class GuestController {

    private final IGuestService guestService;

    public GuestController() {
        this(new GuestService());
    }

    public GuestController(IGuestService guestService) {
        this.guestService = guestService;
    }

    public Guest createGuest(String name, String email) {
        return guestService.createGuest(name, email);
    }

    public boolean updateGuest(Guest guest) {
        return guestService.updateGuest(guest);
    }

    public boolean deleteGuest(int id) {
        return guestService.deleteGuest(id);
    }

    public String listGuestsAsTable() {
        List<String[]> rows = guestService.listGuests().stream()
                .map(guest -> new String[]{
                        String.valueOf(guest.getId()),
                        guest.getName(),
                        guest.getEmail()
                })
                .collect(Collectors.toList());
        return TableFormatterUtil.format(new String[]{"ID", "NOMBRE", "EMAIL"}, rows);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
