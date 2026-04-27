package menu;

import controller.ExportController;

import javax.swing.JOptionPane;

public class ExportMenu {

    private final ExportController exportController;

    public ExportMenu() {
        this.exportController = new ExportController();
    }

    public void open() {
        while (true) {
            String option = JOptionPane.showInputDialog(
                    null,
                    "Export Menu\n1) Exportar habitaciones\n2) Exportar reservas activas\n0) Volver",
                    "Export Menu",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (option == null || "0".equals(option.trim())) {
                return;
            }

            try {
                switch (option.trim()) {
                    case "1" -> {
                        exportController.exportRoomsCsv();
                        JOptionPane.showMessageDialog(null, "Archivo generado: habitaciones_export.csv");
                    }
                    case "2" -> {
                        exportController.exportActiveReservationsCsv();
                        JOptionPane.showMessageDialog(null, "Archivo generado: reservas_activas.csv");
                    }
                    default -> JOptionPane.showMessageDialog(null, "Opción inválida");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
