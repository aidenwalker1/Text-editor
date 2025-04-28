package org.cpts;

import org.cpts.textAttributes.CustomTextPane;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Doc {
    public static void insert(int index, String text) {
        AttributeSet attrs = CustomTextPane.extractStyle(index);
        try {
            ((AbstractDocument) CustomTextPane.getArea().getDocument()).replace(index, 0, text, attrs);
        }
        catch (Exception e) {
            assert (false);
        }
    }

    public static void replace(int index, int length, String text) {
        AttributeSet attrs = CustomTextPane.extractStyle(index);
        try {
            ((AbstractDocument) CustomTextPane.getArea().getDocument()).replace(index, length, text, attrs);
        }
        catch (Exception e) {
            assert (false);
        }
    }

    public static void remove(int index, int length) {
        try {
            CustomTextPane.getArea().getDocument().remove(index, length);
        }
        catch (Exception e) {
            assert (false);
        }
    }

    public static void selectText(int start, int end) {
        CustomTextPane.getArea().setSelectionStart(start);
        CustomTextPane.getArea().setSelectionEnd(end);
    }

    public static void assertContainsAttribute(int index, Object name) {
        assertTrue(CustomTextPane.extractStyle(index).containsAttribute(name, true));
    }

    public static void assertContainsAttribute(int index, Object name, Object val) {
        assertTrue(CustomTextPane.extractStyle(index).containsAttribute(name, val));
    }

    public static void assertDoesNotContainsAttribute(int index, Object name) {
        assertTrue(CustomTextPane.extractStyle(index).containsAttribute(name, false));
    }
}
