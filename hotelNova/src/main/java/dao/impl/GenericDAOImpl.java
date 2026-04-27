package dao.impl;

import dao.GenericDAO;
import db.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {

    protected final ConnectionManager cm = ConnectionManager.getInstance();

    protected abstract T mapRow(ResultSet rs) throws SQLException;
    protected abstract String getInsertSQL();
    protected abstract String getUpdateSQL();
    protected abstract String getDeleteSQL();
    protected abstract String getFindByIdSQL();
    protected abstract String getFindAllSQL();
    protected abstract void setInsertParams(PreparedStatement ps, T entity) throws SQLException;
    protected abstract void setUpdateParams(PreparedStatement ps, T entity) throws SQLException;
    protected abstract void setDeleteParam(PreparedStatement ps, ID id) throws SQLException;
    protected abstract void setFindByIdParam(PreparedStatement ps, ID id) throws SQLException;
    protected abstract void setGeneratedKey(T entity, ResultSet keys) throws SQLException;

    @Override
    public T save(T entity) {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(getInsertSQL(), Statement.RETURN_GENERATED_KEYS)) {
            setInsertParams(ps, entity);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) setGeneratedKey(entity, keys);
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error en save", e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(getFindByIdSQL())) {
            setFindByIdParam(ps, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en findById", e);
        }
    }

    @Override
    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(getFindAllSQL());
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error en findAll", e);
        }
        return list;
    }

    @Override
    public boolean update(T entity) {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(getUpdateSQL())) {
            setUpdateParams(ps, entity);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error en update", e);
        }
    }

    @Override
    public boolean deleteById(ID id) {
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(getDeleteSQL())) {
            setDeleteParam(ps, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error en deleteById", e);
        }
    }
}

