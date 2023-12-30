package cliente;

import cmd.Connection;
import cmd.Message;

import java.io.IOException;

public class Task5 implements Runnable{
    private Demultiplexer con;
    private int tag;

    public Task5(Demultiplexer con, int tag){
        this.con = con;
        this.tag = tag;
    }

    @Override
    public void run() {
        try {
            con.sendMessage(new Message((byte) 12, tag));
            Message rcvd = con.receiveMessage(tag);
            byte[] asd = rcvd.getData();
            System.out.println("[Task 5]--[Pedido: " + tag + "]:");
            System.out.println("Size of Queue: " + asd[0] + "\nMemory Available: " + rcvd.getNum());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
