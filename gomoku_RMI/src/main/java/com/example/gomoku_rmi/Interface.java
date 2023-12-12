package com.example.gomoku_rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Interface extends Remote {

    List<Integer> getBoard() throws RemoteException;
    void Move(int x, int y) throws RemoteException;
    int connect() throws RemoteException;
    int getStep() throws RemoteException;
}
