package me.ahornyai.headerfuzzer.tabs.table;

import burp.api.montoya.http.message.HttpHeader;
import burp.api.montoya.http.message.requests.HttpRequest;
import lombok.Getter;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class HeaderTableModel extends AbstractTableModel {
    private List<HeaderTableEntry> entries;

    public HeaderTableModel() {
        this.entries = new CopyOnWriteArrayList<>();
    }

    @Override
    public synchronized int getRowCount() {
        return entries.size();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    @Override
    public void setValueAt(Object newValue, int rowIndex, int columnIndex) {
        HeaderTableEntry entry = entries.get(rowIndex);

        // The table is only editable in the checkbox column.
        entry.setConstant((Boolean) newValue);
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
        List<HeaderTableEntry> newEntries = new CopyOnWriteArrayList<>();

        // Update the existing headers (the constant boolean will be the same) and add the new headers
        for (HttpHeader header : requestHeaders) {
            newEntries.add(new HeaderTableEntry(isConstant(header.name()).orElse(true), header));
        }

        this.entries = newEntries;
        fireTableDataChanged();
    }

}