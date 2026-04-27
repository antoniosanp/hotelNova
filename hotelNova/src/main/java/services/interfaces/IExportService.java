package services.interfaces;

public interface IExportService {
    void exportRoomsCsv(String fileName);
    void exportActiveReservationsCsv(String fileName);
}
