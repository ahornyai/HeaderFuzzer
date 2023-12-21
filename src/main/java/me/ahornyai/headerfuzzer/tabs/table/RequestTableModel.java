package me.ahornyai.headerfuzzer.tabs.table;

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.MimeType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RequestTableModel extends AbstractTableModel {
    private final List<HttpRequestResponse> entries;

    public RequestTableModel() {
        this.entries = new ArrayList<>();
    }

    @Override
    public synchronized int getRowCount() {
        return entries.size();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "#";
            case 1 -> "URL";
            case 2 -> "Status code";
            case 3 -> "Length";
            case 4 -> "MIME type";
            default -> "";
        };
    }

    @Override
    public synchronized Object getValueAt(int rowIndex, int columnIndex) {
        HttpRequestResponse entry = entries.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> rowIndex;
            case 1 -> entry.request().url();
            case 2 -> entry.response().statusCode();
            case 3 -> entry.response().body().length();
            case 4 -> entry.response().mimeType().name();
            default -> "";
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 2, 3 -> Integer.class;
            case 1 -> String.class;
            case 4 -> MimeType.class;
            default -> Object.class;
        };
    }
}