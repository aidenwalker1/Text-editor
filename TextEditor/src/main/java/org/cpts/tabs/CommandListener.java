package org.cpts.tabs;

import org.cpts.files.FileHandler;
import org.cpts.findText.FindHandler;
import org.cpts.textAttributes.TypefaceHandler;
import org.cpts.undo_redo.UndoHandler;

import javax.swing.text.StyleConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CommandListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (e.isControlDown()) {
            switch (e.getKeyCode()) {
                case 44 -> TypefaceHandler.applySubscript();
                case 46 -> TypefaceHandler.applySuperscript();
                case 66 -> TypefaceHandler.applyNormalTypeface(StyleConstants.Bold);
                case 73 -> TypefaceHandler.applyNormalTypeface(StyleConstants.Italic);
                case 85 -> TypefaceHandler.applyNormalTypeface(StyleConstants.Underline);
                case 89 -> UndoHandler.redo();
                case 90 -> UndoHandler.undo();
            }
        }
    }
}
