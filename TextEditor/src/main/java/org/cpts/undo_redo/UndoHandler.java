package org.cpts.undo_redo;

import org.cpts.findText.FindHandler;
import org.cpts.mainUI.MenuSetup;
import org.cpts.textAttributes.CustomTextPane;

import java.util.Stack;

public class UndoHandler {
    private static Stack<Undo> undos = new Stack<>();
    private static Stack<Undo> redos = new Stack<>();

    private static boolean redoing = false;
    private static boolean undoing = false;

    // is redoing
    public static boolean isRedoing() {
        return redoing;
    }

    // is undoing
    public static boolean isUndoing() {
        return undoing;
    }

    // set redos
    public static Stack<Undo> getUndos() {
        return undos;
    }

    // set redos
    public static Stack<Undo> getRedos() {
        return redos;
    }

    // set redos
    public static void setRedos(Stack<Undo> curRedos) {
        redos = curRedos;
    }

    // set undos
    public static void setUndos(Stack<Undo> curUndos) {
        undos = curUndos;
    }

    // undo command
    public static void undo() {
        // check if no undos
        if (undos.isEmpty()) {
            return;
        }

        // don't allow filter updates
        undoing = true;

        FindHandler.stopFind();

        // get top undo, change name to redo
        Undo cur = undos.pop();
        cur.setName(cur.getName().replace("Undo", "Redo"));

        // apply undo, get next redo
        Undo next = applyUndoRedo(cur);
        redos.push(next);

        FindHandler.startFind();

        // allow filter updates
        undoing = false;

        setUndoRedoButtonText();
    }

    // redo command
    public static void redo() {
        if (redos.isEmpty()) {
            return;
        }

        FindHandler.stopFind();

        redoing = true;

        // get top redo, change name to undo
        Undo cur = redos.pop();
        cur.setName(cur.getName().replace("Redo", "Undo"));

        // apply redo, get next undo
        Undo next = applyUndoRedo(cur);
        undos.push(next);

        FindHandler.startFind();

        // allow filter updates
        redoing = false;

        setUndoRedoButtonText();
    }

    // add undo item
    public static void addUndo(String name) {
        Undo undo = new Undo(CustomTextPane.getSelectionStart(), CustomTextPane.getSelectedText(), CustomTextPane.getSelectedText(), name);
        MenuSetup.getUndoButton().setText(undo.getName());
        undos.push(undo);
        setUndoRedoButtonText();
    }

    // applies undo/redo
    private static Undo applyUndoRedo(Undo cur) {
        // creates next undo/redo
        Undo next = new Undo(cur.getStartIndex(), cur.getNewText(), cur.getOldText(), cur.getName());

        // if no text
        if (cur.getOldText() == null) {
            CustomTextPane.setStyle(cur.getStartIndex(),cur.getOldAttributes()[0]);
            CustomTextPane.getArea().setCharacterAttributes(cur.getOldAttributes()[0], true);
            CustomTextPane.getArea().setParagraphAttributes(cur.getOldAttributes()[0], true);

            // apply attribute to end character if at end
            if (cur.getStartIndex() == CustomTextPane.getText().length()) {
                CustomTextPane.setStyle(cur.getStartIndex(),cur.getOldAttributes()[0]);
            }
            return next;
        }

        // remove current text
        try {
            CustomTextPane.getDocument().remove(cur.getStartIndex(), cur.getNewText().length());
        }
        catch (Exception ignored) {}

        // add new text
        for (int i = 0; i < cur.getOldText().length(); i++) {
            try {
                CustomTextPane.getDocument().insertString(cur.getStartIndex()+i, cur.getOldText().charAt(i)+"", cur.getOldAttributes()[i]);
                CustomTextPane.setStyle(cur.getStartIndex()+i, cur.getOldAttributes()[i]);
            }
            catch (Exception ignored) {}
        }

        return next;
    }

    // update button text
    public static void setUndoRedoButtonText() {
        // set redo button
        if (redos.isEmpty()) {
            MenuSetup.getRedoButton().setText("Redo");
        } else {
            MenuSetup.getRedoButton().setText(redos.peek().getName());
        }

        // set undo button
        if (undos.isEmpty()) {
            MenuSetup.getUndoButton().setText("Undo");
        } else {
            MenuSetup.getUndoButton().setText(undos.peek().getName());
        }
    }
}
