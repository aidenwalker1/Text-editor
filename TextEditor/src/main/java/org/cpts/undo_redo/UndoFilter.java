package org.cpts.undo_redo;

import org.cpts.findText.FindHandler;

import javax.swing.text.*;

public class UndoFilter extends DocumentFilter {
    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        // checks if user inserted string or replaced a string
        boolean inserted = offset >= fb.getDocument().getLength();
        // checks that not undoing or redoing
        if (canChange()) {
            FindHandler.stopFind();
            // clear redo stack after action
            UndoHandler.getRedos().clear();

            // get old text
            String old = fb.getDocument().getText(offset, length);

            String name = inserted ? "Undo insert " + text : "Undo replace " + old;

            // create new undo
            Undo undo = new Undo(offset, old, text, name);
            UndoHandler.getUndos().push(undo);

            UndoHandler.setUndoRedoButtonText();
        }

        int index = FindHandler.indexAt(offset);

        if (index != -1) {
            StyleContext sc = new StyleContext();
            Style s = sc.addStyle(null, null);
            s.addAttributes(attrs);
            s.addAttribute(StyleConstants.Background, FindHandler.getColors().get(index));
            super.replace(fb, offset, length, text, s.copyAttributes());
        } else {
            super.replace(fb, offset, length, text, attrs);
        }

        if (canChange()) {
            FindHandler.startFind();
        }
    }

    @Override
    public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
        if (canChange()) {
            FindHandler.stopFind();
            UndoHandler.getRedos().clear();
            String oldText = fb.getDocument().getText(offset, length);

            Undo undo = new Undo(offset, oldText,"", "Undo remove " + oldText);
            UndoHandler.getUndos().push(undo);
            UndoHandler.setUndoRedoButtonText();
        }

        super.remove(fb, offset, length);

        if (canChange()) {
            FindHandler.startFind();
        }
    }

    private boolean canChange() {
        return !UndoHandler.isRedoing() && !UndoHandler.isUndoing() && !FindHandler.isReplacing();
    }
}


