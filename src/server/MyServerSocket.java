package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStreamReader;


public class MyServerSocket {

    private ServerSocket server;

    private int portNumber;

    public MyServerSocket(int portNumber) {
        this.portNumber = portNumber;
    }

    public void initServer() throws Exception{

        server = new ServerSocket(portNumber);
        System.out.println("Server is running on port " + portNumber);

        while(true){
            Socket clientSocket = server.accept();
            System.out.println("Client is connected");
            readMessageFromSocket(clientSocket);
        }
    }

    private void readMessageFromSocket(Socket client) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

        // Read lines until there are no more lines to read
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    public void close(){
        if(server != null){
            try{
                server.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
