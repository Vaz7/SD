package cliente;

import cmd.Connection;
import cmd.MenuView;
import cmd.Message;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

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
                    case 4:
                        String path = view.getFile();
                        int mem = view.getMemory();
                        try{
                            byte[] fileContent = Files.readAllBytes(Paths.get(path));
                            con.sendMessage(new Message(fileContent, (byte) 3, mem));

                            Message rcvd = con.receiveMessage();
                            // @TODO
                            // meter a interpretar as mensagens que cria a partir do (byte)
                            // dicionario na classe Mensagem
                            System.out.println(new String(rcvd.getData()));
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

    public void connectionTest(String path, int mem) throws IOException {
        Socket socket = new Socket("127.0.0.1", 1234);
        System.out.println("Connected to the server at 127.0.0.1:1234");
        Connection con = new Connection(socket);

        try{
            byte[] fileContent = Files.readAllBytes(Paths.get(path));
            con.sendMessage(new Message(fileContent, (byte) 3, mem));

            Message rcvd = con.receiveMessage();
            // @TODO
            // meter a interpretar as mensagens que cria a partir do (byte)
            // dicionario na classe Mensagem

            System.out.println(new String(rcvd.getData()));
            Thread.sleep(10000);
        } catch(IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
