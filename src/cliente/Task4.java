package cliente;

import cmd.Connection;
import cmd.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Task4 implements Runnable{
    private String path;
    private int mem;
    private Connection con;

    public Task4(String path, int mem, Connection con) {
        this.path = path;
        this.mem = mem;
        this.con = con;
    }

    @Override
    public void run() {
        try{
            byte[] fileContent = Files.readAllBytes(Paths.get(path));
            con.sendMessage(new Message(fileContent, (byte) 3, mem));

            Message rcvd = con.receiveMessage();
            // @TODO
            // meter a interpretar as mensagens que cria a partir do (byte)
            // dicionario na classe Mensagem
            if(rcvd.getMsg() == (byte) 8)
                System.out.println(new String(rcvd.getData()));
            else if (rcvd.getMsg() == (byte) 9)
                System.out.println("The task failed. Try again...");
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
