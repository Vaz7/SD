package cliente;

import java.util.Scanner;



public class Menu {
    private static String loggedInUsername = null;

    public static void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {

            if (loggedInUsername == null) {
                // Display options for unauthenticated users
                System.out.println("1. Authenticate");
                System.out.println("2. Register");
                System.out.println("3. Exit");
            } else {
                // Display options for authenticated users
                System.out.println("4. Enter File Name");
                System.out.println("5. Logout");
            }

            System.out.print("Select an option: ");
            int choice = Integer.parseInt(scanner.nextLine());


            if (loggedInUsername == null) {
                // Handle options for unauthenticated users
                switch (choice) {
                    case 1:
                        System.out.print("Enter your username: ");
                        String authUsername = scanner.nextLine();
                        System.out.print("Enter your password: ");
                        String authPassword = scanner.nextLine();
                        // Add authentication logic here (check if username and password are valid)
                        if (authenticate(authUsername, authPassword)) {
                            loggedInUsername = authUsername;
                            System.out.println("Authentication successful. Welcome, " + loggedInUsername + "!");
                        } else {
                            System.out.println("Authentication failed. Invalid username or password.");
                        }
                        break;
                    case 2:
                        System.out.print("Enter your desired username: ");
                        String regUsername = scanner.nextLine();
                        System.out.print("Enter your desired password: ");
                        String regPassword = scanner.nextLine();
                        // Add registration logic here (create a new user account)
                        if (register(regUsername, regPassword)) {
                            loggedInUsername = regUsername;
                            System.out.println("Registration successful. Welcome, " + loggedInUsername + "!");
                        } else {
                            System.out.println("Registration failed. The username may already be taken.");
                        }
                        break;
                    case 3:
                        System.out.println("Exiting the program. Goodbye!");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option. Please choose a valid option.");
                }
            } else {
                // Handle options for authenticated users
                switch (choice) {
                    case 4:
                        System.out.print("Enter the file name: ");
                        String fileName = scanner.nextLine();
                        // Add your file handling logic here, based on the fileName provided.
                        SendMessages.sendCode(fileName);
                        break;
                    case 5:
                        loggedInUsername = null; // Logout the user
                        System.out.println("Logout successful. You are now unauthenticated.");
                        break;
                    default:
                        System.out.println("Invalid option. Please choose a valid option.");
                }
            }
        }
    }

    private static boolean authenticate(String username, String password) {
        return SendMessages.sendAuthenticationRequest(username,password);
    }

    private static boolean register(String username, String password) {
        return SendMessages.sendRegistationRequest(username,password);
    }
}
