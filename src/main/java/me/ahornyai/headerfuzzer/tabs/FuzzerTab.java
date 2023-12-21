package me.ahornyai.headerfuzzer.tabs;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.ui.UserInterface;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import lombok.Getter;
import me.ahornyai.headerfuzzer.tabs.table.HeaderTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

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

        SwingUtilities.invokeLater(this::initUserInterface);
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
        leftSplitPane.setResizeWeight(0.9d);

        // Set the checkbox column size
        JTable table = new JTable(tableModel);
        TableColumn checkboxColumn = table.getColumnModel().getColumn(0);
        checkboxColumn.setMaxWidth(30);
        checkboxColumn.setResizable(false);

        JScrollPane scrollPane = new JScrollPane(table);
        leftSplitPane.setLeftComponent(scrollPane);

        // Launch attack button
        JButton launchAttack = new JButton("Launch attack");
        launchAttack.addActionListener(actionEvent -> new AttackWindow(api));
        leftSplitPane.setRightComponent(launchAttack);

        setLeftComponent(leftSplitPane);

        // As far as I know the Burp API doesn't have an event that is called when a request in an HttpRequestEditor gets modified.
        // So we need to do some sketchy java hacking - we register our key listener to every component inside the requestEditor
        // We need to do this, because if we only register the parent, the listener doesn't get called.
        for (Component component : getAllComponents(requestEditor.uiComponent().getParent())) {
            component.addKeyListener(new EditorKeyListener(this));
        }
    }

    // https://stackoverflow.com/questions/6495769/how-to-get-all-elements-inside-a-jframe
    private List<Component> getAllComponents(Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container)
                compList.addAll(getAllComponents((Container) comp));
        }
        return compList;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;

        requestEditor.setRequest(request);
        tableModel.updateModel(request);
    }

}
