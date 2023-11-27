package server;

import cmd.Connection;
import cmd.Message;
import sd23.JobFunction;
import sd23.JobFunctionException;

import java.io.IOException;
import java.net.Socket;

public class JobExecute implements Runnable{
    private Connection con;
    private Job job;
    private Server server;

    public JobExecute(Connection socket, Job job, Server server){
        this.job = job;
        this.con = socket;
        this.server = server;
    }
    @Override
    public void run() {
        try {
            byte[] output = JobFunction.execute(this.job.getBytes());
            server.updateMem(this.job.getMemoria());
            System.err.println("success, returned " + output.length + " bytes");


            con.sendMessage(new Message(output, (byte) 8, 0));
            System.out.println("enviei mensagem");
        } catch (JobFunctionException e) {
            System.err.println("job failed: code="+e.getCode()+" message="+e.getMessage());
            try {
                con.sendMessage(new Message((byte) 9));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
