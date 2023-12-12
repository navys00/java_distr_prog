package com.example.gomoku_rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException {

        Interface_Controller server = new Interface_Controller();
        Registry registry = LocateRegistry.createRegistry(3000);

        Remote stub = UnicastRemoteObject.exportObject(server, 0);
        registry.rebind("server.board", stub);

        Thread.sleep(Integer.MAX_VALUE);
    }
}