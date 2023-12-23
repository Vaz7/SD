package cmd;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Connection implements AutoCloseable {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Lock readl = new ReentrantLock();
    private Lock writel = new ReentrantLock();

    public Connection(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
        this.dis = new DataInputStream(clientSocket.getInputStream());
        this.dos = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void sendMessage(Message msg) throws IOException {
        this.writel.lock();
        try{
            msg.serialize(dos);
            dos.flush();
        } finally {
            this.writel.unlock();
        }
    }

    public void sendInt(int num){
        this.writel.lock();
        try{
            dos.writeInt(num);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.writel.unlock();
        }
    }

    public int readInt(){
        this.readl.lock();
        try{
            return dis.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.readl.unlock();
        }
    }

    public Socket getSocket(){
        return this.socket;
    }

    public Message receiveMessage() throws IOException{
        this.readl.lock();
        try{
            return Message.deserialize(dis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.readl.unlock();
        }
    }

    @Override
    public void close() throws Exception {
        this.socket.close();
    }
}
