package services;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import exceptions.BusinessException;
import exceptions.NotFoundException;
import model.User;
import services.interfaces.IUserService;
import utils.AppLogger;
import utils.PasswordUtil;

import java.util.List;

public class UserService implements IUserService {

    private final UserDAO userDAO;

    public UserService() {
        this(new UserDAOImpl());
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User createUser(String email, String plainPassword, String rol, String name) {
        AppLogger.http("POST", "/users");
        validateCommon(email, name);
        validateRole(rol);

        if (userDAO.getByEmail(email).isPresent()) {
            throw new BusinessException("Ya existe un usuario con ese email");
        }

        User user = new User(email, PasswordUtil.hashPassword(plainPassword), rol.toUpperCase(), name);
        return userDAO.save(user);
    }

    @Override
    public boolean updateUser(User user) {
        AppLogger.http("PATCH", "/users/" + user.getId());
        validateCommon(user.getEmail(), user.getName());
        validateRole(user.getRol());

        User existing = userDAO.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("No existe el usuario"));

        userDAO.getByEmail(user.getEmail()).ifPresent(current -> {
            if (current.getId() != user.getId()) {
                throw new BusinessException("El email ya está en uso por otro usuario");
            }
        });

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(existing.getPassword());
        } else if (!user.getPassword().startsWith("$2a$") && !user.getPassword().startsWith("$2b$")) {
            user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        }

        user.setRol(user.getRol().toUpperCase());
        return userDAO.update(user);
    }

    @Override
    public boolean deleteUser(int id) {
        AppLogger.http("DELETE", "/users/" + id);
        if (userDAO.findById(id).isEmpty()) {
            throw new NotFoundException("No existe el usuario");
        }
        return userDAO.deleteById(id);
    }

    @Override
    public List<User> listUsers() {
        AppLogger.http("GET", "/users");
        return userDAO.findAll();
    }

    @Override
    public List<User> listByRol(String rol) {
        AppLogger.http("GET", "/users?rol=" + rol);
        validateRole(rol);
        return userDAO.getByRol(rol.toUpperCase());
    }

    private void validateCommon(String email, String name) {
        if (email == null || email.isBlank()) {
            throw new BusinessException("El email es obligatorio");
        }
        if (name == null || name.isBlank()) {
            throw new BusinessException("El nombre es obligatorio");
        }
    }

    private void validateRole(String rol) {
        if (rol == null) {
            throw new BusinessException("Rol obligatorio");
        }
        String normalized = rol.toUpperCase();
        if (!"ADMIN".equals(normalized) && !"RECEPCIONISTA".equals(normalized)) {
            throw new BusinessException("Rol no permitido. Usa ADMIN o RECEPCIONISTA");
        }
    }
}
