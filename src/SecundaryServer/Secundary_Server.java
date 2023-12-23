package SecundaryServer;

import cmd.Connection;
import cmd.Job;
import cmd.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Secundary_Server {
    private int mem = 14;
    private int port;

    public Secundary_Server(int port){
        this.port = port;
    }

    public void initServer() {
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Secundary server is running on port " + port);

            Socket clientSocket = serverSocket.accept();
            Connection con = new Connection(clientSocket);
            con.sendInt(mem);
            System.out.println("Client is connected " + clientSocket.getInetAddress());

            while(true){
                // criar thread a seguir
                Message recieved = con.receiveMessage();
                Job job = new Job(recieved.getData(),recieved.getNum(),clientSocket,recieved.getTag());

                Thread t = new Thread(new SSJobExecute(job,con));
                t.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
