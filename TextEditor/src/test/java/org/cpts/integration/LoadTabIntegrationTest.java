package org.cpts.integration;


import io.qameta.allure.TmsLink;
import org.cpts.mainUI.MenuSetup;
import org.cpts.tabs.TabHandler;
import org.cpts.textAttributes.CustomTextPane;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import javax.swing.*;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoadTabIntegrationTest {

    @Mock
    private JFileChooser mockedChooser;

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
        Mockito.when(mockedChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        org.cpts.files.FileHandler.newFile();

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


    @TmsLink("C20")
    @Test
    void LoadTabTitlePair(){

        Mockito.when(mockedChooser.getSelectedFile()).thenReturn(new File("PairWiseTitle.txt"));

        org.cpts.files.FileHandler.load(mockedChooser);

        //ChangeTabTitle Verification
        assertEquals("PairWiseTitle.txt", org.cpts.tabs.TabHandler.getPane().getTitleAt(0));

        //Load Success Verification
        assertEquals("Load Complete. Title of tab also successfully changed.\n", CustomTextPane.getText());


    }
}
