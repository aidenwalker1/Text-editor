package org.cpts.systemTests;

import io.qameta.allure.TmsLink;
import org.assertj.swing.core.*;
import org.assertj.swing.core.Robot;
import org.assertj.swing.core.matcher.DialogMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.*;
import org.assertj.swing.timing.Pause;
import org.assertj.swing.finder.DialogFinder;
import org.cpts.files.FileHandler;
import org.cpts.mainUI.MainWindow;
import org.cpts.textAttributes.AttributeHandler;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;

import static org.assertj.swing.core.matcher.JButtonMatcher.withText;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

public class SystemTest {

    static FrameFixture window;

    @BeforeAll
    static void setup() {
        Robot robot = BasicRobot.robotWithNewAwtHierarchy();
        window = new FrameFixture(robot, new MainWindow());
        window.show();
    }

    @BeforeEach
    void beforeEach() {
        for (int i = 0; i < 3; i++) {
            clickMenuButton("View", "Remove Tab");
        }
    }

    JTextPane getFirstPane() {
        ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();

        return (JTextPane) finder.findByName("Panel 1", true);
    }

    void clickMenuButton(String menu, String button) {
        assertDoesNotThrow(() -> {
            // get view button, click
            JMenuItemFixture menuButton = getMenuItem(menu);
            menuButton.click();

            // click new tab button, click
            JMenuItemFixture buttonItem = getMenuItem(button);
            buttonItem.click();
        });
    }

    void assertContainsAttribute(int index, Object name, Object value) {
        JTextPane pane = getFirstPane();
        assertTrue(pane.getStyledDocument().getCharacterElement(index).getAttributes().containsAttribute(name,value));
    }

    @TmsLink("C5")
    @Test
    void testUndoRedo() {
        // get text box, insert text
        JTextComponentFixture textBox = window.textBox("Panel 1");
        // enter text to test
        textBox.enterText("123");
        assertEquals(textBox.text(), "123");

        // test undo
        clickMenuButton("Edit", "Undo");
        assertEquals(textBox.text(), "12");

        // test undoing bold
        textBox.select("12");

        clickMenuButton("Format", "Bold");
        assertContainsAttribute(1, StyleConstants.Bold, true);

        // test undo
        clickMenuButton("Edit", "Undo");
        assertContainsAttribute(1, StyleConstants.Bold, false);

        // test redo
        clickMenuButton("Edit", "Redo");
        assertContainsAttribute(1, StyleConstants.Bold, true);

        // undo it again to reset to old state
        clickMenuButton("Edit", "Undo");

        // add text to reset undo
        textBox.selectText(0,0);
        String oldText = textBox.text();

        textBox.enterText("t");
        String newText = textBox.text();

        // check there is no redo applied, then undo
        clickMenuButton("Edit", "Redo");
        assertContainsAttribute(1, StyleConstants.Bold, false);
        clickMenuButton("Edit", "Undo");

        // test that undo/redo worked
        assertEquals(oldText, textBox.text());

        clickMenuButton("Edit", "Redo");

        assertEquals(newText, textBox.text());
    }

    @TmsLink("C6")
    @Test
    void testTabs() {
        // get tabbed pane
        JTabbedPaneFixture tabbedPane = window.tabbedPane("Tabbed Pane");

        assertEquals(tabbedPane.tabTitles().length, 1);
        assertEquals(tabbedPane.tabTitles()[0], "Document 0");

        // get text box, insert text
        JTextComponentFixture textBox = window.textBox("Panel 1");
        assertTrue(textBox.text().isEmpty());

        textBox.enterText("123");

        clickMenuButton("View", "Add Tab");

        // assert tabs are correct
        assertEquals(tabbedPane.tabTitles().length, 2);
        assertEquals(tabbedPane.tabTitles()[0], "Document 0");
        assertEquals(tabbedPane.tabTitles()[1], "Document 1");

        assertEquals("123", textBox.text());
        tabbedPane.selectTab("Document 1");

        // get text box, insert text
        JTextComponentFixture textBox2 = window.textBox("Panel 2");
        assertTrue(textBox2.text().isEmpty());

        textBox2.enterText("test");

        tabbedPane.selectTab("Document 0");

        textBox.enterText("4");
        assertEquals("1234", textBox.text());

        clickMenuButton("View", "Add Tab");

        // assert tabs are correct
        assertEquals(tabbedPane.tabTitles().length, 3);
        assertEquals(tabbedPane.tabTitles()[2], "Document 2");

        tabbedPane.selectTab("Document 2");

        // assert tab was removed
        clickMenuButton("View", "Remove Tab");

        assertEquals(tabbedPane.tabTitles().length, 2);
    }

