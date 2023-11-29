package server;

import java.net.Socket;
import java.util.List;

public class JobManager implements Runnable {
    private Server server;
    private Socket socket;
    private JobList jobList;

    public JobManager(Server server, Socket socket,JobList jobList){
        this.server = server;
        this.socket = socket;
        this.jobList = jobList;
    }
    @Override
    public void run() {
        while (true){
            jobList.isEmpty();
            Job c = jobList.removeJob(this.server.getAvailableMemory());
            Thread exec = new Thread(new JobExecute(this.socket, c, server));
            exec.start();
        }
    }
}
