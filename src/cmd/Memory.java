package cmd;

import server.JobList;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Memory {
    private ReentrantLock memory = new ReentrantLock();
    private int availableMemory;
    private int totalMemory;


    public Memory(int totalMemory){
        this.totalMemory=totalMemory;
        this.availableMemory=totalMemory;
    }




    public void updateMem(int mem) {
        memory.lock();
        try {
            this.availableMemory += mem;
            if(mem > 0){
                JobList.freeCond();
            }
        } finally {
            memory.unlock();
        }
    }

    public int getAvailableMemory(){
        try{
            memory.lock();
            return this.availableMemory;
        }
        finally {
            memory.unlock();
        }

    }
    public int getTotalMemory(){
        return this.totalMemory;
    }
}
