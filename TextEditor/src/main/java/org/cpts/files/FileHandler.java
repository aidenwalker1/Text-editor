package org.cpts.files;

import org.cpts.mainUI.MenuSetup;
import org.cpts.tabs.TabHandler;
import org.cpts.textAttributes.AttributeHandler;
import org.cpts.textAttributes.CustomTextPane;
import org.cpts.undo_redo.UndoHandler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.*;
import java.util.Arrays;
import java.util.Stack;

import static org.cpts.tabs.TabHandler.changeTabTitle;

public class FileHandler {
    public static void save(JFileChooser savemenu) {
        String ext = "";

        FileNameExtensionFilter fRtf = new FileNameExtensionFilter("RTF Files", "rtf");
        FileNameExtensionFilter fText = new FileNameExtensionFilter("Text Files", "txt");
        savemenu.setFileFilter(fRtf); //set rtf and tft filter
        savemenu.setFileFilter(fText);

        int choice = savemenu.showSaveDialog(null);


        if (choice == JFileChooser.APPROVE_OPTION) {
            RTFEditorKit rtfKit = new RTFEditorKit();

            String fname = savemenu.getSelectedFile().getName();


            if (fname.length() >= 4) {
                ext = fname.substring(fname.length() - 4); //grab file extension
            }

            try {
                if(ext.equals(".rtf")) //rtf case
                {
                    FileOutputStream fos = new FileOutputStream(savemenu.getSelectedFile());
                    rtfKit.write(fos, CustomTextPane.getDocument(), 0, CustomTextPane.getDocument().getLength());
                    fos.close();



                }
                else if (ext.equals(".txt")) //txt case
                {
                    FileWriter writer = new FileWriter(savemenu.getSelectedFile());
                    writer.write(CustomTextPane.getText());
                    writer.close();
                }
                else if(Arrays.equals(((FileNameExtensionFilter) savemenu.getFileFilter()).getExtensions(), fRtf.getExtensions()))
                {
                    FileOutputStream fos = new FileOutputStream(savemenu.getSelectedFile() + ".rtf");
                    rtfKit.write(fos, CustomTextPane.getDocument(), 0, CustomTextPane.getDocument().getLength());
                    fos.close();
                }
                else
                {
                    FileWriter writer = new FileWriter(savemenu.getSelectedFile() + ".txt");
                    writer.write(CustomTextPane.getText());
                    writer.close();
                }


                changeTabTitle(savemenu.getSelectedFile().getName(), false);

            } catch (Exception ignored) {}
        }
    }

    public static void load(JFileChooser loadmenu) {
        String ext;

        FileNameExtensionFilter frtf = new FileNameExtensionFilter("RTF Files", "rtf");
        FileNameExtensionFilter ftext = new FileNameExtensionFilter("Text Files", "txt");
        loadmenu.setFileFilter(frtf); //set rtf and tft filter
        loadmenu.setFileFilter(ftext);

        int choice = loadmenu.showOpenDialog(null);

        if (choice == JFileChooser.APPROVE_OPTION) {

            File loadfile = loadmenu.getSelectedFile();

            String fname = loadmenu.getSelectedFile().getName();
            int length = fname.length();
            if (length >= 4) {
                ext = fname.substring(length - 4); //grab file extension
            } else {
                JOptionPane.showMessageDialog(MenuSetup.getMainWindow(), "Invalid File Name(too short, no extension)", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                if (ext.equals(".rtf")) { //rtf case

                    RTFEditorKit rtfKit = new RTFEditorKit();

                    FileInputStream fis = new FileInputStream(loadfile);
                    newFile(); //reset page
                    rtfKit.read(fis, CustomTextPane.getArea().getStyledDocument(), 0);
                    CustomTextPane.getArea().setCharacterAttributes(AttributeHandler.getDefaultStyle(), false);
                    CustomTextPane.setStyle(0, AttributeHandler.getDefaultStyle());

                    System.out.println(CustomTextPane.getText());
                    Element elements = rtfKit.getCharacterAttributeRun();
                    fis.close();
                } else if (ext.equals(".txt")) //txt case
                {
                    //FileReader fr = new FileReader(loadfile);
                    newFile(); //reset page
                    //CustomTextPane.getArea().read(fr, null);
                    BufferedReader reader = new BufferedReader(new FileReader(loadfile));
                    StringBuilder text = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        text.append(line).append("\n");
                    }

                    reader.close();

                    // Set the text of the JTextPane
                    CustomTextPane.getArea().setText(text.toString());



                    //fr.close();
                } else //invalid ext case
                {
                    JOptionPane.showMessageDialog(MenuSetup.getMainWindow(), "Unsupported file format. Please select .rtf or .txt.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                changeTabTitle(loadmenu.getSelectedFile().getName(), false);
                ((AbstractDocument) CustomTextPane.getArea().getDocument()).setDocumentFilter(new org.cpts.undo_redo.UndoFilter());
            } catch (Exception ignored) {}
        }
    }

    public static void newFile() {
        try {
            StyledDocument newDoc = new DefaultStyledDocument();
            CustomTextPane.getArea().setStyledDocument(newDoc);
        }
        catch (Exception ignored) {}

        CustomTextPane.getArea().setCharacterAttributes(AttributeHandler.getDefaultStyle(), true);
        CustomTextPane.setStyle(0, AttributeHandler.getDefaultStyle());
        ((AbstractDocument) CustomTextPane.getArea().getDocument()).setDocumentFilter(new org.cpts.undo_redo.UndoFilter());
        changeTabTitle("Document ", true);

        TabHandler.resetUndo();
    }
}
