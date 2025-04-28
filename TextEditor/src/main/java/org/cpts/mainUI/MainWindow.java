package org.cpts.mainUI;
import org.cpts.tabs.TabHandler;
import org.cpts.textAttributes.CustomTextPane;

import javax.swing.*;

public class MainWindow extends JFrame {
    MenuSetup menu = new MenuSetup(this);

    public MainWindow() {
        setJMenuBar(menu.createMenu());

        // set up text pane
        TabHandler.setUp();
        add(TabHandler.getPane());

        // set frame settings
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CustomTextPane.getArea().requestFocus();
    }
}