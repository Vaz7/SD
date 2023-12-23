package server;

import cmd.Job;
import cmd.Memory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JobList {
    private List<Job> jobQueue = new ArrayList<>();
    private ReentrantReadWriteLock fifo = new ReentrantReadWriteLock();
    private Lock writefifo = fifo.writeLock();
    private Lock readfifo = fifo.readLock();

    private static ReentrantLock l = new ReentrantLock();
    private static Condition canExecute = l.newCondition();
    private ReentrantLock l2 = new ReentrantLock();
    private Condition isEmpty = l2.newCondition();
    public void printQueue(){
        System.out.println("the queue has " + this.jobQueue.size() + " elements!");
    }

    public Job removeJob(Memory mem){

        l.lock();
        Job res;
        try{
            res = getFirst();
            while(res.getMemoria() > mem.getAvailableMemory()){
                canExecute.await();
            }

            mem.updateMem(-res.getMemoria());

            removeFirst();
            return res;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            l.unlock();
        }
    }

    public void addJob(Job j){
        writefifo.lock();
        l2.lock();
        try{
            jobQueue.add(j);
            isEmpty.signalAll();
        } finally {
            writefifo.unlock();
            l2.unlock();
        }
    }
    public void isEmpty(){
        l2.lock();
        try{
            while(this.jobQueue.isEmpty()){
                isEmpty.await();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            l2.unlock();
        }
    }

    public int size(){
        readfifo.lock();
        try{
            return this.jobQueue.size();
        } finally {
            readfifo.unlock();
        }
    }

    public static void freeCond(){
        try {
            l.lock();
            canExecute.signalAll();
        }
        finally {
            l.unlock();
        }
    }

    private void removeFirst(){
        try{
            writefifo.lock();
            this.jobQueue.removeFirst();
        }
        finally {
            writefifo.unlock();
        }
    }
    private Job getFirst(){
        try {
            writefifo.lock();
            return this.jobQueue.getFirst();
        }
        finally {
            writefifo.unlock();
        }
    }
}
