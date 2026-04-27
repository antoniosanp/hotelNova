package dao;

import model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends GenericDAO<User,Integer>{

    List<User> getByRol(String ROL);
    Optional<User> getByEmail(String email);
}
