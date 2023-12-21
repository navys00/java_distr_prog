package com.example.gomoku_rmi;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
@WebService
public interface Interface{
    @WebMethod
    List<Integer> getBoard();
    @WebMethod
    void Move(int x, int y);
    @WebMethod
    int connect();
    @WebMethod
    int getStep();

    @WebMethod
    public String SayHello(String name);
}
