package services.interfaces;

import model.User;

public interface IAuthService {
    User register(String email, String plainPassword, String rol, String name);
    User login(String email, String plainPassword);
}
