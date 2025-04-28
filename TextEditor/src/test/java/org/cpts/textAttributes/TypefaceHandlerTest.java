package org.cpts.textAttributes;

import org.cpts.Doc;
import org.cpts.files.FileHandler;
import org.cpts.mainUI.MenuSetup;
import org.cpts.tabs.TabHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import static org.mockito.Mockito.*;

class TypefaceHandlerTest {

    private MockedStatic<JOptionPane> mockedOption;

    private MockedStatic<JColorChooser> mockedColor;

    @BeforeAll
    static void SetUp(){
        MenuSetup m = new MenuSetup(new JFrame());
        m.createMenu();
        TabHandler.setUp();
    }


    @BeforeEach
    void reset() {
        FileHandler.newFile();

        mockedOption = mockStatic(JOptionPane.class);
        mockedColor = mockStatic(JColorChooser.class);
    }

    @Test
    void applyNormalTypeface() {

    }

    @Test
    void applySubscript() {
        assert (CustomTextPane.extractStyle(0).isEqual(AttributeHandler.getDefaultStyle()));

        TypefaceHandler.applySubscript();

        assert(MenuSetup.getUndoButton().getText().equals("Undo subscript"));

        Boolean actual = CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Subscript, true);

        assertEquals(true, actual);
        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Superscript, false));


    }

    @Test
    void applySuperscript() {
        assert (CustomTextPane.extractStyle(0).isEqual(AttributeHandler.getDefaultStyle()));

        TypefaceHandler.applySuperscript();

        assert(MenuSetup.getUndoButton().getText().equals("Undo superscript"));

        Boolean actual = CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Superscript, true);

        assertEquals(true, actual);
        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Subscript, false));

    }

    @Test
    void willApplyTypeface_alreadyContained() {
        Doc.insert(0, "test");
        for (int i = 0; i < 5; i++) {
            assert (CustomTextPane.extractStyle(0).isEqual(AttributeHandler.getDefaultStyle()));
        }
        CustomTextPane.getArea().setSelectionStart(0);
        CustomTextPane.getArea().setSelectionEnd(4);

        TypefaceHandler.applyNormalTypeface(StyleConstants.Bold);
        assert(MenuSetup.getUndoButton().getText().equals("Undo bold"));
        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Bold, true));

        TypefaceHandler.willApplyTypeface(StyleConstants.Bold);

        for(int i=0; i<4; i++)
        {
            assertTrue(CustomTextPane.extractStyle(i).containsAttribute(StyleConstants.Bold, true));
        }

    }

    @Test
    void applyHighlighting_valid() {
        mockedColor.when(()->JColorChooser.showDialog(MenuSetup.getMainWindow(),
                "Choose highlight color", Color.yellow)).thenReturn(Color.BLUE);

        TypefaceHandler.applyHighlighting();

        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Background, Color.BLUE));

    }

    @Test
    void applyHighlighting_invalid() {
        mockedColor.when(()->JColorChooser.showDialog(MenuSetup.getMainWindow(),
                "Choose highlight color", Color.yellow)).thenReturn(null);

        TypefaceHandler.applyHighlighting();

        assert (CustomTextPane.extractStyle(0).isEqual(AttributeHandler.getDefaultStyle()));

    }

    @Test
    void applyTextColorChange_valid() {
        mockedColor.when(()->JColorChooser.showDialog(MenuSetup.getMainWindow(),
                "Choose text color", Color.black)).thenReturn(Color.GREEN);

        TypefaceHandler.applyTextColorChange();

        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Foreground, Color.GREEN));
    }

    @Test
    void applyTextColorChange_invalid() {
        mockedColor.when(()->JColorChooser.showDialog(MenuSetup.getMainWindow(),
                "Choose text color", Color.black)).thenReturn(null);

        TypefaceHandler.applyTextColorChange();

        assert (CustomTextPane.extractStyle(0).isEqual(AttributeHandler.getDefaultStyle()));
    }

    @Test
    void applyChangeFont() {
        assert (CustomTextPane.extractStyle(0).isEqual(AttributeHandler.getDefaultStyle()));

        TypefaceHandler.applyChangeFont("Times New Roman");

        assert(MenuSetup.getUndoButton().getText().equals("Undo family"));

        Boolean actual = CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.FontFamily, "Times New Roman");

        assertEquals(true, actual);

    }

    @Test
    void applyChangeTextSize_Valid() {
        mockedOption.when(() -> JOptionPane.showInputDialog(MenuSetup.getMainWindow(),
                "Please enter text size")).thenReturn("12");

        assert (CustomTextPane.extractStyle(0).isEqual(AttributeHandler.getDefaultStyle()));
        TypefaceHandler.applyChangeTextSize();
        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.FontSize, 12));
    }

    @Test
    void applyChangeTextSize_invalid() {
        mockedOption.when(() -> JOptionPane.showInputDialog(MenuSetup.getMainWindow(),
                "Please enter text size")).thenReturn("-12");

        assert (CustomTextPane.extractStyle(0).isEqual(AttributeHandler.getDefaultStyle()));
        TypefaceHandler.applyChangeTextSize();
        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.FontSize, 20));
    }

    @Test
    void applyChangeTextSize_notANumber() {
        mockedOption.when(() -> JOptionPane.showInputDialog(MenuSetup.getMainWindow(),
                "Please enter text size")).thenReturn("abc");

        assert (CustomTextPane.extractStyle(0).isEqual(AttributeHandler.getDefaultStyle()));
        TypefaceHandler.applyChangeTextSize();
        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.FontSize, 20));
    }

    @AfterEach
    void tearDown() {
        mockedOption.close();
        mockedColor.close();
    }
}