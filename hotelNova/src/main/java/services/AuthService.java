package services;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import exceptions.BusinessException;
import model.User;
import services.interfaces.IAuthService;
import utils.AppLogger;
import utils.PasswordUtil;

public class AuthService implements IAuthService {

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_RECEPCIONISTA = "RECEPCIONISTA";

    private final UserDAO userDAO;

    public AuthService() {
        this(new UserDAOImpl());
    }

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User register(String email, String plainPassword, String rol, String name) {
        AppLogger.http("POST", "/auth/register");
        validateBasicFields(email, name);
        validateRole(rol);

        if (userDAO.getByEmail(email).isPresent()) {
            throw new BusinessException("Ya existe un usuario con ese email");
        }

        User user = new User(email, PasswordUtil.hashPassword(plainPassword), rol.toUpperCase(), name);
        return userDAO.save(user);
    }

    @Override
    public User login(String email, String plainPassword) {
        AppLogger.http("POST", "/auth/login");
        validateBasicFields(email, "ok");

        User user = userDAO.getByEmail(email)
                .orElseThrow(() -> new BusinessException("Credenciales inválidas"));

        if (!PasswordUtil.matches(plainPassword, user.getPassword())) {
            throw new BusinessException("Credenciales inválidas");
        }
        return user;
    }

    private void validateRole(String rol) {
        if (rol == null) {
            throw new BusinessException("El rol es obligatorio");
        }
        String normalized = rol.toUpperCase();
        if (!ROLE_ADMIN.equals(normalized) && !ROLE_RECEPCIONISTA.equals(normalized)) {
            throw new BusinessException("Rol no permitido. Usa ADMIN o RECEPCIONISTA");
        }
    }

    private void validateBasicFields(String email, String name) {
        if (email == null || email.isBlank()) {
            throw new BusinessException("El email es obligatorio");
        }
        if (name == null || name.isBlank()) {
            throw new BusinessException("El nombre es obligatorio");
        }
    }
}
