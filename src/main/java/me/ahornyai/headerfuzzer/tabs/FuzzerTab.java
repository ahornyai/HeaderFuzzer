package me.ahornyai.headerfuzzer.tabs;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.ui.UserInterface;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.TableColumn;

@Getter
public class FuzzerTab extends JSplitPane {
    private final MontoyaApi api;
    private final HeaderTableModel tableModel;
    private HttpRequestEditor requestEditor;
    private HttpRequest request;

    public FuzzerTab(MontoyaApi api) {
        super(JSplitPane.HORIZONTAL_SPLIT);

        this.api = api;
        this.tableModel = new HeaderTableModel();

        initUserInterface();
    }

    private void initUserInterface() {
        // Creating request editor
        JTabbedPane tabs = new JTabbedPane();
        UserInterface userInterface = api.userInterface();

        this.requestEditor = userInterface.createHttpRequestEditor();
        tabs.addTab("Request", requestEditor.uiComponent());

        setRightComponent(tabs);

        // Creating the left side of the tab
        JSplitPane leftSplitPane = new JSplitPane(VERTICAL_SPLIT);
        JTable table = new JTable(tableModel);

        // Set the checkbox column size
        TableColumn checkboxColumn = table.getColumnModel().getColumn(0);
        checkboxColumn.setMaxWidth(30);
        checkboxColumn.setResizable(false);

        // TODO: Launch attack pane
        JScrollPane scrollPane = new JScrollPane(table);
        leftSplitPane.setLeftComponent(scrollPane);
        leftSplitPane.setRightComponent(new JSplitPane());

        setLeftComponent(leftSplitPane);
    }

    public void setRequest(HttpRequest request) {
        this.request = request;

        requestEditor.setRequest(request);
        tableModel.updateModel(request);
    }

}
