/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;
/**
 *
 * @author Student
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Random;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * The Message class handles the creation, validation, and storage of messages.
 * It includes methods for generating hashes, checking lengths, and reading/writing JSON.
 */
public class Message {
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    // PART 3: Use Arrays instead of ArrayLists as per rubric
    public static String[] sentMessages = new String[100];
    public static String[] disregardedMessages = new String[100];
    public static String[] storedMessages = new String[100];
    public static String[] messageHashes = new String[100];
    public static String[] messageIDs = new String[100];
    public static String[] recipients = new String[100];

    // Counters
    private static int totalMessagesSent = 0;
    private static int sentCount = 0;
    private static int disregardedCount = 0;
    private static int storedCount = 0;

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageID = generateID();
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    private String generateID() {
        Random rand = new Random();
        // Ensures a 10-digit string
        return String.format("%010d", 1000000000L + (long)(rand.nextDouble() * 9000000000L));
    }

    public boolean checkMessageID() {
        return this.messageID.length() <= 10;
    }

    // Reference: Regular expression for international phone numbers 
    // Source: https://stackoverflow.com/questions/123559/how-to-validate-phone-numbers-using-regex
    public String checkRecipientCell() {
        if (recipient.startsWith("+") && recipient.length() >= 10) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = messageText.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }

    // FIX: Hash generation for single words
    public String createMessageHash() {
        if (messageText == null || messageText.isEmpty()) return "";
        
        String firstTwo = messageID.substring(0, 2);
        String msgNum = String.valueOf(messageNumber);
        
        String[] words = messageText.replaceAll("[^a-zA-Z ]", "").trim().split("\\s+");
        String firstWord = words[0].toUpperCase();
        String lastWord = words[words.length - 1].toUpperCase();

        // Fix: Prevent duplicating the word if it's a single-word message
        String hashWords = firstWord.equals(lastWord) ? firstWord : firstWord + lastWord;

        return firstTwo + ":" + msgNum + ":" + hashWords;
    }

    // FIX: Shows ID, Hash, Recipient, and Message as requested by lecturer
    public String sentMessage(String choice) {
        StringBuilder result = new StringBuilder();
        
        if (choice.equalsIgnoreCase("Send")) {
            totalMessagesSent++;
            sentMessages[sentCount] = messageText;
            messageIDs[sentCount] = messageID;
            messageHashes[sentCount] = messageHash;
            recipients[sentCount] = recipient;
            sentCount++;
            
            result.append("Message successfully sent.\n");
            result.append("----------------------------------------\n");
            result.append("Message ID   : ").append(messageID).append("\n");
            result.append("Message Hash : ").append(messageHash).append("\n");
            result.append("Recipient    : ").append(recipient).append("\n");
            result.append("Message      : ").append(messageText).append("\n");
            result.append("----------------------------------------");
            return result.toString();
            
        } else if (choice.equalsIgnoreCase("Store")) {
            storedMessages[storedCount] = messageText;
            messageIDs[storedCount] = messageID;
            messageHashes[storedCount] = messageHash;
            recipients[storedCount] = recipient;
            storedCount++;
            storeMessageToJson(); // Save to JSON
            return "Message successfully stored.";
            
        } else if (choice.equalsIgnoreCase("Discard")) {
            disregardedMessages[disregardedCount] = messageText;
            disregardedCount++;
            return "Press 0 to delete the message.";
        }
        return "Invalid choice.";
    }

