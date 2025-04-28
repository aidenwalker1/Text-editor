package org.cpts.mainUI;

import org.cpts.Main;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

class MainWindowTest {
    @Test
    void MainWindowTest()
    {
        MainWindow main = new MainWindow();
        assertEquals(500, main.getSize().height);
        assertEquals(500, main.getSize().width);
        assertTrue(main.isVisible());
    }
}