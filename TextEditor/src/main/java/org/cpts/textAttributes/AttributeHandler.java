package org.cpts.textAttributes;

import org.cpts.findText.FindHandler;
import org.cpts.undo_redo.UndoHandler;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;

public class AttributeHandler {
    private static final Style defaultStyle = createDefaultStyle();

    public static Style getDefaultStyle() {
        return defaultStyle;
    }

    // apply attributes while saving undo
    public static void applyAttributes(Object key, Object value) {
        FindHandler.stopFind();
        UndoHandler.getRedos().clear();
        UndoHandler.addUndo("Undo " + key.toString());
        CustomTextPane.applyAttributes(key, value);
        FindHandler.startFind();
        CustomTextPane.getArea().requestFocus();
    }

    // clears formatting of selected text
    public static void clearFormatting() {
        UndoHandler.addUndo("Undo Clear Formatting");

        // applies default attribute to all selected text
        for(int i=CustomTextPane.getSelectionStart(); i<CustomTextPane.getSelectionEnd(); i++) {
            CustomTextPane.setStyle(i, AttributeHandler.defaultStyle);
        }

        // apply default attribute to current carat location
        if (CustomTextPane.getSelectedText() == null) {
            CustomTextPane.getArea().setCharacterAttributes(AttributeHandler.defaultStyle, true);
            CustomTextPane.getArea().setParagraphAttributes(AttributeHandler.defaultStyle, true);
        }

        // apply default attribute to end character if at end
        if (CustomTextPane.getSelectionStart() == CustomTextPane.getText().length()) {
            CustomTextPane.setStyle(CustomTextPane.getSelectionStart(),AttributeHandler.defaultStyle);
        }
    }

    // builds default style
    private static Style createDefaultStyle() {
        StyleContext context = new StyleContext();
        Style defaultStyle = context.addStyle(null, null);

        // add all default fields
        StyleConstants.setForeground(defaultStyle, Color.BLACK);
        StyleConstants.setFontFamily(defaultStyle, "arial");
        StyleConstants.setItalic(defaultStyle, Boolean.FALSE);
        StyleConstants.setBold(defaultStyle, Boolean.FALSE);
        StyleConstants.setFontSize(defaultStyle, 20);
        //StyleConstants.setLineSpacing(defaultStyle, 3);
        StyleConstants.setBackground(defaultStyle, Color.WHITE);
        StyleConstants.setStrikeThrough(defaultStyle, Boolean.FALSE);
        StyleConstants.setSubscript(defaultStyle, Boolean.FALSE);
        StyleConstants.setSuperscript(defaultStyle, Boolean.FALSE);
        StyleConstants.setUnderline(defaultStyle, Boolean.FALSE);
        StyleConstants.setLineSpacing(defaultStyle, 0);
        StyleConstants.setAlignment(defaultStyle, StyleConstants.ALIGN_LEFT);

        return defaultStyle;
    }
}
