package dao;

import model.User;

import java.util.Optional;

public interface UserDAO extends GenericDAO{

    Optional<User> getByRol(String ROL);


}
