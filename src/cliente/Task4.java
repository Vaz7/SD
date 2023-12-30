package cliente;

import cmd.Connection;
import cmd.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            if(rcvd.getMsg() == (byte) 8){
                int id = con.getConnection().getSocket().getLocalPort();
                writeOutputToFile(rcvd.getData(),id,tag);
                //System.out.println(new String(rcvd.getData()));
            }

            else if (rcvd.getMsg() == (byte) 9)
                System.out.println("The task failed. Try again...");
            else if (rcvd.getMsg() == (byte) 10)
                System.out.println("Job memory is bigger than biggest server memory!");
        } catch(IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeOutputToFile(byte[] output,int id,int tag) throws IOException {

        String fileName = "respostas" + File.separator + "pedido_port_" + id + "_tag_"+  tag + ".7z";
        System.out.println("Output was written to file: " + fileName);
        // Create the file if it doesn't exist
        File file = new File(fileName);
        if (!file.exists()) {
            boolean created = file.createNewFile();
            if (!created) {
                return;
            }
        }
        // Write the output to the file
        try (FileOutputStream fos = new FileOutputStream(file)){
            fos.write(output);
            fos.flush();
        }
    }
}