    public String printMessages() {
        if (sentCount == 0) return "No messages sent yet.";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentCount; i++) {
            sb.append("ID: ").append(messageIDs[i])
              .append(" | Hash: ").append(messageHashes[i])
              .append(" | To: ").append(recipients[i])
              .append(" | Msg: ").append(sentMessages[i]).append("\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() { 
    return totalMessagesSent;
}
    
    // FIX: Proper JSON Array formatting (overwrites file with a valid array)
    public static void storeMessageToJson() {
        try (FileWriter file = new FileWriter("stored_messages.json")) {
            StringBuilder json = new StringBuilder("[\n");
            for (int i = 0; i < storedCount; i++) {
                json.append("  {\n")
                    .append("    \"id\": \"").append(messageIDs[i]).append("\",\n")
                    .append("    \"recipient\": \"").append(recipients[i]).append("\",\n")
                    .append("    \"message\": \"").append(storedMessages[i]).append("\",\n")
                    .append("    \"hash\": \"").append(messageHashes[i]).append("\"\n")
                    .append("  }");
                if (i < storedCount - 1) {
                    json.append(","); // Add comma between array elements
                }
                json.append("\n");
            }
            json.append("]");
            file.write(json.toString());
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    
    //PART 3: STORED MESSAGES MENU METHODS
    

    public static String displayStoredSenderRecipient() {
        if (storedCount == 0) return "No stored messages.";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < storedCount; i++) {
            sb.append("Recipient: ").append(recipients[i])
              .append(" | Message: ").append(storedMessages[i]).append("\n");
        }
        return sb.toString();
    }

    public static String displayLongestStoredMessage() {
        if (storedCount == 0) return "No stored messages.";
        String longest = storedMessages[0];
        for (int i = 1; i < storedCount; i++) {
            if (storedMessages[i].length() > longest.length()) {
                longest = storedMessages[i];
            }
        }
        return "Longest message: " + longest;
    }

    public static String searchByMessageID(String targetID) {
        for (int i = 0; i < storedCount; i++) {
            if (messageIDs[i].equals(targetID)) {
                return "Recipient: " + recipients[i] + "\nMessage: " + storedMessages[i];
            }
        }
        return "Message ID not found.";
    }

    public static String searchByRecipient(String targetRecipient) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < storedCount; i++) {
            if (recipients[i].equals(targetRecipient)) {
                sb.append("Message: ").append(storedMessages[i])
                  .append(" (ID: ").append(messageIDs[i]).append(")\n");
            }
        }
        return sb.length() == 0 ? "No messages found for this recipient." : sb.toString();
    }

    public static String deleteByHash(String targetHash) {
        for (int i = 0; i < storedCount; i++) {
            if (messageHashes[i].equals(targetHash)) {
                String deletedMsg = storedMessages[i];
                // Shift array elements left to delete
                for (int j = i; j < storedCount - 1; j++) {
                    storedMessages[j] = storedMessages[j + 1];
                    messageIDs[j] = messageIDs[j + 1];
                    messageHashes[j] = messageHashes[j + 1];
                    recipients[j] = recipients[j + 1];
                }
                storedMessages[--storedCount] = null;
                messageIDs[storedCount] = null;
                messageHashes[storedCount] = null;
                recipients[storedCount] = null;
                
                storeMessageToJson(); // Update JSON after deletion
                return "Message \"" + deletedMsg + "\" successfully deleted.";
            }
        }
        return "Hash not found.";
    }

    public static String displayReport() {
        if (storedCount == 0) return "No stored messages.";
        StringBuilder sb = new StringBuilder("===== Stored Messages Report =====\n");
        for (int i = 0; i < storedCount; i++) {
            sb.append("Hash: ").append(messageHashes[i])
              .append(" | Recipient: ").append(recipients[i])
              .append(" | Message: ").append(storedMessages[i]).append("\n");
        }
        return sb.toString();
    }

    // Getters for Unit Tests
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    
    public static void readAndDisplayJson() {
        try (BufferedReader br = new BufferedReader(new FileReader("stored_messages.json"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            
            String jsonContent = sb.toString();
            if (jsonContent.trim().isEmpty() || jsonContent.equals("[]")) {
                System.out.println("No messages found in the JSON file.");
                return;
            }

            System.out.println("\n--- Messages Read from JSON File ---");
            // Simple parsing for display purposes
            String[] entries = jsonContent.split("\\},\\s*\\{");
            
            for (String entry : entries) {
                // Clean up the brackets and quotes
                entry = entry.replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace("\"", "");
                
                String id = "Unknown";
                String rec = "Unknown";
                String msg = "Unknown";
                
                String[] pairs = entry.split(",\\s*");
                for (String pair : pairs) {
                    String[] keyValue = pair.split(":");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();
                        if (key.equals("id")) id = value;
                        else if (key.equals("recipient")) rec = value;
                        else if (key.equals("message")) msg = value;
                    }
                }
                System.out.println("ID: " + id + " | To: " + rec + " | Msg: " + msg);
            }
            System.out.println("-------------------------------------");
            
        } catch (IOException e) {
            System.out.println("Error reading JSON file (File might not exist yet): " + e.getMessage());
            
        }
    }
      public static void resetForTesting() {
        sentMessages = new String[100];
        disregardedMessages = new String[100];
        storedMessages = new String[100];
        messageHashes = new String[100];
        messageIDs = new String[100];
        recipients = new String[100];
        totalMessagesSent = 0;
        sentCount = 0;
        disregardedCount = 0;
        storedCount = 0;
    }
}