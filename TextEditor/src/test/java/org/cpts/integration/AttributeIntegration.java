package org.cpts.integration;

import io.qameta.allure.TmsLink;
import org.cpts.Doc;
import org.cpts.files.FileHandler;
import org.cpts.findText.FindHandler;
import org.cpts.mainUI.MenuSetup;
import org.cpts.tabs.TabHandler;
import org.cpts.textAttributes.AttributeHandler;
import org.cpts.textAttributes.CustomTextPane;
import org.cpts.undo_redo.UndoHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.swing.*;
import javax.swing.text.StyleConstants;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;

//Pairwise integration for AttributeHandler.applyAttributes and CustomTextPane.applyAttributes
public class AttributeIntegration {

    static MockedStatic<FindHandler> mockedFind;
    static MockedStatic<UndoHandler> mockedUndo;

    @BeforeAll
    static void setUp() {
        MenuSetup m = new MenuSetup(new JFrame());
        m.createMenu();
        TabHandler.setUp();
        mockedFind = mockStatic(FindHandler.class);
        mockedFind.when(() -> FindHandler.indexAt(anyInt())).thenReturn(-1);
        mockedFind.when(FindHandler::isReplacing).thenReturn(false);
        mockedUndo = mockStatic(UndoHandler.class);
        mockedUndo.when(UndoHandler::getUndos).thenReturn(new Stack<>());
        mockedUndo.when(UndoHandler::getRedos).thenReturn(new Stack<>());
        mockedUndo.when(UndoHandler::isUndoing).thenReturn(false);
        mockedUndo.when(UndoHandler::isRedoing).thenReturn(false);

    }

    @BeforeEach
    void reset() {
        FileHandler.newFile();
    }

    @AfterAll
    static void teardown(){
        mockedFind.close();
        mockedUndo.close();
    }
    @TmsLink("C22")
    @Test
    void applyAttributesIntegration(){

        // insert text
        Doc.insert(0, "Test");
        assertEquals("Test", CustomTextPane.getText());

        CustomTextPane.getArea().setSelectionStart(0);
        CustomTextPane.getArea().setSelectionEnd(3);
        AttributeHandler.applyAttributes(StyleConstants.Bold, true);

        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Bold, true));

        Doc.insert(4, "abcd");

        assertEquals("Testabcd", CustomTextPane.getText());

        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Bold, true));
        assertTrue(CustomTextPane.extractStyle(4).containsAttribute(StyleConstants.Bold, false));
    }

}
