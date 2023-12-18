package server;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Memory {
    private ReentrantLock memory = new ReentrantLock();
    private int availableMemory = 500;
    private Condition canExecute = memory.newCondition();

    public void updateMem(int mem) {
        memory.lock();
        try {
            this.availableMemory += mem;
            if(mem > 0){
                this.canExecute.signalAll();
            }
        } finally {
            memory.unlock();
        }
    }

    public int getAvailableMemory(){
        return this.availableMemory;
    }
}
