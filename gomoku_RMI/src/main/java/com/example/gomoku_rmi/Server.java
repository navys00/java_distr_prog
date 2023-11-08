package com.example.gomoku_rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server
{
    public static final String UNIQUE_BINDING_NAME = "server.Interface";

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException {

        final Server_Data server = new Server_Data();

        final Registry registry = LocateRegistry.createRegistry(2732);

        Remote stub = UnicastRemoteObject.exportObject(server, 0);
        registry.bind(UNIQUE_BINDING_NAME, stub);

        Thread.sleep(Integer.MAX_VALUE);
    }
}