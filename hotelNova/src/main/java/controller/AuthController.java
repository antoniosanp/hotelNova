package controller;

import model.User;
import services.AuthService;
import services.interfaces.IAuthService;

import javax.swing.JOptionPane;

public class AuthController {

    private final IAuthService authService;

    public AuthController() {
        this(new AuthService());
    }

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    public User register(String email, String plainPassword, String rol, String name) {
        return authService.register(email, plainPassword, rol, name);
    }

    public User login(String email, String plainPassword) {
        return authService.login(email, plainPassword);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
