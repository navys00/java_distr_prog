package com.example.gomoku_rmi;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException {

        final Registry registry = LocateRegistry.getRegistry(2732);
        final String UNIQUE_BINDING_NAME = "server.Interface";
        final Client_Data data = new Client_Data();

        Interface gameInterface = (Interface) registry.lookup(UNIQUE_BINDING_NAME);
        data.playerCount = gameInterface.addPlayer();

        System.out.println("Игрок #" + data.playerCount);

        Thread updateThread = new Thread(() -> {
            while(!data.exit) {
                try {
                    data.field = gameInterface.getField();
                    data.turn = gameInterface.getTurn();

                    if (checkWin(data) != 0) break;
                    Thread.sleep(100);
                } catch (RemoteException e) {
                    data.exit = true;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        updateThread.start();

        Thread gameThread = new Thread(() -> {
            while(!data.exit) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (data.playerCount == data.turn) {
                    printField(data);
                    System.out.println("Ваш ход");

                    try {
                        data.inputPoint = Arrays.asList(data.reader.readLine().split(" "));
                        for (int i = 0; i < data.inputPoint.size(); i++) {
                            data.coord.set(i, Integer.parseInt(data.inputPoint.get(i)));
                        }
                    } catch (IOException ignored) {
                    }

                    if (checkFirstNotCenter(data)) continue;
                    if (checkEmpty(data)) continue;

                    try {
                        gameInterface.move(data.coord.get(0), data.coord.get(1));
                    } catch (RemoteException e) {
                        break;
                    }

                    try {
                        data.field = gameInterface.getField();
                        data.turn = gameInterface.getTurn();
                        printField(data);
                        System.out.println();
                    } catch (RemoteException e) {
                        break;
                    }
                }

                if (printWin(data)) break;
            }
        });
        gameThread.start();
    }

    static boolean checkFirstNotCenter(Client_Data data) {
        if (data.first && data.playerCount == 1) {
            if (data.coord.get(0) != (Integer) data.size / 2 && data.coord.get(1) != (Integer) data.size / 2) {
                return true;
            }
            data.first = false;
        }
        return false;
    }
    static boolean checkEmpty(Client_Data data) {
        return data.field.get(data.coord.get(0) * data.size + data.coord.get(1)) != 0;
    }

    static int checkWin(Client_Data data) {
        if (data.turn == 3) {
            return 1;
        }
        if (data.turn == 4) {
            return 2;
        }
        return 0;
    }
    static boolean printWin(Client_Data data) {
        if (checkWin(data) == 1 && data.playerCount == 1) {
            System.out.println("Вы победили");
            return true;
        }
        if (checkWin(data) == 2 && data.playerCount == 2) {
            System.out.println("Вы победили");
            return true;
        }
        if (checkWin(data) == 1 && data.playerCount == 2) {
            System.out.println("Вы проиграли");
            return true;
        }
        if (checkWin(data) == 2 && data.playerCount == 1) {
            System.out.println("Вы проиграли");
            return true;
        }
        return false;
    }

    public static void printField(Client_Data data) {
        for (int i = 0; i < data.size; i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < data.size; j++) {
                if (data.field.get(data.size * i + j) == 0) {
                    s.append("-");
                }
                if (data.field.get(data.size * i + j) == 1) {
                    s.append("x");
                }
                if (data.field.get(data.size * i + j) == 2) {
                    s.append("o");
                }
                s.append("  ");
            }
            System.out.println(s);
        }
    }
}