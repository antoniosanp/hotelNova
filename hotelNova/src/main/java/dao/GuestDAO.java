package dao;

import model.Guest;

import java.util.List;
import java.util.Optional;

public interface GuestDAO extends GenericDAO<Guest,Integer>{
    Optional<Guest> findByEmail( String email);
    List<Guest> findByIsActive(boolean active);
    boolean updateIsActive(int id_guest, boolean active);
}
