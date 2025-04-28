package org.cpts.FindHandler;

import org.cpts.Doc;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mockStatic;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FindHandlerTest {



    @Mock
    private static MockedStatic<JOptionPane> mockedOption;

    @BeforeAll
    public static void fixit() {
        org.cpts.mainUI.MenuSetup m = new org.cpts.mainUI.MenuSetup(new JFrame());
        m.createMenu();
        org.cpts.tabs.TabHandler.setUp();
    }

    @BeforeEach
    public void SetUp() {
        mockedOption = mockStatic(JOptionPane.class);
        org.cpts.files.FileHandler.newFile();
        Doc.insert(0, "the the the the");

    }

    @AfterAll
    public static void TearDown() {
        org.cpts.files.FileHandler.newFile();
        org.cpts.findText.FindHandler.findCommand();

    }

    @AfterEach
    public void closeMocks()
    {
        mockedOption.close();
    }

    @Test
    public void ReplaceTest()
    {
        mockedOption.when(()->JOptionPane.showInputDialog(org.cpts.mainUI.MenuSetup.getMainWindow(),
                "Enter text to find:")).thenReturn("the");

        org.cpts.findText.FindHandler.findCommand();

        mockedOption.when(()->JOptionPane.showInputDialog(org.cpts.mainUI.MenuSetup.getMainWindow(),
                "Enter text to replace:")).thenReturn("dog");

        org.cpts.findText.FindHandler.replace();

        String actualText = org.cpts.textAttributes.CustomTextPane.getText();

        assertEquals("dog dog dog dog", actualText);


    }

    @Test
    public void emptySearch()
    {
        mockedOption.when(()->JOptionPane.showInputDialog(org.cpts.mainUI.MenuSetup.getMainWindow(),
                "Enter text to find:")).thenReturn("");

        org.cpts.findText.FindHandler.findCommand();

        assertFalse(org.cpts.findText.FindHandler.isFinding());


    }

    @Test
    public void nullSearch()
    {
        mockedOption.when(()->JOptionPane.showInputDialog(org.cpts.mainUI.MenuSetup.getMainWindow(),
                "Enter text to find:")).thenReturn(null);

        org.cpts.findText.FindHandler.findCommand();

        assertFalse(org.cpts.findText.FindHandler.isFinding());
    }

    @Test
    public void EmptyReplace()
    {
        mockedOption.when(()->JOptionPane.showInputDialog(org.cpts.mainUI.MenuSetup.getMainWindow(),
                "Enter text to find:")).thenReturn("the");

        org.cpts.findText.FindHandler.findCommand();

        mockedOption.when(()->JOptionPane.showInputDialog(org.cpts.mainUI.MenuSetup.getMainWindow(),
                "Enter text to replace:")).thenReturn("");

        org.cpts.findText.FindHandler.replace();

        String actualText = org.cpts.textAttributes.CustomTextPane.getText();

        assertEquals("the the the the", actualText);




    }

    @Test
    public void nullReplace()
    {
        mockedOption.when(()->JOptionPane.showInputDialog(org.cpts.mainUI.MenuSetup.getMainWindow(),
                "Enter text to find:")).thenReturn("the");

        org.cpts.findText.FindHandler.findCommand();

        mockedOption.when(()->JOptionPane.showInputDialog(org.cpts.mainUI.MenuSetup.getMainWindow(),
                "Enter text to replace:")).thenReturn(null);

        org.cpts.findText.FindHandler.replace();

        String actualText = org.cpts.textAttributes.CustomTextPane.getText();

        assertEquals("the the the the", actualText);




    }

}