    @Nested
    class FormatTabTests{

        @TmsLink("C7")
        @Test
        void alignmentTest(){

            JTextComponentFixture textBox = window.textBox("Panel 1");
            textBox.enterText("123");

            assertEquals(textBox.text(), "123");
            // get format button, click
            JMenuItemFixture formatButton = getMenuItem("Format");
            if(formatButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            JMenuItemFixture alignButton = getMenuItem("Alignment");
            if(alignButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            JMenuItemFixture leftAlignButton = getMenuItem("Left Align");
            if(leftAlignButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            JMenuItemFixture centerAlignButton = getMenuItem("Center Align");
            if(centerAlignButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            JMenuItemFixture rightAlignButton = getMenuItem("Right Align");
            if(rightAlignButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
            JTextPane textPane = (JTextPane) finder.findByName("Panel 1", true);

            assertTrue(extractStyle(textPane,0).containsAttribute(StyleConstants.Alignment, 0));

            formatButton.click();
            alignButton.click();
            centerAlignButton.click();

            assertTrue(extractStyle(textPane,0).containsAttribute(StyleConstants.Alignment, 1));

            rightAlignButton.click();
            assertTrue(extractStyle(textPane,0).containsAttribute(StyleConstants.Alignment, 2));

            leftAlignButton.click();
            assertTrue(extractStyle(textPane,0).containsAttribute(StyleConstants.Alignment, 0));
        }

        @TmsLink("C8")
        @Test
        void boldTest(){
            JTextComponentFixture textBox = window.textBox("Panel 1");
            textBox.enterText("123");

            assertEquals(textBox.text(), "123");
            // get format button, click
            JMenuItemFixture formatButton = getMenuItem("Format");
            if(formatButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }
            textBox.selectText(0,3);
            formatButton.click();


            JMenuItemFixture boldButton = getMenuItem("Bold");
            if(boldButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }
            boldButton.click();


            ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
            JTextPane textPane = (JTextPane) finder.findByName("Panel 1", true);

            textPane.setSelectionStart(0);
            textPane.setSelectionEnd(3);



            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.Bold, true));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.Bold, true));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.Bold, true));

