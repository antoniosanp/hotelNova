package services.interfaces;

import model.User;

import java.util.List;

public interface IUserService {
    User createUser(String email, String plainPassword, String rol, String name);
    boolean updateUser(User user);
    boolean deleteUser(int id);
    List<User> listUsers();
    List<User> listByRol(String rol);
}
