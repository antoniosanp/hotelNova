package services;

import dao.RoomDAO;
import dao.impl.RoomDAOImpl;
import exceptions.BusinessException;
import exceptions.NotFoundException;
import model.Room;
import services.interfaces.IRoomService;
import utils.AppLogger;

import java.util.List;
import java.util.Locale;

public class RoomService implements IRoomService {

    private static final String STATE_DISPONIBLE = "DISPONIBLE";
    private static final String STATE_OCUPADA = "OCUPADA";
    private final RoomDAO roomDAO;

    public RoomService() {
        this(new RoomDAOImpl());
    }

    public RoomService(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    @Override
    public Room createRoom(int roomNumber, String roomType, int capacity, double price, String state) {
        AppLogger.http("POST", "/rooms");

        if (roomNumber <= 0) {
            throw new BusinessException("Número de habitación inválido");
        }
        if (roomType == null || roomType.isBlank()) {
            throw new BusinessException("El tipo de habitación es obligatorio");
        }
        if (capacity <= 0) {
            throw new BusinessException("Capacidad inválida");
        }
        if (price <= 0) {
            throw new BusinessException("Precio por noche inválido");
        }
        if (roomDAO.findByNumber(roomNumber).isPresent()) {
            throw new BusinessException("El número de habitación ya existe");
        }

        String normalizedState = normalizeState(state);
        Room room = new Room(roomNumber, roomType.trim(), capacity, price, normalizedState);
        room.setActive(true);
        return roomDAO.save(room);
    }

    @Override
    public boolean updateRoom(Room room) {
        AppLogger.http("PATCH", "/rooms/" + room.getId());
        Room existing = roomDAO.findById(room.getId())
                .orElseThrow(() -> new NotFoundException("No existe la habitación a actualizar"));

        roomDAO.findByNumber(room.getRoom_number()).ifPresent(other -> {
            if (other.getId() != room.getId()) {
                throw new BusinessException("El número de habitación ya está en uso");
            }
        });

        if (room.getRoom_type() == null || room.getRoom_type().isBlank()) {
            throw new BusinessException("El tipo de habitación es obligatorio");
        }
        room.setRoom_state(normalizeState(room.getRoom_state()));
        room.setCreatedAt(existing.getCreatedAt());
        return roomDAO.update(room);
    }

    @Override
    public boolean deleteRoom(int roomId) {
        AppLogger.http("DELETE", "/rooms/" + roomId);
        if (roomDAO.findById(roomId).isEmpty()) {
            throw new NotFoundException("No existe la habitación");
        }
        return roomDAO.deleteById(roomId);
    }

    @Override
    public boolean toggleActive(int roomId, boolean active) {
        AppLogger.http("PATCH", "/rooms/" + roomId + "/active");
        if (roomDAO.findById(roomId).isEmpty()) {
            throw new NotFoundException("No existe la habitación");
        }
        return roomDAO.updateIsActive(roomId, active);
    }

    @Override
    public List<Room> listRooms() {
        AppLogger.http("GET", "/rooms");
        return roomDAO.findAll();
    }

    @Override
    public List<Room> listRoomsByState(String state) {
        AppLogger.http("GET", "/rooms?state=" + state);
        String normalizedState = normalizeState(state);
        return roomDAO.findByState(normalizedState);
    }

    @Override
    public List<Room> listRoomsByType(String type) {
        AppLogger.http("GET", "/rooms?type=" + type);
        if (type == null || type.isBlank()) {
            throw new BusinessException("El tipo de habitación es obligatorio para filtrar");
        }
        return roomDAO.findByType(type.trim());
    }

    private String normalizeState(String state) {
        if (state == null || state.isBlank()) {
            return STATE_DISPONIBLE;
        }
        String normalized = state.trim().toUpperCase(Locale.ROOT);
        if (!STATE_DISPONIBLE.equals(normalized) && !STATE_OCUPADA.equals(normalized)) {
            throw new BusinessException("Estado inválido. Usa DISPONIBLE u OCUPADA");
        }
        return normalized;
    }
}
