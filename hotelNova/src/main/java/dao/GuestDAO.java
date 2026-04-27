package dao;

import model.Guest;

import java.util.Optional;

public interface GuestDAO extends GenericDAO{
    Optional<Guest> findByEmail( String email);
}
