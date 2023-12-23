package server;

import cmd.Connection;

import java.net.UnknownHostException;

public class SSdata {
    Memory mem;
    Connection con;

    public SSdata(int mem,Connection con) throws UnknownHostException {
        this.mem = new Memory(mem);
        this.con = con;
    }

    public Connection getCon(){
        return this.con;
    }

    public int getAvailableMem(){
        return this.mem.getAvailableMemory();
    }

    public Memory getMem(){
        return this.mem;
    }
}
