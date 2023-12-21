package server;

import cmd.Connection;
import cmd.Message;
import sd23.JobFunction;
import sd23.JobFunctionException;

import java.io.*;
import java.net.Socket;

public class JobExecute implements Runnable {
    private Job job;
    private Memory mem;

    public JobExecute(Job job, Memory mem) {
        this.job = job;
        this.mem = mem;
    }

    @Override
    public void run() {
        Socket clientSocket = job.getSocket();
        Connection con = null;
        try {
            con = new Connection(clientSocket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            int meem = job.getMemoria();
            byte[] output = JobFunction.execute(this.job.getBytes());
            mem.updateMem(meem);
            System.err.println("success, returned " + output.length + " bytes");


            con.sendMessage(new Message(output, (byte) 8, 0, this.job.getTag()));

        } catch (JobFunctionException e) {
            System.err.println("job failed: code=" + e.getCode() + " message=" + e.getMessage());
            int meem = job.getMemoria();
            mem.updateMem(meem);
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
