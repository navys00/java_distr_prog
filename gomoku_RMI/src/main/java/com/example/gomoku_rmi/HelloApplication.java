package com.example.gomoku_rmi;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class HelloApplication extends Application {

    private static final int Game_Place_Size = 15;
    static List<Integer> localBoard = new ArrayList<>();
    static int playerCount = 0;
    static int step = 0;
    public Interface board;

    @Override
    public void start(Stage stage) throws IOException, NotBoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 750);

        final Registry registry = LocateRegistry.getRegistry(3000);
        board = (Interface) registry.lookup("server.board");
        playerCount = board.connect();
        if (playerCount == 0) return;
        stage.setTitle("Client " + playerCount);
        stage.setScene(scene);
        stage.show();

        VBox vBox = (VBox) scene.lookup("#vBox");
        vBox.setAlignment(Pos.CENTER);
        ObservableList<Node> hBoxes = vBox.getChildren();

        IntStream.range(0, Game_Place_Size)
                .forEach(x -> {
                    HBox hBox = new HBox();
                    hBoxes.add(hBox);
                    hBox.setSpacing(0.5);
                    hBox.setAlignment(Pos.CENTER);
                    IntStream.range(0, Game_Place_Size)
                            .forEach(y -> {
                                Rectangle rectangle = createRectangle(x, y);
                                hBox.getChildren().add(rectangle);
                            });
                });


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        updateGameState();
                        fill(scene);
                        updatePlayerTurnTitle(step, playerCount, stage);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        if (CheckGameEnd(step, playerCount, stage)) {
                            break;
                        }
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
    private void updatePlayerTurnTitle(int step, int playerCount, Stage stage) {
        Platform.runLater(() -> {
            if (step == playerCount) {
                stage.setTitle("Client " + playerCount + " YOUR TURN");
            } else {
                stage.setTitle("Client " + playerCount);
            }
        });
    }
    private boolean CheckGameEnd(int step, int playerCount, Stage stage) {
        if (step == 3 || step == 4) {
            Platform.runLater(() -> {
                if (step == 3 && playerCount == 1) {
                    stage.setTitle("YOU WON");
                }
                if (step == 4 && playerCount == 2) {
                    stage.setTitle("YOU WON");
                }
            });
            return true;
        }
        return false;
    }
    private void updateGameState() throws RemoteException {
        localBoard = board.getBoard();
        step = board.getStep();
    }
    private void fill(Scene scene) {
        VBox vBox = (VBox) scene.lookup("#vBox");
        ObservableList<Node> hBoxes = vBox.getChildren();

        for (int i = 0; i < Game_Place_Size; i++) {
            HBox hBox = (HBox) hBoxes.get(i);
            for (int j = 0; j < Game_Place_Size; j++) {
                Rectangle rectangle = (Rectangle) hBox.getChildren().get(j);
                int index = i * Game_Place_Size + j;
                setColorRectangle(rectangle,getValueFromLocalBoard(index));
            }
        }
    }
    private int getValueFromLocalBoard(int index) {
        return localBoard.get(index);
    }
    private void setColorRectangle(Rectangle rectangle, int value){
        if (value == 1) {
            rectangle.setFill(Color.BLACK);
        } else if (value == 2) {
            rectangle.setFill(Color.ORANGE);
        }
    }
    private Rectangle createRectangle(int x, int y) {
        Rectangle rectangle = createLabeledRectangle();
        setProperties(rectangle, x, y);
        attachClickHandler(rectangle, x, y);
        return rectangle;
    }

    private Rectangle createLabeledRectangle() {
        return new Rectangle(48, 48);
    }

    private void setProperties(Rectangle rectangle, int x, int y) {
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);
        rectangle.setCursor(Cursor.HAND);
        rectangle.setId("rect" + (x * Game_Place_Size + y));
    }

    private void attachClickHandler(Rectangle rectangle, int x, int y) {
        rectangle.setOnMouseClicked(e -> handleRectangleClick(x, y));
    }
    // Метод для обработки нажатия на прямоугольник
    private void handleRectangleClick(int x, int y) {
        if (playerCount == step) {
            try {
                board.Move(x, y);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public static void main(String[] args) {
        launch();
    }
}