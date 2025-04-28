package org.cpts.integration;

import io.qameta.allure.TmsLink;
import org.cpts.Doc;
import org.cpts.files.FileHandler;
import org.cpts.findText.FindHandler;
import org.cpts.mainUI.MenuSetup;
import org.cpts.tabs.TabHandler;
import org.cpts.textAttributes.CustomTextPane;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class StartShowFindIntegrationTest {
    @BeforeAll
    static void setUp() {
        MenuSetup m = new MenuSetup(new JFrame());
        m.createMenu();
        TabHandler.setUp();

        FindHandler.setSearchText();
        FindHandler.Toggle();
    }

    @AfterAll
    static void tearDown(){

        org.cpts.files.FileHandler.newFile();
        org.cpts.findText.FindHandler.findCommand();

    }

    @TmsLink("C21")
    @Test
    void StartShowTest(){
        Doc.insert(0, "the the the");


        FindHandler.startFind();

        ArrayList<Integer> result = FindHandler.getIndices();

        //confirm Indices of 'the' occurrences populated
        assertEquals(result, Arrays.asList(0, 1, 2, 4, 5, 6, 8, 9, 10));


        Style blueResult = CustomTextPane.extractStyle(0);

        //confirm background change by ShowFind
        assertEquals(blueResult.getAttribute(StyleConstants.Background), Color.CYAN);



    }
}
