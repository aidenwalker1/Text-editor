package org.cpts.undo_redo;

import org.cpts.Doc;
import org.cpts.files.FileHandler;
import org.cpts.findText.FindHandler;
import org.cpts.mainUI.MenuSetup;
import org.cpts.tabs.CommandListener;
import org.cpts.tabs.TabHandler;
import org.cpts.textAttributes.CustomTextPane;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import javax.swing.*;
import javax.swing.text.StyleConstants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class CommandTest {
    @BeforeAll
    static void setUp() {
        MenuSetup m = new MenuSetup(new JFrame());
        m.createMenu();
        TabHandler.setUp();
    }

    @BeforeEach
    void reset() {
        FileHandler.newFile();
    }

    @Test
    void testCommands() {
        CommandListener listener = new CommandListener();
        Doc.insert(0, "test");

        listener.keyPressed(makeEventNoCtrl(KeyEvent.VK_C));

        listener.keyPressed(makeEvent(KeyEvent.VK_CONTROL));
        listener.keyPressed(makeEvent(KeyEvent.VK_M));

        CustomTextPane.getArea().setSelectionStart(0);
        CustomTextPane.getArea().setSelectionEnd(2);

        listener.keyPressed(makeEvent(KeyEvent.VK_B));
        listener.keyPressed(makeEvent(KeyEvent.VK_U));
        listener.keyPressed(makeEvent(KeyEvent.VK_I));
        listener.keyPressed(makeEvent(KeyEvent.VK_COMMA));
        listener.keyPressed(makeEvent(KeyEvent.VK_PERIOD));

        assert(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Bold, true));
        assert(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Superscript, true));


        listener.keyPressed(makeEvent(KeyEvent.VK_Z));
        assert(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Superscript, false));


        listener.keyPressed(makeEvent(KeyEvent.VK_Y));
        assert(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Superscript, true));

    }

    KeyEvent makeEventNoCtrl(int event) {
        JFrame f = new JFrame();
        KeyEvent e = new KeyEvent(f, 0, 0, 0, event, 'c', 0);
        return e;
    }

    KeyEvent makeEvent(int event) {
        JFrame f = new JFrame();
        KeyEvent e = new KeyEvent(f, 0, 0, KeyEvent.CTRL_DOWN_MASK, event, 'c', 0);
        return e;
    }
}
