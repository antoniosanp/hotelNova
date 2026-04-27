package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class CsvUtil {

    private CsvUtil() {
    }

    public static void writeCsv(String fileName, String[] headers, List<String[]> rows) {
        String headerLine = toLine(headers);
        String content = rows == null ? "" : rows.stream().map(CsvUtil::toLine).collect(Collectors.joining(System.lineSeparator()));
        String csv = headerLine + System.lineSeparator() + content + System.lineSeparator();

        try {
            Files.writeString(Path.of(fileName), csv, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo exportar el archivo " + fileName, e);
        }
    }

    private static String toLine(String[] columns) {
        if (columns == null || columns.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(escape(columns[i]));
        }
        return sb.toString();
    }

    private static String escape(String value) {
        String text = value == null ? "" : value;
        String escaped = text.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}
