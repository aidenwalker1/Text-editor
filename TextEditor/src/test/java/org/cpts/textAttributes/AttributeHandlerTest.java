package org.cpts.textAttributes;

import org.cpts.Doc;
import org.cpts.files.FileHandler;
import org.cpts.mainUI.MenuSetup;
import org.cpts.tabs.TabHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;


class AttributeHandlerTest {

    private static final Style style = new StyleContext().addStyle(null, null);

    @BeforeAll
    static void beforeAll() {

        StyleConstants.setForeground(style, Color.BLACK);
        StyleConstants.setFontFamily(style, "arial");
        StyleConstants.setItalic(style, Boolean.FALSE);
        StyleConstants.setBold(style, Boolean.FALSE);
        StyleConstants.setFontSize(style, 20);
        //StyleConstants.setLineSpacing(style, 3);
        StyleConstants.setBackground(style, Color.WHITE);
        StyleConstants.setStrikeThrough(style, Boolean.FALSE);
        StyleConstants.setSubscript(style, Boolean.FALSE);
        StyleConstants.setSuperscript(style, Boolean.FALSE);
        StyleConstants.setUnderline(style, Boolean.FALSE);
        StyleConstants.setLineSpacing(style, 0);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);

        MenuSetup m = new MenuSetup(new JFrame());
        m.createMenu();
        TabHandler.setUp();
    }


    @BeforeEach
    void reset() {
        FileHandler.newFile();
    }

    @Test
    void getDefaultStyle() {
        assert(style.isEqual(AttributeHandler.getDefaultStyle()));
    }

    @Test
    void clearFormatting() {
        assert (CustomTextPane.extractStyle(0).isEqual(AttributeHandler.getDefaultStyle()));
        AttributeHandler.clearFormatting();
        assert (CustomTextPane.extractStyle(0).isEqual(AttributeHandler.getDefaultStyle()));

        Doc.insert(0, "test");
        for (int i = 0; i < 5; i++) {
            assert (CustomTextPane.extractStyle(0).isEqual(AttributeHandler.getDefaultStyle()));
        }
        CustomTextPane.getArea().setSelectionStart(0);
        CustomTextPane.getArea().setSelectionEnd(4);

        TypefaceHandler.applyNormalTypeface(StyleConstants.Bold);

        assert(MenuSetup.getUndoButton().getText().equals("Undo bold"));

        Boolean actual = CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Bold, true);

        assertEquals(true, actual);

        CustomTextPane.getArea().setSelectionStart(0);
        CustomTextPane.getArea().setSelectionEnd(1);

        AttributeHandler.clearFormatting();

        for(int i=0; i<1; i++)
        {
            assertTrue(CustomTextPane.extractStyle(i).isEqual(AttributeHandler.getDefaultStyle()));
        }

    }
}