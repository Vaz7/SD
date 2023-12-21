package cliente;

import cmd.Connection;
import cmd.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Task4 implements Runnable{
    private String path;
    private int mem;
    private Demultiplexer con;
    private int tag;

    public Task4(String path, int mem, Demultiplexer con, int tag) {
        this.path = path;
        this.mem = mem;
        this.con = con;
        this.tag = tag;
    }

    @Override
    public void run() {
        try{
            byte[] fileContent = Files.readAllBytes(Paths.get(path));
            con.sendMessage(new Message(fileContent, (byte) 3, mem, tag));

            Message rcvd = con.receiveMessage(tag);
            // @TODO
            // meter a interpretar as mensagens que cria a partir do (byte)
            // dicionario na classe Mensagem
            System.out.println("[Task 4]--[Pedido: " + tag + "]:");
            if(rcvd.getMsg() == (byte) 8)
                System.out.println(new String(rcvd.getData()));
            else if (rcvd.getMsg() == (byte) 9)
                System.out.println("The task failed. Try again...");
        } catch(IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
