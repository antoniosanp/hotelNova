package dao.impl;

import dao.RoomDAO;
import model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDAOImpl extends GenericDAOImpl<Room, Integer> implements RoomDAO {

    // =========================
    // SQL CRUD GENERAL
    // =========================
    private static final String INSERT =
            "INSERT INTO room (id_room, room_type, room_number, room_capacity, room_price, room_state, isActive) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE room SET room_type=?, room_number=?, room_capacity=?, room_price=?, room_state=?, isActive=? " +
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
    private static final String FIND_BY_TYPE =
            "SELECT * FROM room WHERE room_type=? ORDER BY room_number";
    private static final String FIND_BY_STATE =
            "SELECT * FROM room WHERE room_state=? ORDER BY room_number";
    private static final String NEXT_ID =
            "SELECT COALESCE(MAX(id_room), 0) + 1 FROM room";

    // ==================================================
    // IMPLEMENTACIÓN MÉTODOS ABSTRACTOS GenericDAOImpl
    // ==================================================

    @Override
    protected Room mapRow(ResultSet rs) throws SQLException {
        Room room = new Room();

        room.setId(rs.getInt("id_room"));
        room.setRoom_type(rs.getString("room_type"));
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
        if (room.getId() <= 0) {
            room.setId(nextId());
        }
        ps.setInt(1, room.getId());
        ps.setString(2, room.getRoom_type());
        ps.setInt(3, room.getRoom_number());
        ps.setInt(4, room.getRoom_capacity());
        ps.setDouble(5, room.getRoom_price());
        ps.setString(6, room.getRoom_state());
        ps.setBoolean(7, room.isActive());
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, Room room) throws SQLException {
        ps.setString(1, room.getRoom_type());
        ps.setInt(2, room.getRoom_number());
        ps.setInt(3, room.getRoom_capacity());
        ps.setDouble(4, room.getRoom_price());
        ps.setString(5, room.getRoom_state());
        ps.setBoolean(6, room.isActive());
        ps.setInt(7, room.getId());
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
        if (room.getId() <= 0) {
            room.setId(keys.getInt(1));
        }
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
    public List<Room> findByType(String roomType) {
        List<Room> rooms = new ArrayList<>();
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_TYPE)) {
            ps.setString(1, roomType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRow(rs));
                }
            }
            return rooms;
        } catch (SQLException e) {
            throw new RuntimeException("Error en findByType", e);
        }
    }

    @Override
    public List<Room> findByState(String roomState) {
        List<Room> rooms = new ArrayList<>();
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_STATE)) {
            ps.setString(1, roomState);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRow(rs));
                }
            }
            return rooms;
        } catch (SQLException e) {
            throw new RuntimeException("Error en findByState", e);
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

    private int nextId() {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(NEXT_ID);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new RuntimeException("No fue posible calcular el id de room");
        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo next id de room", e);
        }
    }
}
