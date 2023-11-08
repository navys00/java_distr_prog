package com.example.gomoku_rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Interface extends Remote {

    int addPlayer() throws RemoteException;
    void move(int x, int y) throws RemoteException;
    List<Integer> getField() throws RemoteException;
    int getTurn() throws RemoteException;
}