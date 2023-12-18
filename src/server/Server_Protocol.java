package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server_Protocol {

    private Server server;

    public Server_Protocol() {
        this.server = new Server();
    }

    public JobList jobList = new JobList();
    private Memory mem = new Memory();

    public void initServer() {
        try{
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server is running on port 1234");
            Thread jobmanager = new Thread(new JobManager(mem,jobList));
            jobmanager.start();

            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client is connected " + clientSocket.getInetAddress());

                // criar thread a seguir
                Thread clientThread = new Thread(new ClientHandler(clientSocket, server,jobList));
                clientThread.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
