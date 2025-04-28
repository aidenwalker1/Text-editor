package org.cpts.integration;


import io.qameta.allure.TmsLink;
import org.cpts.Doc;
import org.cpts.files.FileHandler;
import org.cpts.findText.FindHandler;
import org.cpts.mainUI.MenuSetup;
import org.cpts.tabs.TabHandler;
import org.cpts.textAttributes.AttributeHandler;
import org.cpts.textAttributes.CustomTextPane;
import org.cpts.textAttributes.TypefaceHandler;
import org.cpts.undo_redo.UndoHandler;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.internal.matchers.Find;

import javax.swing.*;
import javax.swing.text.StyleConstants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UndoIntegrationTest {
    static MockedStatic<FindHandler> mockedFind;

    @BeforeAll
    static void setUp() {
        MenuSetup m = new MenuSetup(new JFrame());
        m.createMenu();
        TabHandler.setUp();
        mockedFind = mockStatic(FindHandler.class);
        mockedFind.when(() -> FindHandler.indexAt(anyInt())).thenReturn(-1);
        mockedFind.when(FindHandler::isReplacing).thenReturn(false);
    }

    @BeforeEach
    void reset() {
        FileHandler.newFile();
    }

    @AfterAll
    static void teardown() {
        mockedFind.close();
    }

    @TmsLink("C4")
    @Test
    void testUndo_applyUndoRedoText() {
        final JMenuItem undoButton = MenuSetup.getUndoButton();
        final JMenuItem redoButton = MenuSetup.getRedoButton();

        // check buttons
        assertEquals(undoButton.getText(), "Undo");
        assertEquals(redoButton.getText(), "Redo");

        String expectedUndoText = "Undo insert Test";

        // insert text
        Doc.insert(0, "Test");
        assertEquals(undoButton.getText(), expectedUndoText);
        assertEquals(redoButton.getText(), "Redo");

        // undo
        UndoHandler.undo();
        assertEquals(undoButton.getText(), "Undo");
        assertTrue(CustomTextPane.getText().isEmpty());

        String expectedRedoText = "Redo insert Test";
        assertEquals(undoButton.getText(), "Undo");
        assertEquals(redoButton.getText(), expectedRedoText);

        // redo
        UndoHandler.redo();
        assertEquals(undoButton.getText(), expectedUndoText);
        assertEquals(redoButton.getText(), "Redo");

        // undo, insert
        UndoHandler.undo();
        Doc.insert(0, "abcd");
        assertEquals(CustomTextPane.getText(), "abcd");
        assertEquals(redoButton.getText(), "Redo");

        // remove
        Doc.remove(0, 1);
        assertEquals(undoButton.getText(), "Undo remove a");

        // undo
        UndoHandler.undo();
        assertEquals(undoButton.getText(), "Undo insert abcd");
        assertEquals(redoButton.getText(), "Redo remove a");

        // select text
        CustomTextPane.getArea().setSelectionStart(0);
        CustomTextPane.getArea().setSelectionEnd(CustomTextPane.getText().length());

        // apply bold
        TypefaceHandler.applyNormalTypeface(StyleConstants.Bold);
        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Bold, true));
        assertEquals(undoButton.getText(), "Undo bold");
        assertEquals(redoButton.getText(), "Redo");

        // undo
        UndoHandler.undo();
        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Bold, false));
        assertEquals(undoButton.getText(), "Undo insert abcd");
        assertEquals(redoButton.getText(), "Redo bold");

        // undo
        UndoHandler.undo();
        assertEquals(undoButton.getText(), "Undo");
        assertEquals(redoButton.getText(), "Redo insert abcd");
    }
}
