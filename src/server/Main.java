package server;

public class Main {
    public static void main(String[] args) throws Exception {
        Server_Protocol servidor = new Server_Protocol(1234);
        servidor.initServer();
    }
}
