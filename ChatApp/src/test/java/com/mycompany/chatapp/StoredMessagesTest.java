/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author Student
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StoredMessagesTest {
   @BeforeEach
    public void setUp() {
        // Clear the arrays before every single test so they don't mix data
        Message.resetForTesting();
    }

    // 1. Sent Messages array correctly populated
    @Test
    public void testSentMessagesArrayPopulated() {
        Message msg1 = new Message(1, "+27834557896", "Did you get the cake?");
        msg1.sentMessage("Send");
        
        // Check that the array holds the data correctly
        assertEquals("Did you get the cake?", Message.sentMessages[0]);
        assertNotNull(Message.messageIDs[0]);
        assertNotNull(Message.messageHashes[0]);
    }

    // 2. Display the longest Message
    @Test
    public void testDisplayLongestMessage() {
        Message msg1 = new Message(1, "+27834557896", "Short msg");
        msg1.sentMessage("Store");
        
        Message msg2 = new Message(2, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage("Store");

        String result = Message.displayLongestStoredMessage();
        assertTrue(result.contains("Where are you? You are late!"));
    }

    // 3. Search for messageID
    @Test
    public void testSearchByMessageID() {
        Message msg = new Message(1, "+27838884567", "It is dinner time!");
        msg.sentMessage("Store");
        
        String generatedID = msg.getMessageID();
        
        String result = Message.searchByMessageID(generatedID);
        assertTrue(result.contains("It is dinner time!"));
        assertTrue(result.contains("+27838884567"));
    }

    // 4. Search all the messages sent or stored regarding a particular recipient
    @Test
    public void testSearchByRecipient() {
        Message msg1 = new Message(1, "+27838884567", "Message 1");
        msg1.sentMessage("Store");
        
        Message msg2 = new Message(2, "+27838884567", "Message 2");
        msg2.sentMessage("Store");
        
        Message msg3 = new Message(3, "+27831111111", "Message 3 (Different person)");
        msg3.sentMessage("Store");

        String result = Message.searchByRecipient("+27838884567");
        
        assertTrue(result.contains("Message 1"));
        assertTrue(result.contains("Message 2"));
        assertFalse(result.contains("Message 3 (Different person)"));
    }

    // 5. Delete a message using a message hash
    @Test
    public void testDeleteMessageByHash() {
        Message msg = new Message(1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        msg.sentMessage("Store");
        
        String hashToDelete = msg.getMessageHash();
        
        String result = Message.deleteByHash(hashToDelete);
        
        assertTrue(result.contains("successfully deleted"));
        // Check that the array is now empty
        assertEquals("No stored messages.", Message.displayStoredSenderRecipient());
    }
    
    // 6. Display Report
    @Test
    public void testDisplayReport() {
        Message msg = new Message(1, "+27838884567", "Test report message");
        msg.sentMessage("Store");
        
        String report = Message.displayReport();
        assertTrue(report.contains("Test report message"));
        assertTrue(report.contains("+27838884567"));
        assertTrue(report.contains(msg.getMessageHash())); // Report must include the hash
    }
} 

