package org.cpts.undo_redo;

import org.cpts.Doc;
import org.cpts.files.FileHandler;
import org.cpts.findText.FindHandler;
import org.cpts.mainUI.MenuSetup;
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
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@Execution(SAME_THREAD)
class UndoFilterTest {
    static MockedStatic<UndoHandler> undoStub;

    @BeforeAll
    static void setUp() {
        MenuSetup m = new MenuSetup(new JFrame());
        m.createMenu();
        TabHandler.setUp();

        undoStub = mockStatic(UndoHandler.class);
        when(UndoHandler.isUndoing()).thenReturn(false);
        when(UndoHandler.isRedoing()).thenReturn(false);
        when(UndoHandler.getUndos()).thenReturn(new Stack<>());
        when(UndoHandler.getRedos()).thenReturn(new Stack<>());
    }

    @BeforeEach
    void reset() {
        FileHandler.newFile();
    }

    @AfterAll
    static void teardown() {
        undoStub.close();
    }

    @Test
    void testReplace() {
        Doc.insert(0,"test");

        assert (CustomTextPane.getText().equals("test"));

        Doc.insert(2,"ss");

        assert (CustomTextPane.getText().equals("tessst"));

        Doc.replace(1, 2, "1111");

        assert (CustomTextPane.getText().equals("t1111sst"));

        Doc.insert(CustomTextPane.getText().length(), "t");

        assert (CustomTextPane.getText().equals("t1111sstt"));
    }

    @Test
    void testRemove() {
        Doc.insert(0,"test");
        assert (CustomTextPane.getText().equals("test"));
        Doc.remove(0, 2);
        assert (CustomTextPane.getText().equals("st"));

        Doc.remove(0, 1);

        assert (CustomTextPane.getText().equals("t"));
    }

    @Test
    void testReplaceFinding() {
        MockedStatic<JOptionPane> optionStub = mockStatic(JOptionPane.class);
        optionStub.when(()->JOptionPane.showInputDialog(org.cpts.mainUI.MenuSetup.getMainWindow(),
                "Enter text to find:")).thenReturn("test");

        Doc.insert(0, "test");

        assert(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Background, Color.WHITE));

        FindHandler.findCommand();

        assert(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Background, Color.CYAN));

        Doc.insert(0, "t");
        assert(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Background, Color.WHITE));

        optionStub.close();

        FindHandler.findCommand();
    }

    @Test
    void testCanChange() {
        MockedStatic<FindHandler> findStub = mockStatic(FindHandler.class);
        when(FindHandler.indexAt(anyInt())).thenReturn(-1);

        when(UndoHandler.isUndoing()).thenReturn(true);
        when(UndoHandler.isRedoing()).thenReturn(false);
        when(FindHandler.isReplacing()).thenReturn(false);

        Doc.insert(0, "t");
        assert(CustomTextPane.getText().equals("t"));

        when(UndoHandler.isUndoing()).thenReturn(false);
        when(UndoHandler.isRedoing()).thenReturn(true);
        when(FindHandler.isReplacing()).thenReturn(false);

        Doc.insert(0, "e");

        assert(CustomTextPane.getText().equals("et"));

        when(UndoHandler.isUndoing()).thenReturn(false);
        when(UndoHandler.isRedoing()).thenReturn(false);
        when(FindHandler.isReplacing()).thenReturn(true);

        Doc.insert(0, "s");
        assert(CustomTextPane.getText().equals("set"));

        when(UndoHandler.isUndoing()).thenReturn(true);
        when(UndoHandler.isRedoing()).thenReturn(true);
        when(FindHandler.isReplacing()).thenReturn(true);

        Doc.insert(0, "t");
        assert(CustomTextPane.getText().equals("tset"));

        findStub.reset();
        findStub.close();
    }
}