package cliente;

import cmd.MenuView;
import cmd.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {
    private boolean loggedIn;

    public Client(){
        this.loggedIn = false;
    }

    public void connection() throws IOException {
        MenuView view = new MenuView();
        Socket socket = new Socket("127.0.0.1", 1234);
        System.out.println("Connected to the server at 127.0.0.1:1234");

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        byte[] receiveBuffer = new byte[4000];
        int bytesRead;
        boolean loop = true;
        while(loop){
            // LOG IN
            if(!this.loggedIn){
                int sign = view.logIn();
                switch(sign){
                    case 1:
                        do{
                            String dados = view.authenticate();
                            byte[] toSend = Message.serializeMessage(new Message(dados.getBytes(), true, (byte) 1, 0));
                            out.write(toSend);
                            out.flush();

                            while ((bytesRead = in.read(receiveBuffer)) != -1){
                                byte[] receivedData = Arrays.copyOf(receiveBuffer, bytesRead);
                                Message msg = (Message.deserializeMessage(receivedData));
                                if(msg.getMsg() == (byte) 5){
                                    this.loggedIn = true;
                                    System.out.println("Authentication successful. Welcome! ");
                                } else if (msg.getMsg() == (byte) 4){
                                    System.out.println("Authentication failed. Invalid username or password.");
                                }
                                break;
                            }
                        } while(!loggedIn);
                        break;
                    case 2:
                        boolean register = false;
                        do{
                            String dados = view.authenticate();
                            byte[] toSend = Message.serializeMessage(new Message(dados.getBytes(), true, (byte) 2, 0));
                            out.write(toSend);
                            out.flush();

                            while ((bytesRead = in.read(receiveBuffer)) != -1){
                                byte[] receivedData = Arrays.copyOf(receiveBuffer, bytesRead);
                                Message msg = (Message.deserializeMessage(receivedData));
                                if(msg.getMsg() == (byte) 7){
                                    System.out.println("Registration successful. Welcome!");
                                    register = true;
                                } else if (msg.getMsg() == (byte) 6){
                                    System.out.println("Registration failed. The username may already be taken.");
                                }
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
                    case 4:
                        String path = view.getFile();
                        int mem = view.getMemory();
                        try{
                            byte[] fileContent = Files.readAllBytes(Paths.get(path));
                            List<Message> tp = (Message.createMessagesFromByteArray(fileContent,(byte) 3, mem));
                            for(Message c: tp){
                                System.out.println(Message.serializeMessage(c).length);
                                out.write(Message.serializeMessage(c));
                                out.flush();

                                byte[] buffer = new byte[1000];
                                in.read(buffer);
                            }

                            List<Message> tmp = new ArrayList<>();
                            while ((bytesRead = in.read(receiveBuffer)) != -1){
                                int offset = bytesRead;
                                for (int i = 0; i < bytesRead; i += 1000) {
                                    int blockSize = Math.min(1000, offset);
                                    byte[] receivedData = Arrays.copyOfRange(receiveBuffer, i, i + blockSize);
                                    Message msg = (Message.deserializeMessage(receivedData));
                                    tmp.add(msg);
                                    offset -= blockSize;
                                }
                                if(tmp.get(tmp.size()-1).isLast()) break;

                            }

                            System.out.println(new String(Message.convertMessagesToByteArray(tmp)));
                        } catch(IOException e){
                            e.printStackTrace();
                        }
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
}
