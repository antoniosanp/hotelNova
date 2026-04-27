package utils;

import java.util.ArrayList;
import java.util.List;

public class TableFormatterUtil {

    private TableFormatterUtil() {
    }

    public static String format(String[] headers, List<String[]> rows) {
        if (headers == null || headers.length == 0) {
            return "Sin columnas";
        }

        List<String[]> safeRows = rows == null ? new ArrayList<>() : rows;
        int[] widths = new int[headers.length];

        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i] == null ? 0 : headers[i].length();
        }

        for (String[] row : safeRows) {
            for (int i = 0; i < headers.length; i++) {
                String value = (row != null && i < row.length && row[i] != null) ? row[i] : "";
                widths[i] = Math.max(widths[i], value.length());
            }
        }

        StringBuilder sb = new StringBuilder();
        appendRow(sb, headers, widths);
        appendSeparator(sb, widths);
        for (String[] row : safeRows) {
            String[] normalized = new String[headers.length];
            for (int i = 0; i < headers.length; i++) {
                normalized[i] = (row != null && i < row.length && row[i] != null) ? row[i] : "";
            }
            appendRow(sb, normalized, widths);
        }
        return sb.toString();
    }

    private static void appendRow(StringBuilder sb, String[] values, int[] widths) {
        for (int i = 0; i < values.length; i++) {
            sb.append("| ").append(pad(values[i], widths[i])).append(' ');
        }
        sb.append('|').append(System.lineSeparator());
    }

    private static void appendSeparator(StringBuilder sb, int[] widths) {
        for (int width : widths) {
            sb.append("| ").append("-".repeat(Math.max(1, width))).append(' ');
        }
        sb.append('|').append(System.lineSeparator());
    }

    private static String pad(String value, int width) {
        String text = value == null ? "" : value;
        int missing = width - text.length();
        if (missing <= 0) {
            return text;
        }
        return text + " ".repeat(missing);
    }
}
