package services.interfaces;

import model.Guest;

import java.util.List;

public interface IGuestService {
    Guest createGuest(String name, String email);
    boolean updateGuest(Guest guest);
    boolean deleteGuest(int id);
    List<Guest> listGuests();
}
