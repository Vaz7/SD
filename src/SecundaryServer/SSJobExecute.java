package SecundaryServer;

import cmd.Connection;
import cmd.Message;
import sd23.JobFunction;
import sd23.JobFunctionException;
import server.Job;
import server.Memory;

import java.io.*;
import java.net.Socket;

public class SSJobExecute implements Runnable {
    private Job job;
    private Connection con;

    public SSJobExecute(Job job,Connection con) {
        this.job = job;
        this.con = con;
    }

    @Override
    public void run() {
        try {
            byte[] output = JobFunction.execute(this.job.getBytes());

            System.err.println("success, returned " + output.length + " bytes");

            con.sendMessage(new Message(output, (byte) 8, 0, this.job.getTag()));

        } catch (JobFunctionException e) {
            System.err.println("job failed: code=" + e.getCode() + " message=" + e.getMessage());
            try {
                con.sendMessage(new Message((byte) 9, this.job.getTag()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
