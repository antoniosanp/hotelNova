package services;

import dao.GuestDAO;
import dao.impl.GuestDAOImpl;
import exceptions.BusinessException;
import exceptions.NotFoundException;
import model.Guest;
import services.interfaces.IGuestService;
import utils.AppLogger;

import java.util.List;

public class GuestService implements IGuestService {

    private final GuestDAO guestDAO;

    public GuestService() {
        this(new GuestDAOImpl());
    }

    public GuestService(GuestDAO guestDAO) {
        this.guestDAO = guestDAO;
    }

    @Override
    public Guest createGuest(String name, String email) {
        AppLogger.http("POST", "/guests");
        validate(name, email);

        if (guestDAO.findByEmail(email).isPresent()) {
            throw new BusinessException("Ya existe un huésped con ese email");
        }

        return guestDAO.save(new Guest(name, email));
    }

    @Override
    public boolean updateGuest(Guest guest) {
        AppLogger.http("PATCH", "/guests/" + guest.getId());
        validate(guest.getName(), guest.getEmail());

        guestDAO.findById(guest.getId())
                .orElseThrow(() -> new NotFoundException("No existe el huésped a actualizar"));

        guestDAO.findByEmail(guest.getEmail()).ifPresent(existing -> {
            if (existing.getId() != guest.getId()) {
                throw new BusinessException("El email ya está en uso por otro huésped");
            }
        });
        return guestDAO.update(guest);
    }

    @Override
    public boolean deleteGuest(int id) {
        AppLogger.http("DELETE", "/guests/" + id);
        if (guestDAO.findById(id).isEmpty()) {
            throw new NotFoundException("No existe el huésped");
        }
        return guestDAO.deleteById(id);
    }

    @Override
    public List<Guest> listGuests() {
        AppLogger.http("GET", "/guests");
        return guestDAO.findAll();
    }

    private void validate(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("El nombre del huésped es obligatorio");
        }
        if (email == null || email.isBlank()) {
            throw new BusinessException("El email del huésped es obligatorio");
        }
    }
}
