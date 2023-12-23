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
    private int tag = 0;

    public Client(){
        this.loggedIn = false;
    }

    public void connection() throws IOException, InterruptedException {
        MenuView view = new MenuView();
        Socket socket = new Socket("127.0.0.1", 1234);
        System.out.println("Connected to the server at 127.0.0.1:1234");
        Demultiplexer con = new Demultiplexer(new Connection(socket));
        con.start();
        boolean loop = true;
        while(loop){
            // LOG IN
            if(!this.loggedIn){
                int sign = view.logIn();
                switch(sign){
                    case 1:
                        do{
                            String dados = view.authenticate();
                            con.sendMessage(new Message(dados.getBytes(), (byte) 1, 0, tag));
                            Message rcvd = con.receiveMessage(this.tag);
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
                            con.sendMessage(new Message(dados.getBytes(), (byte) 2, 0, tag));
                            Message rcvd = con.receiveMessage(this.tag);
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
                        Thread exe = new Thread(new Task4(path, mem, con, ++this.tag));
                        exe.start();
                        break;
                    case (5):
                        Thread exe2 = new Thread(new Task5(con, ++this.tag));
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
        Demultiplexer con = new Demultiplexer(new Connection(socket));
        con.start();

        Thread exe = new Thread(new Task4(path,mem,con,++this.tag));
        exe.start();
    }


}
