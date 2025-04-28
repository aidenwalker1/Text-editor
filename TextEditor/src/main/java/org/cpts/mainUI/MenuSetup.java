package org.cpts.mainUI;

import org.cpts.files.FileHandler;
import org.cpts.findText.FindHandler;
import org.cpts.tabs.TabHandler;
import org.cpts.textAttributes.AttributeHandler;
import org.cpts.textAttributes.CustomTextPane;
import org.cpts.textAttributes.TypefaceHandler;
import org.cpts.undo_redo.UndoHandler;

import javax.swing.*;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.File;

public class MenuSetup {
    private JMenu mFile;
    private JMenu editMenuButton;
    private JMenu viewButton;
    private JMenu formatButton;
    private JMenu toolsButton;

    private static JFrame mainWindow;

    private static JMenuItem undoButton;
    private static JMenuItem redoButton;

    public MenuSetup(JFrame mainWindow) {
        MenuSetup.mainWindow = mainWindow;
    }

    public static JFrame getMainWindow() {
        return mainWindow;
    }

    public static JMenuItem getUndoButton() {
        return undoButton;
    }

    public static JMenuItem getRedoButton() {
        return redoButton;
    }

    public JMenuBar createMenu()
    {
        // set up buttons/menu
        setUpFileButton();
        setUpEditButton();
        setUpFormatButton();
        setUpViewButton();
        setUpToolButton();
        return setUpMenuBar();
    }

    private void setUpToolButton() {
        toolsButton = new JMenu("Tools");

        JMenuItem wordCountButton = new JMenuItem("Word Count");
        wordCountButton.addActionListener(e-> JOptionPane.showMessageDialog(MenuSetup.mainWindow, "Words: " + CustomTextPane.getText().split(" ").length));
        toolsButton.add(wordCountButton);
    }

    private void setUpViewButton() {
        viewButton = new JMenu("View");

        JMenuItem tabButton = new JMenuItem("Add Tab");
        tabButton.addActionListener(e -> TabHandler.addPane());

        JMenuItem removeButton = new JMenuItem("Remove Tab");
        removeButton.addActionListener(e -> TabHandler.removePane());

        viewButton.add(tabButton);
        viewButton.add(removeButton);
    }

    private JMenuBar setUpMenuBar() {
        // add menu bar and add buttons to menu bar
        JMenuBar menuB = new JMenuBar();

        menuB.add(mFile);
        menuB.add(editMenuButton);
        menuB.add(viewButton);
        menuB.add(formatButton);
        menuB.add(toolsButton);
        JComboBox<String> fontButton = getFontMenu();
        menuB.add(fontButton);
        return menuB;
    }


    private void setUpFormatButton() {
        formatButton = new JMenu("Format");

        // create menu for alignment options
        JMenu alignButton = getAlignMenu();


        // set up edit buttons
        JMenuItem boldButton = new JMenuItem("Bold");
        boldButton.addActionListener(e -> TypefaceHandler.applyNormalTypeface(StyleConstants.Bold));

        JMenuItem italicButton = new JMenuItem("Italic");
        italicButton.addActionListener(e -> TypefaceHandler.applyNormalTypeface(StyleConstants.Italic));

        JMenuItem strikethroughButton = new JMenuItem("Strikethrough");
        strikethroughButton.addActionListener(e -> TypefaceHandler.applyNormalTypeface(StyleConstants.StrikeThrough));

        JMenuItem underlineButton = new JMenuItem("Underline");
        underlineButton.addActionListener(e -> TypefaceHandler.applyNormalTypeface(StyleConstants.Underline));

        JMenuItem superscriptButton = new JMenuItem("Superscript");
        superscriptButton.addActionListener(e -> TypefaceHandler.applySuperscript());

        JMenuItem subscriptButton = new JMenuItem("Subscript");
        subscriptButton.addActionListener(e -> TypefaceHandler.applySubscript());

        JMenuItem changeTextColorButton = new JMenuItem("Text Color");
        changeTextColorButton.addActionListener(e -> TypefaceHandler.applyTextColorChange());

        JMenuItem highlightButton = new JMenuItem("Highlight");
        highlightButton.addActionListener(e -> TypefaceHandler.applyHighlighting());

        JMenuItem changeTextSizeButton = new JMenuItem("Text Size");
        changeTextSizeButton.addActionListener(e -> TypefaceHandler.applyChangeTextSize());

        JMenuItem clearFormattingButton = new JMenuItem("Clear Formatting");
        clearFormattingButton.addActionListener(e -> AttributeHandler.clearFormatting());

        formatButton.add(alignButton);
        formatButton.add(boldButton);
        formatButton.add(italicButton);
        formatButton.add(underlineButton);
        formatButton.add(strikethroughButton);
        formatButton.add(subscriptButton);
        formatButton.add(superscriptButton);
        formatButton.add(changeTextColorButton);
        formatButton.add(highlightButton);
        formatButton.add(changeTextSizeButton);
        formatButton.add(clearFormattingButton);
    }

