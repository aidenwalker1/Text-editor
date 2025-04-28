package org.cpts.tabs;

import org.cpts.Doc;
import org.cpts.mainUI.MenuSetup;
import org.cpts.textAttributes.CustomTextPane;
import org.cpts.undo_redo.UndoHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class TabHandlerTest {
    @BeforeAll
    static void setUp() {
        MenuSetup m = new MenuSetup(new JFrame());
        m.createMenu();

        TabHandler.setUp();
    }

    @BeforeEach
    void reset() {
        TabHandler.getPane().setSelectedIndex(0);
        while (TabHandler.getPane().getTabCount() > 1) {
            TabHandler.removePane();
        }
        TabHandler.removePane();
    }

    @AfterAll
    static void resetAll() {
        TabHandler.getPane().setSelectedIndex(0);
        while (TabHandler.getPane().getTabCount() > 1) {
            TabHandler.removePane();
        }
        TabHandler.removePane();
    }

    @Test
    void testAddAndChangePane() {
        Doc.insert(0, "test");

        TabHandler.addPane();

        assert (TabHandler.getPane().getSelectedIndex() == 0);
        assert (TabHandler.getPane().getTabCount() == 2);

        TabHandler.getPane().setSelectedIndex(1);

        TabHandler.changeTab();

        assert (CustomTextPane.getText().isEmpty());
    }

    @Test
    void testRemovePane() {
        Doc.insert(0, "test");

        TabHandler.removePane();

        assert (CustomTextPane.getText().isEmpty());

        Doc.insert(0, "test");
        TabHandler.addPane();
        assert (TabHandler.getPane().getTabCount() == 2);

        assert (TabHandler.getPane().getSelectedIndex() == 0);

        TabHandler.getPane().setSelectedIndex(1);

        TabHandler.changeTab();

        Doc.insert(0, "t");

        TabHandler.removePane();

        assert (CustomTextPane.getText().equals("test"));
        assert (TabHandler.getPane().getTabCount() == 1);
    }

    @Test
    void testResetUndo() {
        Doc.insert(0, "test");
        assert (UndoHandler.getUndos().size() == 1);

        TabHandler.resetUndo();
        assert (UndoHandler.getUndos().isEmpty());

    }

    @Test
    void testChangeTabTitle() {
        assert (TabHandler.getPane().getTitleAt(0).equals("Document 0"));
        TabHandler.addPane();
        assert (TabHandler.getPane().getTitleAt(1).equals("Document 1"));

        TabHandler.changeTabTitle("newName", false);
        assert (TabHandler.getPane().getTitleAt(0).equals("newName"));

        TabHandler.changeTabTitle("Document", true);
        assert (TabHandler.getPane().getTitleAt(0).equals("Document0"));
    }
}