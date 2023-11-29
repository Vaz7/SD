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
    private int availableMemory = 500000;
    private ReentrantLock memory = new ReentrantLock();

    /**
     * para usar condition quando a memória leva update
     */
    private ReentrantLock l = new ReentrantLock();
    private Condition canExecute = l.newCondition();

    public Server(){
        this.userInfo = new HashMap<>();
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

    public int getAvailableMemory(){
        return this.availableMemory;
    }

}
