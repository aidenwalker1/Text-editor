package org.cpts.tabs;

import org.cpts.files.FileHandler;
import org.cpts.textAttributes.AttributeHandler;
import org.cpts.textAttributes.CustomTextPane;
import org.cpts.textAttributes.TypefaceHandler;
import org.cpts.findText.FindHandler;
import org.cpts.undo_redo.Undo;
import org.cpts.undo_redo.UndoFilter;
import org.cpts.undo_redo.UndoHandler;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.StyleConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Stack;

public class TabHandler {
    private static final ArrayList<JTextPane> allPanes = new ArrayList<>();
    private static final ArrayList<Stack<Undo>> allUndos = new ArrayList<>();
    private static final ArrayList<Stack<Undo>> allRedos = new ArrayList<>();
    private static final JTabbedPane pane = new JTabbedPane();

    // adds first pane
    public static void setUp() {
        pane.addChangeListener(e -> changeTab());
        pane.setName("Tabbed Pane");
        addPane();
    }

    // gets current pane
    public static JTabbedPane getPane() {
        return pane;
    }

    // changes tab
    public static void changeTab() {
        // stop find
        FindHandler.stopFind();

        // get selected pane data
        CustomTextPane.setArea(allPanes.get(pane.getSelectedIndex()));
        UndoHandler.setUndos(allUndos.get(pane.getSelectedIndex()));
        UndoHandler.setRedos(allRedos.get(pane.getSelectedIndex()));

        // set button text
        UndoHandler.setUndoRedoButtonText();

        // find again if finding
        FindHandler.startFind();
    }

    public static void resetUndo() {
        allUndos.set(pane.getSelectedIndex(), new Stack<>());
        allRedos.set(pane.getSelectedIndex(), new Stack<>());

        UndoHandler.setRedos(allRedos.get(pane.getSelectedIndex()));
        UndoHandler.setUndos(allUndos.get(pane.getSelectedIndex()));
    }

    public static void changeTabTitle(String name, boolean isNew)
    {
        int ind = pane.getSelectedIndex();
        if (isNew)
        {

            pane.setTitleAt(ind, name + ind);
            return;
        }
        pane.setTitleAt(ind, name);
    }

    // add undo filter
    private static void setUpDocumentFilter() {
        ((AbstractDocument) CustomTextPane.getArea().getDocument()).setDocumentFilter(new UndoFilter());
    }

    // remove last pane
    public static void removePane() {
        // new file if only 1 pane
        if (pane.getTabCount() == 1) {
            FileHandler.newFile();
            return;
        }

        // if at the last pane, go to one before
        if (pane.getTabCount() == pane.getSelectedIndex() + 1) {
            CustomTextPane.setArea(allPanes.get(allPanes.size() - 2));
            UndoHandler.setUndos(allUndos.get(allUndos.size() - 2));
            UndoHandler.setRedos(allRedos.get(allRedos.size() - 2));
        }

        // remove last pane
        pane.remove(pane.getTabCount() - 1);
        allPanes.remove(allPanes.size() - 1);
        allUndos.remove(allUndos.size() - 1);
        allRedos.remove(allRedos.size() - 1);
    }

    // add new pane at end
    public static void addPane() {
        // add new tab
        allPanes.add(new JTextPane());
        allRedos.add(new Stack<>());
        allUndos.add(new Stack<>());

        allPanes.get(allPanes.size() - 1).setName("Panel " + allPanes.size());

        // get new tab, set it up
        CustomTextPane.setArea(allPanes.get(allPanes.size() - 1));
        UndoHandler.setUndos(allUndos.get(allUndos.size() - 1));
        UndoHandler.setRedos(allRedos.get(allRedos.size() - 1));
        setUpDocumentFilter();
        CustomTextPane.getArea().setCharacterAttributes(AttributeHandler.getDefaultStyle(), true);
        CustomTextPane.setStyle(0, AttributeHandler.getDefaultStyle());

        // add keyboard commands
        CustomTextPane.getArea().addKeyListener(new CommandListener());
        // add the tab, set focus to it
        pane.addTab("Document " + pane.getTabCount(), new JPanel());

        // add area to scroll pane
        JScrollPane scrollArea = new JScrollPane(CustomTextPane.getArea());
        scrollArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setComponentAt(pane.getTabCount() - 1, CustomTextPane.getArea());

        changeTab();
        CustomTextPane.getArea().requestFocus();
    }

}

