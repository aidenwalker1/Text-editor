package org.cpts.undo_redo;

import org.cpts.Doc;
import org.cpts.files.FileHandler;
import org.cpts.findText.FindHandler;
import org.cpts.mainUI.MainWindow;
import org.cpts.mainUI.MenuSetup;
import org.cpts.tabs.TabHandler;
import org.cpts.textAttributes.CustomTextPane;
import org.cpts.textAttributes.TypefaceHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@Execution(SAME_THREAD)
public class UndoHandlerTest {

    @Mock
    private static MockedStatic<FindHandler> findStub;

    @BeforeAll
    static void setUp() {
        MenuSetup m = new MenuSetup(new JFrame());
        m.createMenu();
        TabHandler.setUp();
        findStub = mockStatic(FindHandler.class);
        when(FindHandler.isFinding()).thenReturn(false);
        when(FindHandler.isReplacing()).thenReturn(false);
        when(FindHandler.indexAt(anyInt())).thenReturn(-1);
    }

    @BeforeEach
    void reset() {
        FileHandler.newFile();
    }

    @AfterAll
    static void teardown() {
        findStub.close();
    }

    @Test
    void testUndo() {
        assert(UndoHandler.getUndos().isEmpty());

        UndoHandler.undo();
        assert(UndoHandler.getUndos().isEmpty());

        // insert 2 texts
        Doc.insert(0, "test");
        assert (UndoHandler.getUndos().size() == 1);
        Doc.insert(1, "t");
        assert (UndoHandler.getUndos().size() == 2);

        // undo 1 test
        UndoHandler.undo();

        assert (CustomTextPane.getText().equals("test"));

        // remove, then undo: should be same
        Doc.remove(0, 1);
        UndoHandler.undo();

        assert (CustomTextPane.getText().equals("test"));

        // apply bold
        CustomTextPane.getArea().setSelectionStart(0);
        CustomTextPane.getArea().setSelectionEnd(4);

        TypefaceHandler.applyNormalTypeface(StyleConstants.Bold);

        assert(MenuSetup.getUndoButton().getText().equals("Undo bold"));

        assert (CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Bold, true));

        // undo bold
        UndoHandler.undo();

        assert (CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Bold, false));


    }

    @Test
    void testNoSelectionUndo() {
        Doc.insert(0, "test");

        // apply bold at 1 spot
        int length = CustomTextPane.getText().length();
        CustomTextPane.getArea().setSelectionStart(length);
        CustomTextPane.getArea().setSelectionEnd(length);

        CustomTextPane.getArea().setCaretPosition(length);

        TypefaceHandler.applyNormalTypeface(StyleConstants.Bold);

        assert (CustomTextPane.extractStyle(length).containsAttribute(StyleConstants.Bold, true));

        // undo bold
        UndoHandler.undo();
        assert (CustomTextPane.extractStyle(length).containsAttribute(StyleConstants.Bold, false));

        // apply bold at one spot in middle
        CustomTextPane.getArea().setSelectionStart(1);
        CustomTextPane.getArea().setSelectionEnd(1);
        CustomTextPane.getArea().setCaretPosition(1);

        TypefaceHandler.applyNormalTypeface(StyleConstants.Bold);

        assert (CustomTextPane.extractStyle(1).containsAttribute(StyleConstants.Bold, false));

        UndoHandler.undo();
        assert (CustomTextPane.extractStyle(1).containsAttribute(StyleConstants.Bold, false));
    }


    @Test
    void testRedo() {
        assert(UndoHandler.getRedos().isEmpty());

        UndoHandler.redo();
        assert(UndoHandler.getUndos().isEmpty());

        // insert 2 texts
        Doc.insert(0, "test");
        assert (UndoHandler.getRedos().isEmpty());
        Doc.insert(1, "t");
        assert (UndoHandler.getRedos().isEmpty());

        // undo, check if redo made
        UndoHandler.undo();

        assert (UndoHandler.getRedos().size() == 1);

        // redo, check text
        UndoHandler.redo();

        assert (CustomTextPane.getText().equals("ttest"));

        assert (UndoHandler.getRedos().isEmpty());

        UndoHandler.undo();
        UndoHandler.undo();
        assert (UndoHandler.getRedos().size() == 2);

        // verify redos cleared after command
        Doc.insert(0, "test");
        assert (UndoHandler.getRedos().isEmpty());


    }

    @Test
    void testRedoAttributes() {
        // verify redos cleared after command
        Doc.insert(0, "test");
        assert (UndoHandler.getRedos().isEmpty());

        assert (CustomTextPane.getText().equals("test"));

        // select area, apply bold
        CustomTextPane.getArea().setSelectionEnd(0);
        CustomTextPane.getArea().setSelectionEnd(4);

        TypefaceHandler.applyNormalTypeface(StyleConstants.Bold);

        assert (CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Bold, true));

        // undo bold
        UndoHandler.undo();
        assert(MenuSetup.getRedoButton().getText().equals("Redo bold"));

        assert (CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Bold, false));

        // redo bold, check
        UndoHandler.redo();

        assert (CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Bold, true));
    }

    @Test
    void testCreate() {
        Doc.insert(0, "test");
        CustomTextPane.getArea().setSelectionEnd(0);
        CustomTextPane.getArea().setSelectionEnd(3);

        assert (UndoHandler.getUndos().size() == 1);

        UndoHandler.addUndo("Undo bold");

        assert (UndoHandler.getUndos().size() == 2);
        Undo undo = UndoHandler.getUndos().peek();

        assert (undo.getOldText().equals("tes"));
        assert (undo.getNewText().equals("tes"));
        assert (undo.getName().equals("Undo bold"));
        assert (undo.getStartIndex() == 0);
        assert (undo.getOldAttributes().length == 3);
        assert (undo.getOldAttributes()[0].containsAttribute(StyleConstants.Bold, false));
    }

    @Test
    void testButton() {
        UndoHandler.setUndoRedoButtonText();
        assert(MenuSetup.getUndoButton().getText().equals("Undo"));
        assert(MenuSetup.getRedoButton().getText().equals("Redo"));

        TypefaceHandler.applyNormalTypeface(StyleConstants.Bold);

        assert(MenuSetup.getUndoButton().getText().equals("Undo bold"));
        assert(MenuSetup.getRedoButton().getText().equals("Redo"));

        UndoHandler.undo();

        assert(MenuSetup.getUndoButton().getText().equals("Undo"));
        assert(MenuSetup.getRedoButton().getText().equals("Redo bold"));
    }
}
