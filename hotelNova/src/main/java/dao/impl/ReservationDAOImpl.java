package dao.impl;

import dao.ReservationDAO;
import model.Reservation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationDAOImpl extends GenericDAOImpl<Reservation, Integer> implements ReservationDAO {

    private static final String INSERT =
            "INSERT INTO reservation (id_room, id_guest, total_nights, day_in, day_out, check_in, check_out) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE reservation SET id_room=?, id_guest=?, total_nights=?, day_in=?, day_out=?, check_in=?, check_out=? " +
                    "WHERE id_reservation=?";

    private static final String DELETE =
            "DELETE FROM reservation WHERE id_reservation=?";

    private static final String FIND_BY_ID =
            "SELECT * FROM reservation WHERE id_reservation=?";

    private static final String FIND_ALL =
            "SELECT * FROM reservation";

    private static final String FIND_BY_ROOM =
            "SELECT * FROM reservation WHERE id_room=? ORDER BY day_in";

    private static final String FIND_ACTIVE_BY_ROOM =
            "SELECT * FROM reservation WHERE id_room=? AND check_in=true AND check_out=false ORDER BY id_reservation DESC LIMIT 1";

    private static final String FIND_ACTIVE_BY_ID =
            "SELECT * FROM reservation WHERE id_reservation=? AND check_in=true AND check_out=false";

    private static final String OVERLAP =
            "SELECT COUNT(*) FROM reservation " +
                    "WHERE id_room=? AND daterange(day_in, day_out, '[)') && daterange(?, ?, '[)')";

    @Override
    protected Reservation mapRow(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getInt("id_reservation"));
        reservation.setId_room(rs.getInt("id_room"));
        reservation.setId_guest(rs.getInt("id_guest"));
        reservation.setTotal_nights(rs.getInt("total_nights"));
        reservation.setDay_in(rs.getDate("day_in").toLocalDate());
        reservation.setDay_out(rs.getDate("day_out").toLocalDate());
        reservation.setCheck_in(rs.getBoolean("check_in"));
        reservation.setCheck_out(rs.getBoolean("check_out"));
        return reservation;
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
    protected void setInsertParams(PreparedStatement ps, Reservation reservation) throws SQLException {
        ps.setInt(1, reservation.getId_room());
        ps.setInt(2, reservation.getId_guest());
        ps.setInt(3, reservation.getTotal_nights());
        ps.setDate(4, Date.valueOf(reservation.getDay_in()));
        ps.setDate(5, Date.valueOf(reservation.getDay_out()));
        ps.setBoolean(6, reservation.isCheck_in());
        ps.setBoolean(7, reservation.isCheck_out());
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, Reservation reservation) throws SQLException {
        setInsertParams(ps, reservation);
        ps.setInt(8, reservation.getId());
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
    protected void setGeneratedKey(Reservation reservation, ResultSet keys) throws SQLException {
        reservation.setId(keys.getInt(1));
    }

    @Override
    public List<Reservation> findByRoom(int id_room) {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ROOM)) {
            ps.setInt(1, id_room);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapRow(rs));
                }
            }
            return reservations;
        } catch (SQLException e) {
            throw new RuntimeException("Error en findByRoom", e);
        }
    }

    @Override
    public Optional<Reservation> findActiveByRoom(int id_room) {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ACTIVE_BY_ROOM)) {
            ps.setInt(1, id_room);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en findActiveByRoom", e);
        }
    }

    @Override
    public Optional<Reservation> findActiveById(int id_reservation) {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ACTIVE_BY_ID)) {
            ps.setInt(1, id_reservation);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en findActiveById", e);
        }
    }

    @Override
    public boolean hasOverlappingReservation(int id_room, LocalDate day_in, LocalDate day_out) {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(OVERLAP)) {
            ps.setInt(1, id_room);
            ps.setDate(2, Date.valueOf(day_in));
            ps.setDate(3, Date.valueOf(day_out));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error en hasOverlappingReservation", e);
        }
    }
}