            JMenuItemFixture undoButton = getMenuItem("Undo");
            undoButton.click();

            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.Bold, false));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.Bold, false));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.Bold, false));

            formatButton.click();
            boldButton.click();
            textBox.enterText("abc");

            assertTrue(extractStyle(textPane, 3).containsAttribute(StyleConstants.Bold, true));
            assertTrue(extractStyle(textPane, 4).containsAttribute(StyleConstants.Bold, true));
            assertTrue(extractStyle(textPane, 5).containsAttribute(StyleConstants.Bold, true));

        }

        @TmsLink("C9")
        @Test
        void italicTest(){
            JTextComponentFixture textBox = window.textBox("Panel 1");
            textBox.enterText("123");

            assertEquals(textBox.text(), "123");
            // get format button, click
            JMenuItemFixture formatButton = getMenuItem("Format");
            if(formatButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            JMenuItemFixture italicButton = getMenuItem("Italic");
            if(italicButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }
            textBox.selectText(0,3);
            formatButton.click();
            italicButton.click();


            ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
            JTextPane textPane = (JTextPane) finder.findByName("Panel 1", true);

            textPane.setSelectionStart(0);
            textPane.setSelectionEnd(3);



            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.Italic, true));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.Italic, true));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.Italic, true));

            JMenuItemFixture undoButton = getMenuItem("Undo");
            undoButton.click();

            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.Italic, false));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.Italic, false));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.Italic, false));

            formatButton.click();
            italicButton.click();
            textBox.enterText("abc");

            assertTrue(extractStyle(textPane, 3).containsAttribute(StyleConstants.Italic, true));
            assertTrue(extractStyle(textPane, 4).containsAttribute(StyleConstants.Italic, true));
            assertTrue(extractStyle(textPane, 5).containsAttribute(StyleConstants.Italic, true));

        }

        @TmsLink("C10")
        @Test
        void underlineTest(){
            JTextComponentFixture textBox = window.textBox("Panel 1");
            textBox.enterText("123");

            assertEquals(textBox.text(), "123");
            // get format button, click
            JMenuItemFixture formatButton = getMenuItem("Format");
            if(formatButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            JMenuItemFixture underlineButton = getMenuItem("Underline");
            if(underlineButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }
            textBox.selectText(0,3);
            formatButton.click();
            underlineButton.click();


            ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
            JTextPane textPane = (JTextPane) finder.findByName("Panel 1", true);

            textPane.setSelectionStart(0);
            textPane.setSelectionEnd(3);

            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.Underline, true));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.Underline, true));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.Underline, true));

            JMenuItemFixture undoButton = getMenuItem("Undo");
            undoButton.click();

            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.Underline, false));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.Underline, false));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.Underline, false));

            formatButton.click();
            underlineButton.click();
            textBox.enterText("abc");

            assertTrue(extractStyle(textPane, 3).containsAttribute(StyleConstants.Underline, true));
            assertTrue(extractStyle(textPane, 4).containsAttribute(StyleConstants.Underline, true));
            assertTrue(extractStyle(textPane, 5).containsAttribute(StyleConstants.Underline, true));

        }

        @TmsLink("C11")
        @Test
        void strikethroughTest(){
            JTextComponentFixture textBox = window.textBox("Panel 1");
            textBox.enterText("123");

            assertEquals(textBox.text(), "123");
            // get format button, click
            JMenuItemFixture formatButton = getMenuItem("Format");
            if(formatButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            JMenuItemFixture strikethroughButton = getMenuItem("Strikethrough");
            if(strikethroughButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }
            textBox.selectText(0,3);
            formatButton.click();
            strikethroughButton.click();


            ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
            JTextPane textPane = (JTextPane) finder.findByName("Panel 1", true);

            textPane.setSelectionStart(0);
            textPane.setSelectionEnd(3);



            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.StrikeThrough, true));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.StrikeThrough, true));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.StrikeThrough, true));

            JMenuItemFixture undoButton = getMenuItem("Undo");
            undoButton.click();

            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.StrikeThrough, false));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.StrikeThrough, false));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.StrikeThrough, false));

            formatButton.click();
            strikethroughButton.click();
            textBox.enterText("abc");

            assertTrue(extractStyle(textPane, 3).containsAttribute(StyleConstants.StrikeThrough, true));
            assertTrue(extractStyle(textPane, 4).containsAttribute(StyleConstants.StrikeThrough, true));
            assertTrue(extractStyle(textPane, 5).containsAttribute(StyleConstants.StrikeThrough, true));

        }

        @TmsLink("C12")
        @Test
        void subscriptTest(){
            JTextComponentFixture textBox = window.textBox("Panel 1");
            textBox.enterText("123");

            assertEquals(textBox.text(), "123");
            // get format button, click
            JMenuItemFixture formatButton = getMenuItem("Format");
            if(formatButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            JMenuItemFixture subscriptButton = getMenuItem("Subscript");
            if(subscriptButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }
            textBox.selectText(1,3);
            formatButton.click();
            subscriptButton.click();


            ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
            JTextPane textPane = (JTextPane) finder.findByName("Panel 1", true);

            textPane.setSelectionStart(1);
            textPane.setSelectionEnd(3);



            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.Subscript, false));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.Subscript, true));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.Subscript, true));

            JMenuItemFixture undoButton = getMenuItem("Undo");
            undoButton.click();

            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.Subscript, false));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.Subscript, false));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.Subscript, false));

            formatButton.click();
            subscriptButton.click();
            textBox.enterText("abc");

            assertTrue(extractStyle(textPane, 3).containsAttribute(StyleConstants.Subscript, true));
            assertTrue(extractStyle(textPane, 4).containsAttribute(StyleConstants.Subscript, true));
            assertTrue(extractStyle(textPane, 5).containsAttribute(StyleConstants.Subscript, true));

        }

        @TmsLink("C13")
        @Test
        void superscriptTest(){
            JTextComponentFixture textBox = window.textBox("Panel 1");
            textBox.enterText("123");

            assertEquals(textBox.text(), "123");
            // get format button, click
            JMenuItemFixture formatButton = getMenuItem("Format");
            if(formatButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            JMenuItemFixture superscriptButton = getMenuItem("Superscript");
            if(superscriptButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }
            textBox.selectText(1,3);
            formatButton.click();
            superscriptButton.click();


            ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
            JTextPane textPane = (JTextPane) finder.findByName("Panel 1", true);

            textPane.setSelectionStart(1);
            textPane.setSelectionEnd(3);



            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.Superscript, false));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.Superscript, true));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.Superscript, true));

            JMenuItemFixture undoButton = getMenuItem("Undo");
            undoButton.click();

            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.Superscript, false));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.Superscript, false));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.Superscript, false));

            formatButton.click();
            superscriptButton.click();
            textBox.enterText("abc");

            assertTrue(extractStyle(textPane, 3).containsAttribute(StyleConstants.Superscript, true));
            assertTrue(extractStyle(textPane, 4).containsAttribute(StyleConstants.Superscript, true));
            assertTrue(extractStyle(textPane, 5).containsAttribute(StyleConstants.Superscript, true));

        }

        @TmsLink("C14")
        @Test
        void ClearFormattingTest(){
            JTextComponentFixture textBox = window.textBox("Panel 1");
            textBox.enterText("123");

            assertEquals(textBox.text(), "123");
            // get format button, click
            JMenuItemFixture formatButton = getMenuItem("Format");
            if(formatButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            JMenuItemFixture boldButton = getMenuItem("Bold");
            if(boldButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }
            JMenuItemFixture italicButton = getMenuItem("Italic");
            if(italicButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            JMenuItemFixture clearFormatButton = getMenuItem("Clear Formatting");
            if(clearFormatButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }
            textBox.selectText(0,3);
            formatButton.click();
            boldButton.click();
            italicButton.click();


            ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
            JTextPane textPane = (JTextPane) finder.findByName("Panel 1", true);

            textPane.setSelectionStart(0);
            textPane.setSelectionEnd(3);



            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.Bold, true));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.Bold, true));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.Bold, true));

            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.Italic, true));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.Italic, true));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.Italic, true));

            clearFormatButton.click();

            assert(extractStyle(textPane, 0).isEqual(AttributeHandler.getDefaultStyle()));
            assert(extractStyle(textPane, 1).isEqual(AttributeHandler.getDefaultStyle()));
            assert(extractStyle(textPane, 2).isEqual(AttributeHandler.getDefaultStyle()));

        }

        @TmsLink("C15")
        @Test
        void textColorTest(){
            JTextComponentFixture textBox = window.textBox("Panel 1");
            textBox.enterText("123");

            assertEquals(textBox.text(), "123");
            // get format button, click
            JMenuItemFixture formatButton = getMenuItem("Format");
            if(formatButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            JMenuItemFixture textColorButton = getMenuItem("Text Color");
            if(textColorButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
            JTextPane textPane = (JTextPane) finder.findByName("Panel 1", true);

            textBox.selectText(0,3);
            formatButton.click();
            textColorButton.click();

            JColorChooser colorChooser = (JColorChooser) getColorChooser().toArray()[1];

            //JColorChooser colorChooser = (JColorChooser) finder.findByType(JColorChooser.class);
            colorChooser.setColor(Color.BLUE);

            JButtonFixture okButton = getButton("OK");
            okButton.click();

            textPane.setSelectionStart(0);
            textPane.setSelectionEnd(3);

            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.Foreground, Color.BLUE));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.Foreground, Color.BLUE));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.Foreground, Color.BLUE));


        }

        @TmsLink("C16")
        @Test
        void highlightColorTest(){
            JTextComponentFixture textBox = window.textBox("Panel 1");
            textBox.enterText("123");

            assertEquals(textBox.text(), "123");
            // get format button, click
            JMenuItemFixture formatButton = getMenuItem("Format");
            if(formatButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            JMenuItemFixture highlightButton = getMenuItem("Highlight");
            if(highlightButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
            JTextPane textPane = (JTextPane) finder.findByName("Panel 1", true);

            textBox.selectText(0,3);
            formatButton.click();
            highlightButton.click();

            JColorChooser colorChooser = (JColorChooser) getColorChooser().toArray()[0];

            //JColorChooser colorChooser = (JColorChooser) finder.findByType(JColorChooser.class);
            colorChooser.setColor(Color.CYAN);

            JButtonFixture okButton = getButton("OK");
            okButton.click();

            textPane.setSelectionStart(0);
            textPane.setSelectionEnd(3);

            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.Background, Color.CYAN));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.Background, Color.CYAN));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.Background, Color.CYAN));
        }

        @TmsLink("C17")
        @Test
        void textSizeTest(){
            JTextComponentFixture textBox = window.textBox("Panel 1");
            textBox.enterText("123");

            assertEquals(textBox.text(), "123");
            // get format button, click
            JMenuItemFixture formatButton = getMenuItem("Format");
            if(formatButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            JMenuItemFixture textSizeButton = getMenuItem("Text Size");
            if(textSizeButton!=null){
                assertTrue(true);
            }
            else{
                fail();
            }

            ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();

            textBox.selectText(0,3);
            formatButton.click();
            textSizeButton.click();

            JOptionPane option = finder.findByType(JOptionPane.class);
            option.setInputValue("8");
            option.setValue("8");

            JTextPane textPane = (JTextPane) finder.findByName("Panel 1", true);
            textPane.setSelectionStart(0);
            textPane.setSelectionEnd(3);

            assertTrue(extractStyle(textPane, 0).containsAttribute(StyleConstants.FontSize, 8));
            assertTrue(extractStyle(textPane, 1).containsAttribute(StyleConstants.FontSize, 8));
            assertTrue(extractStyle(textPane, 2).containsAttribute(StyleConstants.FontSize, 8));


        }

    }

    @TmsLink("C18")
    @Test
    void SaveLoadTest(){
        //issues with static context so doing this setup in here
        JFileChooser mockedChooser = Mockito.mock(JFileChooser.class);
        Mockito.when(mockedChooser.showSaveDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        Mockito.when(mockedChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        Mockito.when(mockedChooser.getCurrentDirectory()).thenReturn(new File(System.getProperty("user.dir")));
        Mockito.when(mockedChooser.getSelectedFile()).thenReturn(new File("test.txt"));

        JTabbedPaneFixture tabbedPane = window.tabbedPane("Tabbed Pane");

        JMenuItemFixture sButton = getMenuItem("Save");
        JMenuItemFixture lButton = getMenuItem("Load");
        JMenuItemFixture nButton = getMenuItem("New");
        JMenuItemFixture fButton = getMenuItem("File");

        JMenuItem sMock = sButton.target();
        JMenuItem lMock = lButton.target();

        for (ActionListener al : sMock.getActionListeners()) {
            sMock.removeActionListener(al);
        }

        for (ActionListener al : lMock.getActionListeners()) {
            lMock.removeActionListener(al);
        }

        sMock.addActionListener(e -> FileHandler.save(mockedChooser));
        lMock.addActionListener(e -> FileHandler.load(mockedChooser));

        JTextComponentFixture textBox = window.textBox("Panel 1");
        textBox.enterText("This will save and load successfully");

        fButton.click();
        sButton.click();
        assertEquals(tabbedPane.tabTitles()[0], "test.txt");

        fButton.click();
        nButton.click();
        assertEquals(tabbedPane.tabTitles()[0], "Document 0");
        assert(textBox.text().isEmpty());

        fButton.click();
        lButton.click();
        Pause.pause(1000); //wait for dialogue to close
        assertEquals(tabbedPane.tabTitles()[0], "test.txt");
        assertEquals(textBox.text(),"This will save and load successfully\n");



    }

    @TmsLink("C19")
    @Test
    void FindReplace()
    {
        JTextComponentFixture textBox = window.textBox("Panel 1");
        textBox.enterText("the the the the");



        clickMenuButton("Edit", "Find");

        DialogFixture findD = window.dialog();
        findD.textBox().enterText("the");
        findD.button(withText("OK")).click();



        clickMenuButton("Edit", "Replace");

        DialogFixture replaceD = window.dialog();
        replaceD.textBox().enterText("dog");
        replaceD.button(withText("OK")).click();


        assertEquals("dog dog dog dog", textBox.text());


    }


    Collection<JColorChooser> getColorChooser(){
        ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
        return finder.findAll(new GenericTypeMatcher<>(JColorChooser.class) {
            @Override
            protected boolean isMatching(JColorChooser jColorChooser) {
                return true;
            }
        });
    }


    JButtonFixture getButton(String text) {
        return window.button(new GenericTypeMatcher<>(JButton.class) {
            @Override
            protected boolean isMatching(JButton jButton) {
                return jButton.getText().equals(text);
            }
        });
    }

    JMenuItemFixture getMenuItem(String text) {
        return window.menuItem(new GenericTypeMatcher<>(JMenuItem.class) {
            @Override
            protected boolean isMatching(JMenuItem item) {
                return item.getText().startsWith(text);

            }
        });
    }

    private Style extractStyle(JTextPane textPane, int index) {
        // get attributes from character
        AttributeSet characterAttributes = textPane.getStyledDocument().getCharacterElement(index).getAttributes();
        AttributeSet paragraphAttributes = textPane.getStyledDocument().getParagraphElement(index).getAttributes();

        // create style object
        StyleContext context = new StyleContext();
        Style style = context.addStyle(null, null);

        // add attributes to style
        style.addAttributes(characterAttributes);
        style.addAttribute(StyleConstants.Alignment, paragraphAttributes.getAttribute(StyleConstants.Alignment));
        style.addAttribute(StyleConstants.LineSpacing, paragraphAttributes.getAttribute(StyleConstants.LineSpacing));

        return style;
    }
}