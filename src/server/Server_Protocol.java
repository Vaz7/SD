package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server_Protocol {

    private Server server;

    public JobList jobList = new JobList();
    private Memory mem = new Memory(10000);

    private int port;

    public Server_Protocol(int port){
        this.port = port;
        this.server = new Server();
    }
    public void initServer() {
        try{
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("Server is running on port " + this.port);
            Thread jobmanager = new Thread(new JobManager(mem,jobList));
            jobmanager.start();

            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client is connected " + clientSocket.getInetAddress());

                // criar thread a seguir
                Thread clientThread = new Thread(new ClientHandler(clientSocket, server,jobList, mem));
                clientThread.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
