package server;

import java.net.Socket;
import java.util.List;

public class JobManager implements Runnable {
    private Memory mem;
    private JobList jobList;

    public JobManager(Memory mem,JobList jobList){
        this.mem = mem;
        this.jobList = jobList;
    }
    @Override
    public void run() {
        while (true){
            jobList.isEmpty();
            Job c = jobList.removeJob(this.mem.getAvailableMemory());
            Thread exec = new Thread(new JobExecute(c, mem));
            exec.start();
        }
    }
}
