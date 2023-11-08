package com.example.gomoku_rmi;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Server_Data implements Interface {

    List<Integer> board;
    int players;
    int turn;
    int size;

    public Server_Data() {
        this.players = 0;
        this.board = new ArrayList<>();
        this.size = 19;

        for (int i = 0; i < this.size * this.size; i++) {
            this.board.add(0);
        }
        System.out.println(board);

        this.turn = 1;
    }

    @Override
    public int addPlayer() throws RemoteException {
        players++;
        return players;
    }

    @Override
    public List<Integer> getField() throws RemoteException {
        return this.board;
    }

    void checkDiagonals() {
        for (int i = 0; i < this.size - 5; i++) {
            int xNum = 0;
            int oNum = 0;
            int j, col;
            for (j = i, col = 0; j < this.size && col < this.size; j++, col++) {
                if (board.get(j * this.size + col) == 1) {
                    xNum++;
                    if (xNum >= 5) {
                        turn = 3;
                        return;
                    }
                } else {
                    xNum = 0;
                }
                if (board.get(j * this.size + col) == 2) {
                    oNum++;
                    if (oNum >= 5) {
                        turn = 4;
                        return;
                    }
                } else {
                    oNum = 0;
                }
            }
        }

        for (int i = 1; i < this.size - 5; i++) {
            int xNum = 0;
            int oNum = 0;
            int j, col;
            for (j = 0, col = i; j < this.size && col < this.size; j++, col++) {
                if (board.get(j * this.size + col) == 1) {
                    xNum++;
                    if (xNum >= 5) {
                        turn = 3;
                        return;
                    }
                } else {
                    xNum = 0;
                }
                if (board.get(j * this.size + col) == 2) {
                    oNum++;
                    if (oNum >= 5) {
                        turn = 4;
                        return;
                    }
                } else {
                    oNum = 0;
                }
            }
        }
    }

    void checkRows() {
        for (int i = 0; i < this.size - 5; i++) {
            int xNum = 0;
            int oNum = 0;
            for (int j = 0; j < this.size; j++) {
                if (board.get(i * this.size + j) == 1) {
                    xNum++;
                    if (xNum >= 5) {
                        turn = 3;
                        return;
                    }
                } else {
                    xNum = 0;
                }
                if (board.get(i * this.size + j) == 2) {
                    oNum++;
                    if (oNum >= 5) {
                        turn = 4;
                        return;
                    }
                } else {
                    oNum = 0;
                }
            }
        }
    }

    void checkColumns() {
        for (int j = 0; j < this.size - 5; j++) {
            int xNum = 0;
            int oNum = 0;
            for (int i = 0; i < this.size; i++) {
                if (board.get(i * this.size + j) == 1) {
                    xNum++;
                    if (xNum >= 5) {
                        turn = 3;
                        return;
                    }
                } else {
                    xNum = 0;
                }
                if (board.get(i * this.size + j) == 2) {
                    oNum++;
                    if (oNum >= 5) {
                        turn = 4;
                        return;
                    }
                } else {
                    oNum = 0;
                }
            }
        }
    }
    void checkWin() {

        checkDiagonals();
        checkRows();
        checkColumns();
    }

    @Override
    public void move(int x, int y) throws RemoteException {
        if (turn == 3 || turn == 4) return;
        board.set(this.size * x + y, turn == 1 ? 1 : 2);
        this.turn = this.turn == 1 ? 2 : 1;
        checkWin();
    }

    @Override
    public int getTurn() throws RemoteException {
        return this.turn;
    }
}
