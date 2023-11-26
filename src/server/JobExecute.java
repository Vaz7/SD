package server;

import cmd.Connection;
import cmd.Message;
import sd23.JobFunction;
import sd23.JobFunctionException;

import java.io.IOException;
import java.net.Socket;

public class JobExecute implements Runnable{
    private Socket clientSocket;
    private Job job;
    private Server server;

    public JobExecute(Socket socket, Job job, Server server){
        this.job = job;
        this.clientSocket = socket;
        this.server = server;
    }
    @Override
    public void run() {
        Connection con = null;
        try {
            con = new Connection(clientSocket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            byte[] output = JobFunction.execute(this.job.getBytes());
            server.updateMem(-job.getMemoria());
            System.err.println("success, returned " + output.length + " bytes");


            con.sendMessage(new Message(output, (byte) 8, 0));
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
