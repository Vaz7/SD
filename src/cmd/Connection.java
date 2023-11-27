package cmd;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Connection {
    private DataInputStream dis;
    private DataOutputStream dos;
    private Lock readl = new ReentrantLock();
    private Lock writel = new ReentrantLock();

    public Connection(Socket clientSocket) throws IOException {
        System.out.println(clientSocket);
        this.dis = new DataInputStream(clientSocket.getInputStream());
        this.dos = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void sendMessage(Message msg) throws IOException {
        this.writel.lock();
        try{
            msg.serialize(dos);
            dos.flush();
            System.out.println("partiu ----- ");
        } finally {
            this.writel.unlock();
        }
    }

    public Message receiveMessage(){
        this.readl.lock();
        try{
            return Message.deserialize(dis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.readl.unlock();
        }
    }
}
