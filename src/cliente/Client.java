package cliente;

import cmd.Connection;
import cmd.MenuView;
import cmd.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client {
    private boolean loggedIn;

    public Client(){
        this.loggedIn = false;
    }

    public void connection() throws IOException {
        MenuView view = new MenuView();
        Socket socket = new Socket("127.0.0.1", 1234);
        System.out.println("Connected to the server at 127.0.0.1:1234");
        Connection con = new Connection(socket);
        boolean loop = true;
        while(loop){
            // LOG IN
            if(!this.loggedIn){
                int sign = view.logIn();
                switch(sign){
                    case 1:
                        do{
                            String dados = view.authenticate();
                            con.sendMessage(new Message(dados.getBytes(), (byte) 1, 0));
                            Message rcvd = con.receiveMessage();
                            if(rcvd.getMsg() == (byte) 5){
                                this.loggedIn = true;
                                System.out.println("Authentication successful. Welcome! ");
                            } else if (rcvd.getMsg() == (byte) 4){
                                System.out.println("Authentication failed. Invalid username or password.");
                                break;
                            }
                        } while(!loggedIn);
                        break;
                    case 2:
                        boolean register = false;
                        do{
                            String dados = view.authenticate();
                            con.sendMessage(new Message(dados.getBytes(), (byte) 2, 0));
                            Message rcvd = con.receiveMessage();
                            if(rcvd.getMsg() == (byte) 7){
                                System.out.println("Registration successful. Welcome!");
                                register = true;
                            } else if (rcvd.getMsg() == (byte) 6){
                                System.out.println("Registration failed. The username may already be taken.");
                                break;
                            }
                        }while(!register);
                        break;
                    case 0:
                        System.out.println("Exiting the program. Goodbye!");
                        loop = false;
                        System.exit(0);
                        break;
                }
            }

            else{
                int choice = view.loggedOptions();
                switch(choice){
                    case (4):
                        String path = view.getFile();
                        int mem = view.getMemory();
                        Thread exe = new Thread(new Task4(path, mem, con));
                        exe.start();
                        break;
                    case (5):
                        Thread exe2 = new Thread(new Task5(con));
                        exe2.start();
                        break;
                    case 0:
                        System.out.println("Exiting the program. Goodbye!");
                        loop = false;
                        System.exit(0);
                        break;
                }
            }
        }
    }

    public void connectionTest(String path, int mem) throws IOException {
        Socket socket = new Socket("127.0.0.1", 1234);
        System.out.println("Connected to the server at 127.0.0.1:1234");
        Connection con = new Connection(socket);

        try{
            byte[] fileContent = Files.readAllBytes(Paths.get(path));
            con.sendMessage(new Message(fileContent, (byte) 3, mem));

            Message rcvd = con.receiveMessage();

            // meter a interpretar as mensagens que cria a partir do (byte)
            // dicionario na classe Mensagem



            if(rcvd.getMsg() == (byte) 8) {
                byte[] data = rcvd.getData();
                //se quisermos guardar o output num ficheiro zipado para testar na defesa
                //writeOutputToFile(data);
                System.out.println(new String(data));
            }


            else if (rcvd.getMsg() == (byte) 9)
                System.out.println("The task failed. Try again...");

            //Thread.sleep(10000);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void writeOutputToFile(byte[] output) throws IOException {
        // Generate a timestamp for the filename
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timestamp = now.format(formatter);

        String fileName = "respostas" + File.separator + "output_" + timestamp + ".7z";
        // Create the file if it doesn't exist
        File file = new File(fileName);
        if (!file.exists()) {
            boolean created = file.createNewFile();
            if (!created) {
                return;
            }
        }
        // Write the output to the file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(output);
        }


    }
}
