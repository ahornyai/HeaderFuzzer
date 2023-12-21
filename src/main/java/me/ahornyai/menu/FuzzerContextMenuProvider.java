package me.ahornyai.menu;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import lombok.RequiredArgsConstructor;
import me.ahornyai.tabs.FuzzerTab;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FuzzerContextMenuProvider implements ContextMenuItemsProvider {
    private final MontoyaApi api;
    private final FuzzerTab fuzzerTab;

    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event) {
        List<Component> menuItemList = new ArrayList<>();
        JMenuItem retrieveRequestItem = new JMenuItem("Send to fuzzer");

        HttpRequestResponse requestResponse = event.messageEditorRequestResponse().isPresent() ? event.messageEditorRequestResponse().get().requestResponse() : event.selectedRequestResponses().get(0);

        retrieveRequestItem.addActionListener(e -> {
            fuzzerTab.setRequest(requestResponse.request());

        });
        menuItemList.add(retrieveRequestItem);

        return menuItemList;
    }

}
