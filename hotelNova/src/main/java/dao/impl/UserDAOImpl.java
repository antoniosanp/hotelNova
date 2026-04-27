package dao.impl;

import dao.UserDAO;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl extends  GenericDAOImpl<User, Integer> implements UserDAO {


    // =========================
    // SQL CRUD GENERAL
    // =========================
    private static final String INSERT =
            "INSERT INTO users (id_user, email, password, rol, name) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE users SET email=?, password=?, rol=?, name=? " +
                    "WHERE id_user=?";

    private static final String DELETE =
            "DELETE FROM users WHERE id_user=?";

    private static final String FIND_BY_ID =
            "SELECT * FROM users WHERE id_user=?";

    private static final String FIND_ALL =
            "SELECT * FROM users";

    // =========================
    // SQL MÉTODOS EXTRA
    // =========================
    private static final String FIND_BY_EMAIL =
            "SELECT * FROM users WHERE email=?";


    private static final String FIND_BY_ROL =
            "SELECT * FROM users WHERE rol=?";
    private static final String NEXT_ID =
            "SELECT COALESCE(MAX(id_user), 0) + 1 FROM users";


    // ==================================================
    // IMPLEMENTACIÓN MÉTODOS ABSTRACTOS GenericDAOImpl
    // ==================================================

    @Override
    protected User mapRow(ResultSet rs) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("id_user"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRol(rs.getString("rol"));
        user.setName(rs.getString("name"));

        return user;
    }

    @Override
    protected String getInsertSQL() {
        return INSERT;
    }

    @Override
    protected String getUpdateSQL() {
        return UPDATE;
    }

    @Override
    protected String getDeleteSQL() {
        return DELETE;
    }

    @Override
    protected String getFindByIdSQL() {
        return FIND_BY_ID;
    }

    @Override
    protected String getFindAllSQL() {
        return FIND_ALL;
    }

    @Override
    protected void setInsertParams(PreparedStatement ps, User user) throws SQLException {
        if (user.getId() <= 0) {
            user.setId(nextId());
        }
        ps.setInt(1, user.getId());
        ps.setString(2, user.getEmail());
        ps.setString(3,user.getPassword());
        ps.setString(4,user.getRol());
        ps.setString(5,user.getName());

    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, User user) throws SQLException {
        ps.setString(1, user.getEmail());
        ps.setString(2,user.getPassword());
        ps.setString(3,user.getRol());
        ps.setString(4,user.getName());
        ps.setInt(5, user.getId());

    }

    @Override
    protected void setDeleteParam(PreparedStatement ps, Integer id) throws SQLException {
        ps.setInt(1, id);
    }

    @Override
    protected void setFindByIdParam(PreparedStatement ps, Integer id) throws SQLException {
        ps.setInt(1, id);
    }

    @Override
    protected void setGeneratedKey(User user, ResultSet keys) throws SQLException {
        if (user.getId() <= 0) {
            user.setId(keys.getInt(1));
        }
    }

    // ===================================
    // MÉTODOS EXTRA DE RoomDAO
    // ===================================

    @Override
    public List<User> getByRol(String rol) {
        List<User> lista = new ArrayList<>();

        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ROL)) {

            ps.setString(1, rol);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en getByRol", e);
        }

        return lista;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_EMAIL)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en findByEmail", e);
        }
    }

    private int nextId() {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(NEXT_ID);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new RuntimeException("No fue posible calcular el id de user");
        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo next id de user", e);
        }
    }




}
