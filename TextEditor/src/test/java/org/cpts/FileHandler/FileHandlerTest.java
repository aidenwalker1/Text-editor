package org.cpts.FileHandler;

import org.cpts.Doc;
import org.cpts.mainUI.MenuSetup;
import org.cpts.tabs.TabHandler;
import org.cpts.textAttributes.CustomTextPane;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;

class FileHandlerTest {


    @Mock
    private JFileChooser mockedChooser;

    private MockedStatic<JOptionPane> mockedOption;


    @BeforeAll
    public static void fixit()
    {
        MenuSetup m = new MenuSetup(new JFrame());

        m.createMenu();
        org.cpts.tabs.TabHandler.setUp();

    }

    @BeforeEach
    public void SetUp()
    {
        mockedChooser = Mockito.mock(JFileChooser.class);
        Mockito.when(mockedChooser.showSaveDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        Mockito.when(mockedChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        org.cpts.files.FileHandler.newFile();
        mockedOption = mockStatic(JOptionPane.class);

    }

    @AfterAll
     public static void TearDown()
    {
        TabHandler.getPane().setSelectedIndex(0);
        while (TabHandler.getPane().getTabCount() > 1) {
            TabHandler.removePane();
        }
        TabHandler.removePane();
        org.cpts.files.FileHandler.newFile();
    }

    @AfterEach
    void closeMocks() {
        mockedOption.close();
    }

    @Test
    public void regTextFileSaveLoad()
    {
        Mockito.when(mockedChooser.getSelectedFile()).thenReturn(new File("test.txt"));
        Doc.insert(0, "test");
        org.cpts.files.FileHandler.save(mockedChooser);

        assertEquals("test.txt", org.cpts.tabs.TabHandler.getPane().getTitleAt(0));

        org.cpts.files.FileHandler.newFile();

        org.cpts.files.FileHandler.load(mockedChooser);

        assertEquals("test\n", CustomTextPane.getText());

    }

    @Test
    public void rtfFileSaveLoad()
    {
        Mockito.when(mockedChooser.getSelectedFile()).thenReturn(new File("test.rtf"));
        Doc.insert(0, "test");
        MutableAttributeSet changes =  CustomTextPane.extractStyle(0);
        StyleConstants.setBold(changes, true);
        CustomTextPane.setStyle(1, changes);
        CustomTextPane.setStyle(2, changes);

        org.cpts.files.FileHandler.save(mockedChooser);


        assertEquals("test.rtf", org.cpts.tabs.TabHandler.getPane().getTitleAt(0));

        org.cpts.files.FileHandler.newFile();

        org.cpts.files.FileHandler.load(mockedChooser);
        AttributeSet loaded = CustomTextPane.extractStyle(1);
        Assertions.assertTrue(StyleConstants.isBold(changes));
        Assertions.assertTrue(StyleConstants.isBold(loaded));

    }


    @Test
    public void regTextFileSaveLoadNoExt()
    {
        Mockito.when(mockedChooser.getSelectedFile()).thenReturn(new File("tn"));
        FileNameExtensionFilter fText = new FileNameExtensionFilter("Text Files", "txt");
        mockedChooser.setFileFilter(fText);
        Mockito.when(mockedChooser.getFileFilter()).thenReturn(fText);

        Doc.insert(0, "test");

        org.cpts.files.FileHandler.save(mockedChooser);

        assertEquals("tn", org.cpts.tabs.TabHandler.getPane().getTitleAt(0));

        org.cpts.files.FileHandler.newFile();

        Mockito.when(mockedChooser.getSelectedFile()).thenReturn(new File("tn.txt"));

        org.cpts.files.FileHandler.load(mockedChooser);

        assertEquals("test\n", CustomTextPane.getText());

    }

    @Test
    public void rtfFileSaveLoadNoExt()
    {
        Mockito.when(mockedChooser.getSelectedFile()).thenReturn(new File("testrtfnext"));
        FileNameExtensionFilter fRtf = new FileNameExtensionFilter("RTF Files", "rtf");
        mockedChooser.setFileFilter(fRtf);
        Mockito.when(mockedChooser.getFileFilter()).thenReturn(fRtf);
        Doc.insert(0, "test");
        MutableAttributeSet changes =  CustomTextPane.extractStyle(0);
        StyleConstants.setBold(changes, true);
        CustomTextPane.setStyle(1, changes);
        CustomTextPane.setStyle(2, changes);

        org.cpts.files.FileHandler.save(mockedChooser);


        assertEquals("testrtfnext", org.cpts.tabs.TabHandler.getPane().getTitleAt(0));

        org.cpts.files.FileHandler.newFile();

        Mockito.when(mockedChooser.getSelectedFile()).thenReturn(new File("testrtfnext.rtf"));

        org.cpts.files.FileHandler.load(mockedChooser);
        AttributeSet loaded = CustomTextPane.extractStyle(1);
        Assertions.assertTrue(StyleConstants.isBold(changes));
        Assertions.assertTrue(StyleConstants.isBold(loaded));

    }

    @Test
    public void invalidExt()
    {
        Mockito.when(mockedChooser.getSelectedFile()).thenReturn(new File("test.doc"));

        Doc.insert(0, "test");
        org.cpts.files.FileHandler.save(mockedChooser);


        org.cpts.files.FileHandler.newFile();

        org.cpts.files.FileHandler.load(mockedChooser);

        assertEquals("", CustomTextPane.getText());



    }

    @Test
    public void invalidExtViaLength()
    {
        Mockito.when(mockedChooser.getSelectedFile()).thenReturn(new File("tes"));


        org.cpts.files.FileHandler.newFile();

        org.cpts.files.FileHandler.load(mockedChooser);

        assertEquals("", CustomTextPane.getText());



    }

    @Test
    public void invalidLoadViaError()
    {

        Mockito.when(mockedChooser.getSelectedFile()).thenReturn(new File("the directory/isbad.txt"));

        org.cpts.files.FileHandler.newFile();

        org.cpts.files.FileHandler.load(mockedChooser);

        assertEquals("", CustomTextPane.getText());

    }

    @Test
    public void notApproved()
    {
        Mockito.when(mockedChooser.showSaveDialog(null)).thenReturn(1);
        org.cpts.files.FileHandler.save(mockedChooser);

        assertEquals("Document 0", org.cpts.tabs.TabHandler.getPane().getTitleAt(0));




        Mockito.when(mockedChooser.showOpenDialog(null)).thenReturn(1);

        org.cpts.files.FileHandler.load(mockedChooser);

        assertEquals("Document 0", org.cpts.tabs.TabHandler.getPane().getTitleAt(0));

    }




}
