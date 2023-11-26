package server;

import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
    private Map<String, byte[]> userInfo;
    private ReentrantReadWriteLock catalogo = new ReentrantReadWriteLock();
    private Lock writel = catalogo.writeLock();
    private Lock readl = catalogo.readLock();

    /**
     * para escrever e ler da queue
     */
    private ReentrantReadWriteLock fifo = new ReentrantReadWriteLock();
    private Lock writefifo = fifo.writeLock();
    private Lock readfifo = fifo.readLock();

    private List<Job> jobQueue = new ArrayList<>();
    private int availableMemory = 500;
    private ReentrantLock memory = new ReentrantLock();

    /**
     * para usar condition quando a memória leva update
     */
    private ReentrantLock l = new ReentrantLock();
    private Condition isEmpty = l.newCondition();
    private Condition canExecute = l.newCondition();

    public Server(){
        this.userInfo = new HashMap<>();
    }


    public List<Job> getJobQueue() {
        this.readfifo.lock();
        try{
            return jobQueue;
        } finally {
            this.readfifo.unlock();
        }
    }

    public Job getFirstElement(){
        this.readfifo.lock();
        try{
            return jobQueue.get(0);
        } finally {
            this.readfifo.unlock();
        }
    }

    /**
     * Método que adiciona user à hash
     * @param id
     * @param pass
     */
    public void addUser(String id, byte[] pass){
        try{
            writel.lock();
            this.userInfo.put(id, pass);
        } finally {
            writel.unlock();
        }
    }

    /**
     * Método que devolve a password de um dado ID para verificar se corresponde
     * @param id
     * @return
     */
    public boolean isPassword(String id, byte[] pass){
        try{
            this.readl.lock();
            byte[] array = this.userInfo.get(id);
            return Arrays.equals(array, pass);
        } finally{
            this.readl.unlock();
        }
    }

    /**
     * Método que verifica se um ID está registado.
     * @param id
     * @return
     */
    public boolean containsID(String id){
        try{
            readl.lock();
            if(this.userInfo.containsKey(id)) return true;
            else return false;
        } finally {
            readl.unlock();
        }
    }

    public void addJob(Job j){
        writefifo.lock();
        l.lock();
        try{
            jobQueue.add(j);
            System.out.println("aqui 1");
            isEmpty.signalAll();
        } finally {
            writefifo.unlock();
            l.unlock();
        }
    }

    public void printQueue(){
        System.out.println("the queue has " + this.jobQueue.size() + " elements!");
    }

    public void removeJob(Job j){
        writefifo.lock();
        l.lock();
        try{
            while(j.getMemoria() > this.availableMemory){
                canExecute.await();
            }
            jobQueue.remove(j);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            writefifo.unlock();
            l.unlock();
        }
    }

    public void updateMem(int mem) {
        memory.lock();
        l.lock();
        try {
            this.availableMemory += mem;
            if(mem > 0){
                canExecute.signalAll();
            }
        } finally {
            memory.unlock();
            l.unlock();
        }
    }

    public void isEmpty(){
        l.lock();
        try{
            List<Job> a = getJobQueue();
            while(a.isEmpty()){
                isEmpty.await();
                System.out.println("aqui 5");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            l.unlock();
        }
    }
}
