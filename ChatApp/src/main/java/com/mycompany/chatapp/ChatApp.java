/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatapp;

/**
 *
 * @author Student
 */
import java.util.Scanner;
public class ChatApp {

    /**
 * The main entry point for the QuickChat application.
 * Handles user registration, login, and the main menu navigation.
 */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Registration phase
        System.out.println("Registration ");
        System.out.print("Enter username: ");
        String username = input.nextLine();

        System.out.print("Enter password: ");
        String password = input.nextLine();

        System.out.print("Enter cell phone number (+27...): ");
        String cellNumber = input.nextLine();

        Registration reg = new Registration(username, password, cellNumber);
        String registrationResult = reg.registerUser();
        System.out.println(registrationResult);

        // Only allow login if registration succeeded
        if (registrationResult.equals("Username, password, and cell phone successfully captured.")) {
            System.out.println("\nLogin ");
            System.out.print("Enter username: ");
            String loginUsername = input.nextLine();

            System.out.print("Enter password: ");
            String loginPassword = input.nextLine();

            String loginStatus = reg.returnLoginStatus(loginUsername, loginPassword);
            System.out.println(loginStatus);

            // Only show the menu if the login was successful
            if (loginStatus.startsWith("Welcome")) {
                
                System.out.println("\nWelcome to QuickChat.");
                System.out.print("How many messages do you wish to enter? ");
                int numMessages = getValidInt(input);
                
                boolean running = true;

                // 4. Application runs until user selects 'quit'
                while (running) {
                    // Part 3: Added the "Stored Messages" option
                    System.out.println("\n--- Menu ---");
                    System.out.println("1) Send Messages");
                    System.out.println("2) Show recently sent messages");
                    System.out.println("3) Stored Messages ");
                    System.out.println("4) Quit");
                    System.out.print("Choose an option: ");
                    
                    int choice = getValidInt(input);

                    switch (choice) {
                        case 1:
                            sendMessagesFlow(input, numMessages);
                            break;
                        case 2:
                            System.out.println("Coming Soon.");
                            break;
                        case 3:
                            storedMessagesMenu(input);
                            break;
                        case 4:
                            running = false;
                            System.out.println("Exiting QuickChat. Goodbye!");
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
                    }
                }
            }

        } else {
            System.out.println("\nRegistration failed. Please fix your details before trying to log in.");
        }

        input.close();
    } 

 
    private static void sendMessagesFlow(Scanner input, int numMessages) {
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Entering Message " + (i + 1) + " of " + numMessages + " ---");

            String recipient;
            // Re-ask loop for Recipient
            while (true) {
                System.out.print("Enter recipient number (+27...): ");
                recipient = input.nextLine().trim();
                // Create a temp message to use the validation method
                Message temp = new Message(i, recipient, "test");
                if (temp.checkRecipientCell().equals("Cell phone number successfully captured.")) {
                    System.out.println(temp.checkRecipientCell());
                    break; // Exit loop if valid
                }
                System.out.println(temp.checkRecipientCell() + " Please try again.");
            }

            String text;
            // Re-ask loop for Message Length
            while (true) {
                System.out.print("Enter message: ");
                text = input.nextLine().trim();
                Message temp = new Message(i, recipient, text);
                if (temp.checkMessageLength().equals("Message ready to send.")) {
                    break; // Exit loop if valid
                }
                System.out.println(temp.checkMessageLength() + " Please try again.");
            }

            // Instantiate the actual Message object
            Message msg = new Message(i, recipient, text);
            
            System.out.println("\nChoose an action:");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message");
            System.out.print("Choice: ");
            int actionChoice = getValidInt(input);
            
            String actionString = "";
            if (actionChoice == 1) {
                actionString = "Send";
            } else if (actionChoice == 2) {
                actionString = "Discard";
            } else if (actionChoice == 3) {
                actionString = "Store";
            }
            
            // Call the method and print the result (This now prints ID, Hash, etc.)
            System.out.println(msg.sentMessage(actionString));
        }
        
       
        System.out.println("\n----------------------------------------");
        System.out.println("Total messages successfully sent: " + Message.returnTotalMessages());
        System.out.println("----------------------------------------");
    }

   
    private static void storedMessagesMenu(Scanner input) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("a) Display sender and recipient");
            System.out.println("b) Display longest stored message");
            System.out.println("c) Search by Message ID");
            System.out.println("d) Search by recipient");
            System.out.println("e) Delete by hash");
            System.out.println("g) Read and display JSON file"); 
            System.out.println("h) Back to Main Menu");           
            System.out.print("Choose: ");
            String opt = input.nextLine().trim().toLowerCase();

            switch (opt) {
                case "a":
                    System.out.println(Message.displayStoredSenderRecipient());
                    break;
                case "b":
                    System.out.println(Message.displayLongestStoredMessage());
                    break;
                case "c":
                    System.out.print("Enter Message ID: ");
                    System.out.println(Message.searchByMessageID(input.nextLine().trim()));
                    break;
                case "d":
                    System.out.print("Enter recipient: ");
                    System.out.println(Message.searchByRecipient(input.nextLine().trim()));
                    break;
                case "e":
                    System.out.print("Enter message hash: ");
                    System.out.println(Message.deleteByHash(input.nextLine().trim()));
                    break;
                case "f":
                    System.out.println(Message.displayReport());
                    break;
                    
                case "g":
                    Message.readAndDisplayJson();
                    break;
                              
                case "h":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Helper method to prevent Scanner crashes if a user types a letter instead of a number
    private static int getValidInt(Scanner input) {
        while (true) {
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}