package org.cpts.textAttributes;

import org.cpts.Doc;
import org.cpts.files.FileHandler;
import org.cpts.findText.FindHandler;
import org.cpts.mainUI.MenuSetup;
import org.cpts.tabs.TabHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class CustomTextPaneTest {

    private MockedStatic<JOptionPane> mockedOption;

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
    }


    @Test
    void setStyle() {
        CustomTextPane.applyAttributes(StyleConstants.StrikeThrough, true);
        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.StrikeThrough, true));
        Style style = CustomTextPane.extractStyle(0);
        style.addAttribute(StyleConstants.Underline, true);
        CustomTextPane.setStyle(0, style);

        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.StrikeThrough, true));
        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Underline, true));
    }

    @Test
    void applyAttributes_falseEmptyStyle() {
        Doc.insert(0,"test");

        CustomTextPane.applyAttributes(StyleConstants.Bold, true);

        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Bold, false));
    }

    @Test
    void applyAttributes_backgroundColorAndFindIndex() {
        Doc.insert(0, "test");

        assertTrue(CustomTextPane.extractStyle(0).containsAttribute(StyleConstants.Background, Color.WHITE));


        mockedOption.when(()->JOptionPane.showInputDialog(MenuSetup.getMainWindow(),
               "Enter text to find:")).thenReturn("e");

        FindHandler.findCommand();

        assertTrue(CustomTextPane.extractStyle(1).containsAttribute(StyleConstants.Background, Color.CYAN));

        CustomTextPane.getArea().setSelectionStart(0);
        CustomTextPane.getArea().setSelectionEnd(4);
        CustomTextPane.applyAttributes(StyleConstants.Background, Color.YELLOW);
        assertTrue(FindHandler.getColors().contains(Color.YELLOW));
        FindHandler.stopFind();
        for(int i=0; i<4; i++)
        {
            assertTrue(CustomTextPane.extractStyle(i).containsAttribute(StyleConstants.Background, Color.YELLOW));
        }

    }


    @AfterEach
    void tearDown() {
        mockedOption.close();
    }
}