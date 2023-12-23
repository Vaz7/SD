package server;

import cmd.Connection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

public class JobManager implements Runnable {
    private Memory mem;
    private JobList jobList;
    private SSQueue slaveServers;

    public JobManager(JobList jobList, SSQueue slaveServers){
        this.jobList = jobList;
        this.slaveServers = slaveServers;
    }
    @Override
    public void run() {
        while (true){
            jobList.isEmpty();
            SSdata chosenOne = slaveServers.removeChosen();
            Job c = jobList.removeJob(chosenOne.getMem());
            slaveServers.addServer(chosenOne);
            Thread exec = new Thread(new JobExecute(c,chosenOne.getCon(),chosenOne.getMem()));
            exec.start();
        }
    }
}
