package me.ahornyai.headerfuzzer.tabs;

import lombok.AllArgsConstructor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@AllArgsConstructor
public class EditorKeyListener implements KeyListener {
    private final FuzzerTab fuzzerTab;

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (fuzzerTab.getRequestEditor().isModified()) {
            fuzzerTab.getTableModel().updateModel(fuzzerTab.getRequestEditor().getRequest());
        }
    }
}
