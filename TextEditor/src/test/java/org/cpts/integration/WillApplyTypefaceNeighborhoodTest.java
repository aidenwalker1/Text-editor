package org.cpts.integration;

import io.qameta.allure.TmsLink;
import org.cpts.Doc;
import org.cpts.files.FileHandler;
import org.cpts.findText.FindHandler;
import org.cpts.mainUI.MenuSetup;
import org.cpts.tabs.TabHandler;
import org.cpts.textAttributes.CustomTextPane;
import org.cpts.textAttributes.TypefaceHandler;
import org.cpts.undo_redo.UndoHandler;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import javax.swing.*;
import javax.swing.text.StyleConstants;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;

public class WillApplyTypefaceNeighborhoodTest {
    final static Object superscript = StyleConstants.Superscript;
    final static Object subscript = StyleConstants.Subscript;
    final static Object bold = StyleConstants.Bold;
    final static Object italic = StyleConstants.Italic;

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
    static void teardown() {
        mockedFind.close();
        mockedUndo.close();
    }

    @TmsLink("C1")
    @Test
    void testWillApplyTypeface_applySuperscript() {
        // insert text
        Doc.insert(0, "test");
        assertEquals("test", CustomTextPane.getText());

        // check there is no style
        Doc.assertDoesNotContainsAttribute(0, superscript);
        Doc.assertDoesNotContainsAttribute(0, subscript);

        // select whole text
        Doc.selectText(0, 4);

        // apply superscript, check its there and subscript isnt
        TypefaceHandler.applySuperscript();
        Doc.assertContainsAttribute(0, superscript);
        Doc.assertDoesNotContainsAttribute(0, subscript);

        // reset superscript, check it and subscript isnt there
        TypefaceHandler.applySuperscript();
        Doc.assertDoesNotContainsAttribute(0, superscript);
        Doc.assertDoesNotContainsAttribute(0, subscript);

        // apply subscript, check its there and superscript isnt
        TypefaceHandler.applySubscript();
        Doc.assertDoesNotContainsAttribute(0, superscript);
        Doc.assertContainsAttribute(0, subscript);

        // apply superscript, check its there and subscript was reset
        TypefaceHandler.applySuperscript();
        Doc.assertContainsAttribute(0, superscript);
        Doc.assertDoesNotContainsAttribute(0, subscript);

        // apply superscript to reset, check
        TypefaceHandler.applySuperscript();
        Doc.assertDoesNotContainsAttribute(0, superscript);
        Doc.assertDoesNotContainsAttribute(0, subscript);

        // select first char
        Doc.selectText(0, 1);

        // apply superscript to first char
        TypefaceHandler.applySuperscript();
        Doc.assertContainsAttribute(0, superscript);
        Doc.assertDoesNotContainsAttribute(0, subscript);

        // select whole text
        Doc.selectText(0, 4);

        // apply superscript, make sure first char is still superscript
        TypefaceHandler.applySuperscript();
        Doc.assertContainsAttribute(0, superscript);
        Doc.assertDoesNotContainsAttribute(0, subscript);
    }

    @TmsLink("C2")
    @Test
    void testWillApplyTypeface_applySubscript() {
        // insert text
        Doc.insert(0, "test");
        assertEquals("test", CustomTextPane.getText());

        // check there is no style
        Doc.assertDoesNotContainsAttribute(0, subscript);
        Doc.assertDoesNotContainsAttribute(0, superscript);

        // select whole text
        Doc.selectText(0, 4);

        // apply subscript, check its there and superscript isnt
        TypefaceHandler.applySubscript();
        Doc.assertContainsAttribute(0, subscript);
        Doc.assertDoesNotContainsAttribute(0, superscript);

        // reset subscript, check it and superscript isnt there
        TypefaceHandler.applySubscript();
        Doc.assertDoesNotContainsAttribute(0, subscript);
        Doc.assertDoesNotContainsAttribute(0, superscript);

        // apply superscript, check its there and subscript isnt
        TypefaceHandler.applySuperscript();
        Doc.assertDoesNotContainsAttribute(0, subscript);
        Doc.assertContainsAttribute(0, superscript);

        // apply subscript, check its there and superscript was reset
        TypefaceHandler.applySubscript();
        Doc.assertContainsAttribute(0, subscript);
        Doc.assertDoesNotContainsAttribute(0, superscript);

        // apply subscript to reset, check
        TypefaceHandler.applySubscript();
        Doc.assertDoesNotContainsAttribute(0, subscript);
        Doc.assertDoesNotContainsAttribute(0, superscript);

        // select first char
        Doc.selectText(0, 1);

        // apply subscript to first char
        TypefaceHandler.applySubscript();
        Doc.assertContainsAttribute(0, subscript);
        Doc.assertDoesNotContainsAttribute(0, superscript);

        // select whole text
        Doc.selectText(0, 4);

        // apply subscript, make sure first char is still subscript
        TypefaceHandler.applySubscript();
        Doc.assertContainsAttribute(0, subscript);
        Doc.assertDoesNotContainsAttribute(0, superscript);
    }

    @TmsLink("C3")
    @Test
    void testWillApplyTypeface_applyNormal() {
        // insert text
        Doc.insert(0, "test");
        assertEquals("test", CustomTextPane.getText());

        // check there is no style
        Doc.assertDoesNotContainsAttribute(0, bold);
        Doc.assertDoesNotContainsAttribute(0, italic);

        // select whole text
        Doc.selectText(0, 4);

        // apply bold, check its there
        TypefaceHandler.applyNormalTypeface(bold);
        Doc.assertContainsAttribute(0, bold);
        Doc.assertDoesNotContainsAttribute(0, italic);

        // apply italic, check both are there
        TypefaceHandler.applyNormalTypeface(italic);
        Doc.assertContainsAttribute(0, bold);
        Doc.assertContainsAttribute(0, italic);

        // apply bold, check it was reset
        TypefaceHandler.applyNormalTypeface(bold);
        Doc.assertDoesNotContainsAttribute(0, bold);
        Doc.assertContainsAttribute(0, italic);

        // apply italic, check it was reset
        TypefaceHandler.applyNormalTypeface(italic);
        Doc.assertDoesNotContainsAttribute(0, bold);
        Doc.assertDoesNotContainsAttribute(0, italic);

        // select first char
        Doc.selectText(0, 1);

        // apply bold to first char
        TypefaceHandler.applyNormalTypeface(bold);
        Doc.assertContainsAttribute(0, bold);
        Doc.assertDoesNotContainsAttribute(0, italic);

        // select whole text
        Doc.selectText(0, 4);

        // apply bold, make sure first char is still bold
        TypefaceHandler.applyNormalTypeface(bold);
        Doc.assertContainsAttribute(0, bold);
        Doc.assertDoesNotContainsAttribute(0, italic);
    }
}
