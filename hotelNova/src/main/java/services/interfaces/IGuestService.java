package services.interfaces;

import model.Guest;

import java.util.List;

public interface IGuestService {
    Guest createGuest(String name, String email);
    boolean updateGuest(Guest guest);
    boolean deleteGuest(int id);
    boolean toggleActive(int id, boolean active);
    Guest findByEmail(String email);
    List<Guest> listGuests();
    List<Guest> listActiveGuests();
}