    private void setUpFileButton() {
        // set up file menu
        mFile = new JMenu("File");

        //file inner buttons, New, Save, Load
        // set up each button in the file menu
        JMenuItem newB = new JMenuItem("New");

        newB.addActionListener(e -> FileHandler.newFile());

        JMenuItem loadB = new JMenuItem("Load");
        loadB.addActionListener(e -> FileHandler.load(new JFileChooser(new File(System.getProperty("user.dir")))));

        JMenuItem saveB = new JMenuItem("Save");
        saveB.addActionListener(e -> FileHandler.save(new JFileChooser(new File(System.getProperty("user.dir")))));

        // add buttons to file menu
        mFile.add(newB);
        mFile.add(loadB);
        mFile.add(saveB);
    }

    private void setUpEditButton() {
        // set up edit menu
        editMenuButton = new JMenu("Edit");

        undoButton = new JMenuItem("Undo");
        undoButton.addActionListener(e -> UndoHandler.undo());
        redoButton = new JMenuItem("Redo");
        redoButton.addActionListener(e -> UndoHandler.redo());

        JMenuItem findButton = new JMenuItem("Find");
        findButton.addActionListener(e -> FindHandler.findCommand());

        JMenuItem replaceButton = new JMenuItem("Replace");
        replaceButton.addActionListener(e -> FindHandler.replace());

        // add all edit buttons to menu

        editMenuButton.add(undoButton);
        editMenuButton.add(redoButton);
        editMenuButton.add(findButton);
        editMenuButton.add(replaceButton);
    }

    // Add align buttons to inner menu of the edit menu
    private JMenu getAlignMenu() {
        JMenu alignButton = new JMenu("Alignment");

        JMenuItem leftAlignButton = new JMenuItem("Left Align");
        leftAlignButton.addActionListener(e -> AttributeHandler.applyAttributes(StyleConstants.Alignment, StyleConstants.ALIGN_LEFT));

        JMenuItem centerAlignButton = new JMenuItem("Center Align");
        centerAlignButton.addActionListener(e -> AttributeHandler.applyAttributes(StyleConstants.Alignment, StyleConstants.ALIGN_CENTER));

        JMenuItem rightAlignButton = new JMenuItem("Right Align");
        rightAlignButton.addActionListener(e -> AttributeHandler.applyAttributes(StyleConstants.Alignment, StyleConstants.ALIGN_RIGHT));

        alignButton.add(leftAlignButton);
        alignButton.add(centerAlignButton);
        alignButton.add(rightAlignButton);
        return alignButton;
    }

    // Inner menu of edit to allow users to choose between 5 font families to change font
    private JComboBox<String> getFontMenu() {
        JComboBox<String> fontButton = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontButton.setEnabled(true);
        fontButton.setSelectedItem("Arial");
        fontButton.addActionListener(e->TypefaceHandler.applyChangeFont(fontButton.getSelectedItem()));
        fontButton.setMaximumSize(new Dimension(fontButton.getPreferredSize().width/2, fontButton.getPreferredSize().height) );

        return fontButton;
    }
}
