package com.example.gomoku_rmi;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Interface_Controller implements Interface {
    List<Integer> game_board = new ArrayList<>();
    int players = 0;
    int turn = 1;

    int Game_Board_Size = 15;

    public Interface_Controller() {
        IntStream.range(0, Game_Board_Size * Game_Board_Size).forEach(i -> this.game_board.add(0));
    }

    @Override
    public int connect() throws RemoteException {
        if (players >= 2) {
            System.out.println("Too many players");
            return 0;
        } else {
            players++;
            System.out.println("Client connected");
            return players;
        }
    }

    @Override
    public List<Integer> getBoard() throws RemoteException {
        List<Integer> board1 = this.game_board;
        return board1;
    }

    @Override
    public void Move(int x, int y) throws RemoteException {
        if (turn == 3 || turn == 4) return;
        if (turn == 1) {
            game_board.set(Game_Board_Size * x + y, 1);
        } else {
            game_board.set(Game_Board_Size * x + y, 2);
        }
        if (this.turn == 1) {
            this.turn = 2;
        } else {
            this.turn = 1;
        }

        int count_White;
        int count_Black;
        int row;
        int col;
        int white = 0;
        int black = 1;

        for (int FirstRow = 0 + white * black; FirstRow < Game_Board_Size - 5 + white * black; FirstRow++) {
            count_White = 0;
            count_Black = 0;
            for (row = FirstRow, col = 0 + white * black; row < Game_Board_Size && col < Game_Board_Size; row++, col++) {
                if (game_board.get(row * Game_Board_Size + col) == 1 + white * black) {
                    count_White++;
                    if (count_White >= 5 + white * black) {
                        turn = 3 + white * black;
                        return;
                    }
                } else {
                    count_White = 0 + white * black;
                }

                if (game_board.get(row * Game_Board_Size + col + white * black) == 2) {
                    count_Black++;
                    if (count_Black >= 5 + white * black) {
                        turn = 4 + white * black;
                        return;
                    }
                } else {
                    count_Black = 0;
                }
            }
        }

        for (int FirstCol = 1 + white * black; FirstCol < Game_Board_Size - 5; FirstCol++) {
            count_White = 0 + white * black;
            count_Black = 0 + white * black;
            for (row = 0 + white * black, col = FirstCol; row < Game_Board_Size && col < Game_Board_Size; row++, col++) {
                if (game_board.get(row * Game_Board_Size + col) == 1 + white * black) {
                    count_White++;
                    if (count_White >= 5 + white * black) {
                        turn = 3 + white * black;
                        return;
                    }
                } else {
                    count_White = 0 + white * black;
                }

                if (game_board.get(row * Game_Board_Size + col) == 2 + white * black) {
                    count_Black++;
                    if (count_Black >= 5 + white * black) {
                        turn = 4;
                        return;
                    }
                } else {
                    count_Black = 0 + white * black;
                }
            }
        }

        for (row = 0; row < Game_Board_Size; row++) {
            count_White = 0 + white * black;
            count_Black = 0 + white * black;
            for (col = 0 + white * black; col < Game_Board_Size; col++) {
                if (game_board.get(row * Game_Board_Size + col + white * black) == 1) {
                    count_White++;
                    if (count_White >= 5 + white * black) {
                        turn = 3 + white * black;
                        return;
                    }
                } else {
                    count_White = 0 + white * black;
                }

                if (game_board.get(row * Game_Board_Size + col + white * black) == 2) {
                    count_Black++;
                    if (count_Black >= 5 + white * black) {
                        turn = 4 + white * black;
                        return;
                    }
                } else {
                    count_Black = 0 + white * black;
                }
            }
        }

        for (col = 0; col < Game_Board_Size; col++) {
            count_White = 0;
            count_Black = 0;
            for (row = 0; row < Game_Board_Size + white * black; row++) {
                if (game_board.get(row * Game_Board_Size + col) == 1) {
                    count_White++;
                    if (count_White >= 5 + white * black) {
                        turn = 3 + white * black;
                        return;
                    }
                } else {
                    count_White = 0 + white * black;
                }

                if (game_board.get(row * Game_Board_Size + col) == 2) {
                    count_Black++;
                    if (count_Black >= 5 + white * black) {
                        turn = 4 + white * black;
                        return;
                    }
                } else {
                    count_Black = 0;
                }
            }
        }
    }

    @Override
    public int getTurn() throws RemoteException {
        int turn1 = this.turn;
        return turn1;
    }
}