package cliente;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import cmd.Connection;
import cmd.Message;

public class Demultiplexer implements AutoCloseable{
    private Connection conn;
    private final Map<Integer, Obj> map = new HashMap<>();
    private ReentrantLock lock = new ReentrantLock();
    private IOException excpt = null;

    private class Obj {
        int waiters = 0;
        Queue<Message> queue = new ArrayDeque<>();
        Condition c = lock.newCondition();

        public Obj(){}
    }

    private Obj get (int tag){
        return this.map.get(tag);
    }

    public Demultiplexer(Connection conn) {
        this.conn = conn;
    }

    public void start(){
        new Thread(() -> {
            try {
                while (true) {
                    Message frame = conn.receiveMessage();
                    try {
                        lock.lock();
                        Obj e = this.map.get(frame.getTag());
                        if(e==null){
                            e = new Obj();
                            this.map.put(frame.getTag(), e);
                        }
                        e.queue.add(frame);
                        e.c.signalAll();
                    }
                    finally {
                        lock.unlock();
                    }
                }
            } catch (IOException e){
                lock.lock();
                excpt = e;
                map.forEach((k,v)->v.c.signalAll());
            } finally{
                lock.unlock();
            }
        }).start();
    }

    public void sendMessage(Message frame) throws IOException {
        conn.sendMessage(frame);
    }

    public Message receiveMessage(int tag) throws InterruptedException, IOException {
        lock.lock();
        try {
            Obj e = this.get(tag);
            if (e == null) {
                e = new Obj();
                this.map.put(tag, e);
            }
            e.waiters++;
            while (true) {
                if(!e.queue.isEmpty()){
                    e.waiters--;
                    Message reply = e.queue.poll();
                    if(e.waiters == 0 && e.queue.isEmpty())
                        this.map.remove(tag);
                    return reply;
                }
                if(excpt != null){
                    throw excpt;
                }
                e.c.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void close() throws Exception {
        conn.close();
    }


}
