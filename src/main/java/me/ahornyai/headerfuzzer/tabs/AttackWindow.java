package me.ahornyai.headerfuzzer.tabs;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.requests.HttpRequest;
import me.ahornyai.headerfuzzer.tabs.table.HeaderTableModel;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class AttackWindow extends JFrame {
    private final MontoyaApi api;
    private final HeaderTableModel tableModel;
    private final HttpRequest request;

    public AttackWindow(MontoyaApi api, HeaderTableModel tableModel, HttpRequest request) {
        this.api = api;
        this.tableModel = tableModel;
        this.request = request;

        JFrame frame = new JFrame("HeaderFuzzer - " + request.httpService().host());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
