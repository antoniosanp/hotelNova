package dao;

import model.Guest;

import java.util.Optional;

public interface GuestDAO extends GenericDAO<Guest,Integer>{
    Optional<Guest> findByEmail( String email);
}
