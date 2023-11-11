package cmd;

import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MenuView {

    private Scanner sc;

    public MenuView(){
        this.sc = new Scanner(System.in);
    }

    public int leInteiro(){
        boolean loop = false;
        int option = 0;
        do{
            try{
                option = Integer.parseInt(sc.nextLine());
                loop = true;
            } catch (Exception e){
                if(sc.hasNext()) sc.nextLine();
                System.out.println("Please provide an integer!");
            }
        } while(!loop);

        return option;
    }

    public int logIn(){
        System.out.println("1. Authenticate");
        System.out.println("2. Register");
        System.out.println("0. Exit");

        int option = 0;
        do{
            option = leInteiro();
            if(option < 0 | option > 2)
                System.out.println("Write a value between 0 and 2");
        } while(option < 0 | option > 2);

        return option;
    }

    public int loggedOptions(){
        System.out.println("4. Enter File Name");
        System.out.println("0. Exit");

        int option = 0;
        do{
            option = leInteiro();
            if(option != 0 && option != 4)
                System.out.println("Write the value 0 or 4!");
        } while(option != 0 && option != 4 && option != 5);

        return option;
    }

    public String authenticate(){
        System.out.println("Warning: In case any blank space is written, the system will remove it!");
        System.out.println("Enter your username: ");
        String authUsername = sc.nextLine().replaceAll(" ", "");
        System.out.println("Enter your password: ");
        String authPassword = sc.nextLine().replaceAll(" ", "");

        return authUsername + " " + authPassword;
    }

    public String getFile(){
        System.out.println("Enter the file path: ");
        boolean pathExists = false;
        String fileName = null;
        do{
            fileName = sc.nextLine();
            Path path = Paths.get(fileName);
            pathExists = Files.exists(path);
            if(!pathExists){
                System.out.println("Please provide an existing path!");
            } else {
                System.out.println("The system will execute your file once it gets to its turn!");
            }
        } while(!pathExists);

        return fileName;
    }

    public int getMemory(){
        System.out.println("Enter the amount of memory this program needs in MB!");
        int mem = leInteiro();
        return mem;
    }
}
