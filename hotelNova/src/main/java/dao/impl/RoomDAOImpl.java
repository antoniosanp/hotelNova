package dao.impl;

import dao.RoomDAO;
import model.Room;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

public class RoomDAOImpl extends GenericDAOImpl<Room, Integer> implements RoomDAO {

    // =========================
    // SQL CRUD GENERAL
    // =========================
    private static final String INSERT =
            "INSERT INTO room (room_number, room_capacity, room_price, room_state, isActive) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE room SET room_number=?, room_capacity=?, room_price=?, room_state=?, isActive=? " +
                    "WHERE id_room=?";

    private static final String DELETE =
            "DELETE FROM room WHERE id_room=?";

    private static final String FIND_BY_ID =
            "SELECT * FROM room WHERE id_room=?";

    private static final String FIND_ALL =
            "SELECT * FROM room";

    // =========================
    // SQL MÉTODOS EXTRA
    // =========================
    private static final String FIND_BY_NUMBER =
            "SELECT * FROM room WHERE room_number=?";

    private static final String UPDATE_STATE =
            "UPDATE room SET room_state=? WHERE id_room=?";

    private static final String UPDATE_ACTIVE =
            "UPDATE room SET isActive=? WHERE id_room=?";

    // ==================================================
    // IMPLEMENTACIÓN MÉTODOS ABSTRACTOS GenericDAOImpl
    // ==================================================

    @Override
    protected Room mapRow(ResultSet rs) throws SQLException {
        Room room = new Room();

        room.setId(rs.getInt("id_room"));
        room.setRoom_number(rs.getInt("room_number"));
        room.setRoom_capacity(rs.getInt("room_capacity"));
        room.setRoom_price(rs.getDouble("room_price"));
        room.setRoom_state(rs.getString("room_state"));
        room.setActive(rs.getBoolean("isActive"));
        room.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());

        return room;
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
    protected void setInsertParams(PreparedStatement ps, Room room) throws SQLException {
        ps.setInt(1, room.getRoom_number());
        ps.setInt(2, room.getRoom_capacity());
        ps.setDouble(3, room.getRoom_price());
        ps.setString(4, room.getRoom_state());
        ps.setBoolean(5, room.isActive());
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, Room room) throws SQLException {
        ps.setInt(1, room.getRoom_number());
        ps.setInt(2, room.getRoom_capacity());
        ps.setDouble(3, room.getRoom_price());
        ps.setString(4, room.getRoom_state());
        ps.setBoolean(5, room.isActive());
        ps.setInt(6, room.getId());
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
    protected void setGeneratedKey(Room room, ResultSet keys) throws SQLException {
        room.setId(keys.getInt(1));
    }

    // ===================================
    // MÉTODOS EXTRA DE RoomDAO
    // ===================================

    @Override
    public Optional<Room> findByNumber(int roomNumber) {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_NUMBER)) {

            ps.setInt(1, roomNumber);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en findByNumber", e);
        }
    }

    @Override
    public boolean updateState(int id_room, String new_state) {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STATE)) {

            ps.setString(1, new_state);
            ps.setInt(2, id_room);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error en updateState", e);
        }
    }

    @Override
    public boolean updateIsActive(int id_room, boolean new_active) {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_ACTIVE)) {

            ps.setBoolean(1, new_active);
            ps.setInt(2, id_room);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error en updateIsActive", e);
        }
    }
}