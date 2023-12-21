package me.ahornyai.headerfuzzer.tabs;

import burp.api.montoya.http.message.HttpHeader;
import burp.api.montoya.http.message.requests.HttpRequest;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HeaderTableModel extends AbstractTableModel {
    private List<HeaderTableEntry> entries;

    public HeaderTableModel() {
        this.entries = new ArrayList<>();
    }

    @Override
    public synchronized int getRowCount() {
        return entries.size();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 1 -> "Header";
            case 2 -> "Value";
            default -> "";
        };
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return switch (column) {
            case 0 -> Boolean.class;
            case 1, 2 -> String.class;
            default -> Object.class;
        };
    }

    @Override
    public synchronized Object getValueAt(int rowIndex, int columnIndex) {
        HeaderTableEntry entry = entries.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> entry.isConstant();
            case 1 -> entry.getHeader().name();
            case 2 -> entry.getHeader().value();
            default -> "";
        };
    }

    public synchronized void add(HeaderTableEntry responseReceived) {
        int index = entries.size();
        entries.add(responseReceived);

        fireTableRowsInserted(index, index);
    }

    public synchronized HeaderTableEntry get(int rowIndex) {
        return entries.get(rowIndex);
    }

    public synchronized Optional<Boolean> isConstant(String headerName) {
        for (HeaderTableEntry entry : entries) {
            if (entry.getHeader().name().equalsIgnoreCase(headerName)) {
                return Optional.of(entry.isConstant());
            }
        }

        return Optional.empty();
    }

    //TODO: Use HashMap for better performance, this is a naive solution
    public synchronized void updateModel(HttpRequest request) {
        List<HttpHeader> requestHeaders = request.headers();
        List<HeaderTableEntry> newEntries = new ArrayList<>();

        // Update the existing headers (the constant boolean will be the same) and add the new headers
        for (HttpHeader header : requestHeaders) {
            newEntries.add(new HeaderTableEntry(isConstant(header.name()).orElse(true), header));
        }

        this.entries = newEntries;
        fireTableDataChanged();
    }

}