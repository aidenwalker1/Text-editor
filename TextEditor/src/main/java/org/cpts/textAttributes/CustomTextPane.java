package org.cpts.textAttributes;

import org.cpts.findText.FindHandler;
import org.cpts.undo_redo.UndoHandler;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.Stack;

public class CustomTextPane {
    private static JTextPane area;

    public static void setArea(JTextPane area) {
        CustomTextPane.area = area;
    }

    public static JTextPane getArea() {
        return area;
    }

    public static StyledDocument getDocument() {
        return area.getStyledDocument();
    }

    public static String getSelectedText() {
        return area.getSelectedText();
    }

    // gets selection start
    public static int getSelectionStart() {
        return area.getSelectionStart();
    }

    // gets selection end
    public static int getSelectionEnd() {
        return area.getSelectionEnd();
    }

    // gets text
    public static String getText() {
        return area.getText();
    }


    // extracts the style of a character at specified index
    public static Style extractStyle(int index) {
        // get attributes from character
        AttributeSet characterAttributes = getDocument().getCharacterElement(index).getAttributes();
        AttributeSet paragraphAttributes = getDocument().getParagraphElement(index).getAttributes();

        // create style object
        StyleContext context = new StyleContext();
        Style style = context.addStyle(null, null);

        // add attributes to style
        style.addAttributes(characterAttributes);
        style.addAttribute(StyleConstants.Alignment, paragraphAttributes.getAttribute(StyleConstants.Alignment));
        style.addAttribute(StyleConstants.LineSpacing, paragraphAttributes.getAttribute(StyleConstants.LineSpacing));

        return style;
    }

    // applies style to specified character
    public static void setStyle(int index, AttributeSet style) {
        getDocument().setParagraphAttributes(index, 1, style, true);
        getDocument().setCharacterAttributes(index, 1, style, true);
    }

    // applies specified attribute type and value to selected text
    public static void applyAttributes(Object attributeName, Object value) {
        // apply attribute to all selected text
        for (int i = getSelectionStart(); i < getSelectionEnd(); i++) {
            // get style of character
            Style style = extractStyle(i);

            if (FindHandler.getIndices().contains(i) && attributeName.equals(StyleConstants.Background)) {
                int idx = FindHandler.getIndices().indexOf(i);
                FindHandler.getColors().set(idx, (Color) value);
            }

            // change specified style component to specified value
            style.addAttribute(attributeName, value);

            // set character style
            setStyle(i, style);
        }

        // apply attribute to current carat location
        if (getSelectedText() == null) {
            applyEmptyStyle(attributeName, value);
        }
    }

    // applies style to no selection
    private static void applyEmptyStyle(Object attributeName, Object value) {
        // get style of carat
        Style style = extractStyle(getSelectionStart()-1);

        // change specified style component to specified value
        style.addAttribute(attributeName, value);

        // save old attribute
        area.setParagraphAttributes(style, true);
        area.setCharacterAttributes(style, true);

        // apply attribute to end character if at end
        if (getSelectionStart() == getText().length()) {
            setStyle(area.getSelectionStart(), style);
        }
    }
}
