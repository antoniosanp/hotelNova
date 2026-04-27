package controller;

import model.User;
import services.UserService;
import services.interfaces.IUserService;
import utils.TableFormatterUtil;

import javax.swing.JOptionPane;
import java.util.List;
import java.util.stream.Collectors;

public class UserController {

    private final IUserService userService;

    public UserController() {
        this(new UserService());
    }

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    public User createUser(String email, String plainPassword, String rol, String name) {
        return userService.createUser(email, plainPassword, rol, name);
    }

    public boolean updateUser(User user) {
        return userService.updateUser(user);
    }

    public boolean deleteUser(int id) {
        return userService.deleteUser(id);
    }

    public String listUsersAsTable() {
        List<String[]> rows = userService.listUsers().stream()
                .map(this::toRow)
                .collect(Collectors.toList());
        return TableFormatterUtil.format(new String[]{"ID", "NOMBRE", "EMAIL", "ROL"}, rows);
    }

    public String listUsersByRolAsTable(String rol) {
        List<String[]> rows = userService.listByRol(rol).stream()
                .map(this::toRow)
                .collect(Collectors.toList());
        return TableFormatterUtil.format(new String[]{"ID", "NOMBRE", "EMAIL", "ROL"}, rows);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private String[] toRow(User user) {
        return new String[]{
                String.valueOf(user.getId()),
                user.getName(),
                user.getEmail(),
                user.getRol()
        };
    }
}
