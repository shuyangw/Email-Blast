package gui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import gui.MainGUI;

public class MainGUITest {

    @Test
    public void emailParserTest(){
        ArrayList<String> recipientList;

        String sample1 = "abcdefg@gmail.com";
        recipientList = MainGUI.parseRecipients(sample1);
        assertEquals("abcdefg@gmail.com", recipientList.get(0));

        String sample2 = "1234567@gmail.com";
        recipientList = MainGUI.parseRecipients(sample1 + "," + sample2);
        assertEquals(sample1, recipientList.get(0));
        assertEquals(sample2, recipientList.get(1));

        String sample3 = "zyx@umass.edu";
        recipientList = MainGUI.parseRecipients(
                sample1 + "," + sample2 + ", " + sample3);
        assertEquals(sample1, recipientList.get(0));
        assertEquals(sample2, recipientList.get(1));
        assertEquals(sample3, recipientList.get(2));
    }
}