package controller;

import services.ExportService;
import services.interfaces.IExportService;

import javax.swing.JOptionPane;

public class ExportController {

    private final IExportService exportService;

    public ExportController() {
        this(new ExportService());
    }

    public ExportController(IExportService exportService) {
        this.exportService = exportService;
    }

    public void exportRoomsCsv() {
        exportService.exportRoomsCsv("habitaciones_export.csv");
    }

    public void exportActiveReservationsCsv() {
        exportService.exportActiveReservationsCsv("reservas_activas.csv");
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
