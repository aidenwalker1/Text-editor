package org.cpts.undo_redo;
import org.cpts.textAttributes.CustomTextPane;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.Style;

public class Undo {
    private String name;
    private final String oldText;
    private final String newText;
    private final int startIndex;
    private AttributeSet[] oldAttributes;

    public Undo(int startIndex, String oldText, String newText, String name) {
        this.name = name;
        this.oldText = oldText;
        this.newText = newText;
        this.startIndex = startIndex;
        this.setAttributes();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldText() {
        return oldText;
    }

    public String getNewText() {
        return newText;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public AttributeSet[] getOldAttributes() {
        return oldAttributes;
    }

    // set old attributes based on text length and offset
    private void setAttributes() {
        int length = oldText == null ? 0 : oldText.length();
        if (length == 0) {
            this.oldAttributes = new Style[1];
        }
        else {
            this.oldAttributes = new Style[length];
        }

        for (int i = 0; i < this.oldAttributes.length; i++) {
            this.oldAttributes[i] = CustomTextPane.extractStyle(i+startIndex).copyAttributes();
        }
    }
}
