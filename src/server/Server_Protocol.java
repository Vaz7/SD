package server;

import cmd.Connection;

import java.io.IOException;
import java.net.*;
import java.net.spi.InetAddressResolver;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


public class Server_Protocol {

    private Server server;

    public JobList jobList = new JobList();

    SSQueue slaveServers = new SSQueue();
    private List<InetSocketAddress> ipPortaServers = new ArrayList<>();
    private int port;
    String [] args;

    public Server_Protocol(int port,String[]args){
        this.port = port;
        this.server = new Server();
        this.args=args;

    }

    public void populateSS() throws UnknownHostException {
<<<<<<< HEAD
        //InetSocketAddress s1 = new InetSocketAddress(InetAddress.getByName("127.0.0.1"),12345);
        InetSocketAddress s2 = new InetSocketAddress(InetAddress.getByName("127.0.0.1"),12346);

        //ipPortaServers.add(s1);
        ipPortaServers.add(s2);
=======

        for(int i=0;i< args.length;i+=2){
            String ip = args[i];
            int port = Integer.parseInt(args[i+1]);
            InetSocketAddress s = new InetSocketAddress(InetAddress.getByName(ip),port);
            ipPortaServers.add(s);
        }
>>>>>>> refs/remotes/origin/main
    }


    public void startCons(){
        try {
            populateSS();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        for(InetSocketAddress i : ipPortaServers){
            try {
                Socket SSsocket = new Socket(i.getAddress().getHostAddress(),i.getPort());
                Connection con = new Connection(SSsocket);
                int SSmem = con.readInt();
                SSdata ss = new SSdata(SSmem,con);

                slaveServers.addServer(ss);
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
    public void initServer() {
        try{
            startCons();
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("Server is running on port " + this.port);
            Thread jobmanager = new Thread(new JobManager(jobList,slaveServers));
            jobmanager.start();

            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client is connected " + clientSocket.getInetAddress());

                // criar thread a seguir
                Thread clientThread = new Thread(new ClientHandler(clientSocket, server,jobList,slaveServers));
                clientThread.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
