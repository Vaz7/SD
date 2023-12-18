package cliente;

import cmd.Connection;
import cmd.Message;

import java.io.IOException;

public class Task5 implements Runnable{
    private Connection con;

    public Task5(Connection con){
        this.con = con;
    }

    @Override
    public void run() {
        try {
            con.sendMessage(new Message((byte) 4));
            Message rcvd = con.receiveMessage();
            byte[] asd = rcvd.getData();
            System.out.println("Size of Queue: " + asd[0] + "\nMemory Available: " + rcvd.getNum());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
