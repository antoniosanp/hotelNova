package dao.impl;

import dao.GuestDAO;
import model.Guest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuestDAOImpl extends GenericDAOImpl<Guest,Integer> implements GuestDAO {
    // =========================
    // SQL CRUD GENERAL
    // =========================
    private static final String INSERT =
            "INSERT INTO guest (id_guest, isActive, name, email) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE guest SET isActive=?, name=?, email=? " +
                    "WHERE id_guest=?";

    private static final String DELETE =
            "DELETE FROM guest WHERE id_guest=?";

    private static final String FIND_BY_ID =
            "SELECT * FROM guest WHERE id_guest=?";

    private static final String FIND_ALL =
            "SELECT * FROM guest";

    // =========================
    // SQL MÉTODOS EXTRA
    // =========================
    private static final String FIND_BY_EMAIL =
            "SELECT * FROM guest WHERE email=?";
    private static final String FIND_BY_IS_ACTIVE =
            "SELECT * FROM guest WHERE isActive=? ORDER BY id_guest";
    private static final String UPDATE_IS_ACTIVE =
            "UPDATE guest SET isActive=? WHERE id_guest=?";
    private static final String NEXT_ID =
            "SELECT COALESCE(MAX(id_guest), 0) + 1 FROM guest";


    // ==================================================
    // IMPLEMENTACIÓN MÉTODOS ABSTRACTOS GenericDAOImpl
    // ==================================================

    @Override
    protected Guest mapRow(ResultSet rs) throws SQLException {
        Guest guest = new Guest();

        guest.setId(rs.getInt("id_guest"));
        guest.setActive(rs.getBoolean("isActive"));
        guest.setName(rs.getString("name"));
        guest.setEmail(rs.getString("email"));

        return guest;
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
    protected void setInsertParams(PreparedStatement ps, Guest guest) throws SQLException {
        if (guest.getId() <= 0) {
            guest.setId(nextId());
        }
        ps.setInt(1, guest.getId());
        ps.setBoolean(2, guest.isActive());
        ps.setString(3, guest.getName());
        ps.setString(4, guest.getEmail());

    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, Guest guest) throws SQLException {
        ps.setBoolean(1, guest.isActive());
        ps.setString(2, guest.getName());
        ps.setString(3, guest.getEmail());
        ps.setInt(4, guest.getId());
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
    protected void setGeneratedKey(Guest guest, ResultSet keys) throws SQLException {
        if (guest.getId() <= 0) {
            guest.setId(keys.getInt(1));
        }
    }

    // ===================================
    // MÉTODOS EXTRA DE GuestDAO
    // ===================================

    @Override
    public Optional<Guest> findByEmail(String email) {
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

    @Override
    public List<Guest> findByIsActive(boolean active) {
        List<Guest> guests = new ArrayList<>();
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_IS_ACTIVE)) {
            ps.setBoolean(1, active);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    guests.add(mapRow(rs));
                }
            }
            return guests;
        } catch (SQLException e) {
            throw new RuntimeException("Error en findByIsActive", e);
        }
    }

    @Override
    public boolean updateIsActive(int id_guest, boolean active) {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_IS_ACTIVE)) {
            ps.setBoolean(1, active);
            ps.setInt(2, id_guest);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error en updateIsActive", e);
        }
    }

    private int nextId() {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(NEXT_ID);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new RuntimeException("No fue posible calcular el id de guest");
        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo next id de guest", e);
        }
    }


    //----------------------------------------------------------------------
}
