package org.cpts.findText;

import org.cpts.mainUI.MenuSetup;
import org.cpts.textAttributes.CustomTextPane;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class FindHandler {
    private static boolean finding = false;
    private static boolean replacing = false;
    private static String searchText = "";
    private static final ArrayList<Integer> findIndices = new ArrayList<>();
    private static final ArrayList<Color> findColors = new ArrayList<>();

    // get indices list
    public static ArrayList<Integer> getIndices() {
        return findIndices;
    }

    // get colors list
    public static ArrayList<Color> getColors() {
        return findColors;
    }

    // returns if replacing
    public static boolean isReplacing() {
        return replacing;
    }

    public static boolean isFinding() { return finding;}


    //for pairwise test
    public static void setSearchText(){
        searchText = "the";
    }

    public static void Toggle(){finding = true;}



    // stops find command if finding
    public static void stopFind() {
        if (isFinding()) {
            undoFind();
        }
    }

    // starts find command if finding
    public static void startFind() {
        if (isFinding()) {
            showFind();
        }
    }

    // index of input index in list
    public static int indexAt(int index) {
        return findIndices.indexOf(index);
    }

    // apply/stop find
    public static void findCommand() {
        finding = !finding;

        // start/stop find
        if (finding) {
            // get input
            searchText = JOptionPane.showInputDialog(MenuSetup.getMainWindow(), "Enter text to find:");

            // check input
            if (searchText == null || searchText.isEmpty()) {
                finding = false;
                return;
            }

            // display found items
            showFind();
        } else {
            // hide blue background
            undoFind();
        }
    }

    // replace find string
    public static void replace() {
        // stop finding
        stopFind();

        if (!finding) {
            return;
        }

        // get replacement text
        String replaceText = JOptionPane.showInputDialog(MenuSetup.getMainWindow(), "Enter text to replace:");

        if (replaceText == null | Objects.equals(replaceText, "")) {
            return;
        }


        replacing = true;

        // get index of search
        int index = CustomTextPane.getText().indexOf(searchText);
        while (index != -1) {
            // replace text
            try {
                Style style = CustomTextPane.extractStyle(index);
                CustomTextPane.getDocument().remove(index, searchText.length());
                CustomTextPane.getDocument().insertString(index, replaceText, style);
            }
            catch (Exception ignored) {}

            // get next index
            index = CustomTextPane.getText().indexOf(searchText, index + searchText.length());
        }

        replacing = false;

        // start finding again
        startFind();
    }

    // find start indices of string
    private static ArrayList<Integer> find() {
        ArrayList<Integer> indices = new ArrayList<>();

        // find index
        int index = CustomTextPane.getText().indexOf(searchText);

        // add index, keep searching
        while (index != -1) {
            indices.add(index);
            index = CustomTextPane.getText().indexOf(searchText, index + searchText.length());
        }

        return indices;
    }

    // display find command
    private static void showFind() {
        findIndices.clear();
        findColors.clear();

        // get search text indices
        ArrayList<Integer> indices = find();

        // save old background colors
        for (int index : indices) {
            for (int i = index; i < index + searchText.length(); i++) {
                Style style = CustomTextPane.extractStyle(i);
                findIndices.add(i);
                findColors.add((Color) style.getAttribute(StyleConstants.Background));
            }
        }

        // replace background colors with blue
        for (int index : indices) {
            for (int i = index; i < index + searchText.length(); i++) {
                Style style = CustomTextPane.extractStyle(i);
                style.addAttribute(StyleConstants.Background, Color.CYAN);
                CustomTextPane.setStyle(i, style);
            }
        }
    }

    // clear find colors
    private static void undoFind() {
        // goes through all found spots
        for (int i = 0; i < findIndices.size(); i++) {
            int index = findIndices.get(i);

            // get style, replace blue with correct background
            Style style = CustomTextPane.extractStyle(index);
            style.addAttribute(StyleConstants.Background, findColors.get(i));
            CustomTextPane.setStyle(index, style);
        }
    }
}
