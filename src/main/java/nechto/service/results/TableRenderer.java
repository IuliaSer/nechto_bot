package nechto.service.results;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TableRenderer {
    public static final class Column {
        public final String header;
        public final int minWidth;
        public final boolean padRight;
        public Column(String header, int minWidth, boolean padRight) {
            this.header = header; this.minWidth = minWidth; this.padRight = padRight;
        }
    }

    private final List<Column> columns = new ArrayList<>();
    private final List<List<String>> rows = new ArrayList<>();
    private final List<Integer> widths = new ArrayList<>();

    public TableRenderer addColumn(String header, int minWidth, boolean padRight) {
        columns.add(new Column(header, minWidth, padRight));
        widths.add(Math.max(header.length(), minWidth));
        return this;
    }
    public TableRenderer addRow(String... cells) {
        rows.add(Arrays.asList(cells));
        for (int i = 0; i < cells.length; i++) {
            widths.set(i, Math.max(widths.get(i), cells[i] == null ? 0 : cells[i].length()));
        }
        return this;
    }

    public String renderAsInlineCodeLines() {
        StringBuilder sb = new StringBuilder();
        // header:
        sb.append("`");
        for (int i = 0; i < columns.size(); i++) {
            var c = columns.get(i);
            sb.append(pad(c.header, widths.get(i), c.padRight));
        }
        sb.append("`\n");
        // rows:
        for (var row : rows) {
            sb.append("`");
            for (int i = 0; i < columns.size(); i++) {
                var c = columns.get(i);
                var cell = i < row.size() && row.get(i) != null ? row.get(i) : "";
                sb.append(pad(cell, widths.get(i), c.padRight));
            }
            sb.append("`\n");
        }
        return sb.toString();
    }

    private static String pad(String s, int width, boolean right) {
        if (s == null) s = "";
        if (s.length() >= width) return s;
        int spaces = width - s.length();
        return right ? s + " ".repeat(spaces) : " ".repeat(spaces) + s;
    }
}
