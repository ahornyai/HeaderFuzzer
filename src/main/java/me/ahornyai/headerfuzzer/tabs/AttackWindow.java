package me.ahornyai.headerfuzzer.tabs;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.UserInterface;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import lombok.Getter;
import me.ahornyai.headerfuzzer.tabs.table.RequestTableModel;

import javax.swing.*;
import javax.swing.table.TableColumnModel;

@Getter
public class AttackWindow extends JFrame {
    private final MontoyaApi api;
    private final RequestTableModel tableModel;
    private HttpRequestEditor requestEditor;
    private HttpResponseEditor responseEditor;
    private JProgressBar progressBar;

    public AttackWindow(MontoyaApi api) {
        this.api = api;
        this.tableModel = new RequestTableModel();

        SwingUtilities.invokeLater(this::initUserInterface);
    }

    private void initUserInterface() {
        JFrame frame = new JFrame("HeaderFuzzer - " + "youtube.com"/*request.httpService().host()*/);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Request log on the top, request viewer and progressbar on the bottom.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5d);

        JTable table = new JTable(tableModel);
        TableColumnModel columnModel = table.getColumnModel();

        //TODO: better solution, because this is pretty bad
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(800);
        columnModel.getColumn(2).setPreferredWidth(30);
        columnModel.getColumn(3).setPreferredWidth(30);
        columnModel.getColumn(4).setPreferredWidth(100);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        splitPane.setLeftComponent(scrollPane);

        // Request viewer on the top, progressbar on the bottom
        JSplitPane viewerProgressbarPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        viewerProgressbarPane.setResizeWeight(0.95d);

        // Request on the left, response on the right
        JSplitPane editorSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        UserInterface userInterface = api.userInterface();

        this.requestEditor = userInterface.createHttpRequestEditor();
        this.responseEditor = userInterface.createHttpResponseEditor();

        editorSplitPane.setLeftComponent(requestEditor.uiComponent());
        editorSplitPane.setRightComponent(responseEditor.uiComponent());
        editorSplitPane.setResizeWeight(0.5d);

        this.progressBar = new JProgressBar(SwingConstants.HORIZONTAL,0,1024);
        progressBar.setValue(500);

        viewerProgressbarPane.setLeftComponent(editorSplitPane);
        viewerProgressbarPane.setRightComponent(progressBar);

        splitPane.setRightComponent(viewerProgressbarPane);

        frame.add(splitPane);
        frame.setVisible(true);
    }

}
