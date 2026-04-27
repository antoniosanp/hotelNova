package dao.impl;

import dao.GuestDAO;
import model.Guest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class GuestDAOImpl extends GenericDAOImpl<Guest,Integer> implements GuestDAO {
    // =========================
    // SQL CRUD GENERAL
    // =========================
    private static final String INSERT =
            "INSERT INTO guest (id_guest, name, email) " +
                    "VALUES (?, ?, ?)";

    private static final String UPDATE =
            "UPDATE guest SET name=?, email=? " +
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
    private static final String NEXT_ID =
            "SELECT COALESCE(MAX(id_guest), 0) + 1 FROM guest";


    // ==================================================
    // IMPLEMENTACIÓN MÉTODOS ABSTRACTOS GenericDAOImpl
    // ==================================================

    @Override
    protected Guest mapRow(ResultSet rs) throws SQLException {
        Guest guest = new Guest();

        guest.setId(rs.getInt("id_guest"));
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
        ps.setString(2, guest.getName());
        ps.setString(3, guest.getEmail());

    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, Guest guest) throws SQLException {
        ps.setString(1, guest.getName());
        ps.setString(2, guest.getEmail());
        ps.setInt(3, guest.getId());
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
