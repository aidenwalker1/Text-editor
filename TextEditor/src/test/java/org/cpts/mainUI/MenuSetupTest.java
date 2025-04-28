package org.cpts.mainUI;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MenuSetupTest {

    MainWindow mainWindow = new MainWindow();

    @Test
    void createMenu() {

        MenuSetup menu = new MenuSetup(mainWindow);

        JMenuBar menuBar = menu.createMenu();

        assertEquals(6, menuBar.getComponentCount());
    }
}