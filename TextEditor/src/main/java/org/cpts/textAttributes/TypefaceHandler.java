package org.cpts.textAttributes;

import org.cpts.mainUI.MenuSetup;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class TypefaceHandler {
    // applies input typeface, must not depend on other typefaces
    public static void applyNormalTypeface(Object typeface) {
        AttributeHandler.applyAttributes(typeface, willApplyTypeface(typeface));
    }

    // applies subscript, removes superscript
    public static void applySubscript() {
        CustomTextPane.applyAttributes(StyleConstants.Superscript, false);
        AttributeHandler.applyAttributes(StyleConstants.Subscript, willApplyTypeface(StyleConstants.Subscript));
    }

    // applies superscript, removes subscript
    public static void applySuperscript() {
        CustomTextPane.applyAttributes(StyleConstants.Subscript, false);
        AttributeHandler.applyAttributes(StyleConstants.Superscript, willApplyTypeface(StyleConstants.Superscript));
    }

    // checks to see if typeface will all apply or will all de-apply (de-applies only when all elements have typeface)
    public static boolean willApplyTypeface(Object key) {
        // checks if no selected text
        if (CustomTextPane.getArea().getSelectedText() == null) {
            // check if carat location contains typeface
            // get style, search for value
            Style style = CustomTextPane.extractStyle(CustomTextPane.getSelectionStart());
            return !style.containsAttribute(key, Boolean.TRUE);
        }

        // check every other selected value
        for (int i = CustomTextPane.getSelectionStart(); i < CustomTextPane.getSelectionEnd(); i++) {
            // get style, search for value
            Style style = CustomTextPane.extractStyle(i);
            boolean isContained = style.containsAttribute(key, Boolean.TRUE);

            // if typeface not contained, then now know we need to apply typeface
            if (!isContained) {
                return true;
            }
        }

        return false;
    }

    // Adds highlighting to selected area or current carat location forward
    public static void applyHighlighting() {
        Color highlightColor = JColorChooser.showDialog(MenuSetup.getMainWindow(), "Choose highlight color", Color.yellow);
        if (highlightColor != null) {
            AttributeHandler.applyAttributes(StyleConstants.ColorConstants.Background, highlightColor);
        }
    }

    // Adds specified text color to selected area or from current carat location forward
    public static void applyTextColorChange() {
        Color textColor = JColorChooser.showDialog(MenuSetup.getMainWindow(), "Choose text color", Color.black);
        if (textColor != null) {
            AttributeHandler.applyAttributes(StyleConstants.ColorConstants.Foreground, textColor);
        }
    }

    // Changes the font to the selected option for selected location or from caret location forward
    public static void applyChangeFont(Object font) {
        AttributeHandler.applyAttributes(StyleConstants.FontFamily, font);
        CustomTextPane.getArea().requestFocus();
    }

    // Allows users to specify what font size they want and change it for selected text
    // or from current caret location forward
    public static void applyChangeTextSize() {
        String result = JOptionPane.showInputDialog(MenuSetup.getMainWindow(), "Please enter text size");
        try {
            if (Integer.parseInt(result) > 0) {
                AttributeHandler.applyAttributes(StyleConstants.FontSize, Integer.parseInt(result));
            } else {
                JOptionPane.showMessageDialog(MenuSetup.getMainWindow(), "Unable to change font size, unsupported number entered");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MenuSetup.getMainWindow(), "Unable to change font size, non-number value entered");
        }
    }

}
