package com.example.gomoku_rmi;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client extends Application {

    private static final int Game_Board_Size = 15;
    static List<Integer> localBoard = new ArrayList<>();
    static int playerCount = 0;
    static int step = 0;
    private static final String UNIQUE_BINDING_NAME = "server.board";
    private static final int PORT = 3000;

    @Override
    public void start(Stage stage) throws IOException, NotBoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 50 * Game_Board_Size, 50 * Game_Board_Size);

        final Registry registry = LocateRegistry.getRegistry(PORT);
        Interface board = (Interface) registry.lookup(UNIQUE_BINDING_NAME);
        playerCount = board.connect();
        if (playerCount == 0) return;
        stage.setTitle("Client " + playerCount);
        stage.setScene(scene);
        stage.show();

        VBox vBox = (VBox) scene.lookup("#vBox");
        vBox.setAlignment(Pos.CENTER);
        ObservableList<Node> hBoxes = vBox.getChildren();


        for (int x = 0; x < Game_Board_Size; x++) {
            HBox hBox = new HBox();
            hBoxes.add(hBox);
            hBox.setSpacing(0.5);
            hBox.setAlignment(Pos.CENTER);
            for (int y = 0; y < Game_Board_Size; y++) {
                Rectangle rectangle = new Rectangle(48, 48);
                rectangle.setFill(Color.WHITE);
                rectangle.setStroke(Color.BLACK);
                rectangle.setCursor(Cursor.HAND);
                rectangle.setId("rect" + (x * Game_Board_Size + y));
                int finalX = x;
                int finalY = y;
                rectangle.setOnMouseClicked(e -> {
                    if (playerCount == step) {
                        try {
                            board.Move(finalX, finalY);
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                hBox.getChildren().add(rectangle);
            }
        }

        new Thread(() -> {
            while(true) {
                try {
                    localBoard = board.getBoard();
                    step = board.getStep();
                    fill(scene);
                    new Thread(() -> Platform.runLater(() -> {
                        if (step == playerCount) {
                            stage.setTitle("Client " + playerCount + " YOUR TURN");
                        } else {
                            stage.setTitle("Client " + playerCount);
                        }
                    })).start();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (step == 3 || step == 4) {
                    new Thread(() -> Platform.runLater(() -> {
                        if (step == 3 && playerCount == 1) {
                            stage.setTitle("YOU WON");
                        }
                        if (step == 4 && playerCount == 2) {
                            stage.setTitle("YOU WON");
                        }
                    })).start();
                    break;
                }
            }
        }).start();
    }

    private void fill(Scene scene) {
        VBox vBox = (VBox) scene.lookup("#vBox");
        ObservableList<Node> hBoxes = vBox.getChildren();

        for (int i = 0; i < Game_Board_Size; i++) {
            HBox hBox = (HBox) hBoxes.get(i);
            for (int j = 0; j < Game_Board_Size; j++) {
                Rectangle rectangle = (Rectangle) hBox.getChildren().get(j);
                if (localBoard.get(i * Game_Board_Size + j) == 1) {
                    rectangle.setFill(Color.BLACK);
                }
                if (localBoard.get(i * Game_Board_Size + j) == 2) {
                    rectangle.setFill(Color.ORANGE);
                }
            }
        }
    }
    public static void main(String[] args) {
        launch();
    }
}