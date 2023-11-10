package cliente;

import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MyClientSocket {
    int portNumber;
    Socket cliente;

    public MyClientSocket(int portNumber) {
        this.portNumber = portNumber;
    }

    public void initCliente() throws Exception{
        cliente = new Socket("127.0.0.1",portNumber);
    }

    public void sendMessage(String msg) throws Exception{
        System.out.println("Sending message to server...");
        PrintWriter writer = new PrintWriter(cliente.getOutputStream(),true);
        writer.println(msg);
    }

    public void close(){
        if(cliente != null){
            try{
                cliente.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }



}
