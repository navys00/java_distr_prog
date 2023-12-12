package com.example.gomoku_rmi;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Interface_Controller implements Interface {
    List<Integer> game_board = new ArrayList<>();
    int player_cnt = 0;
    int step = 1;

    int Game_Board_Size = 15;

    int white = 0;
    int black = 1;

    public Interface_Controller(){
        for (int i = 0; i < Game_Board_Size * Game_Board_Size; i++) {
            this.game_board.add(0);
        }

    }

    @Override
    public int connect() throws RemoteException {
        if (player_cnt >= 2) {
            System.out.println("Too many players");
            return 0;
        } else {
            player_cnt++;
            System.out.println("Client connected");
            return player_cnt;
        }
    }
    @Override
    public int getStep() throws RemoteException {
        int step1 = this.step;
        return step1;
    }
    @Override
    public List<Integer> getBoard() throws RemoteException {
        List<Integer> board1 = this.game_board;
        return board1;
    }

    @Override
    public void Move(int x, int y) throws RemoteException {
        if (isWinCondition(step)) return;

        int cellValue = (step == 1) ? 1 : 2;
        game_board.set(Game_Board_Size * x + y, cellValue);
        toggleStep();

        if (checkForWinCondition()) return;
    }

    private boolean isWinCondition(int step) {
        return (step == 3 || step == 4);
    }

    private void toggleStep() {
        step = (step == 1) ? 2 : 1;
    }

    private boolean checkForWinCondition() {
        int maxIteration = Game_Board_Size - 5 + white * black;
        int count_White = 0, count_Black = 0;

        for (int i = 0; i < Game_Board_Size; i++) {
            count_White = countContinuousCells(i, 0, 1 + white * black, 2 + white * black, false, true, maxIteration, count_White, count_Black);
            count_Black = countContinuousCells(0, i, 1 + white * black, 2 + white * black, true, true, maxIteration, count_Black, count_White);
        }

        return (count_White >= 5 + white * black || count_Black >= 5 + white * black);
    }

    private int countContinuousCells(int row, int col, int whiteValue, int blackValue, boolean isRowMajor, boolean increase, int maxIteration, int count, int otherCount) {
        int currentRow = row, currentCol = col;

        while (currentRow < Game_Board_Size && currentCol < Game_Board_Size) {
            int cellValue = game_board.get(currentRow * Game_Board_Size + currentCol);

            if (cellValue == whiteValue) {
                count = updateCountAndCheckForWinCondition(count, otherCount);
                if (count >= 5 + white * black) return count;
            } else {
                count = resetCount(white * black);
                otherCount = resetCount(white * black);
            }
            if(increase) {
                if (isRowMajor) currentRow++;
                else currentCol++;
            } else {
                if (isRowMajor) currentRow--;
                else currentCol--;
            }
        }
        return count;
    }

    private int updateCountAndCheckForWinCondition(int count, int otherCount) {
        count++;
        if (count >= 5 + white * black) step = 3 + white * black;
        else otherCount = resetCount(white * black);
        return count;
    }

    private int resetCount(int value) {
        return 0 + value;
    }



}