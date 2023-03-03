/****************************************************
 * Assignment: Assignment 8 Pt.2 - RaceTrack
 * Description: this program emulates a race between
 *      three race cars by utilizing javaFx
 * Name: Jamarr B.
 * Course: CMSC403
 ***************************************************/

package com.example.demo1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import static java.lang.Thread.sleep;

public class RaceTrack extends Application {
    private static Thread threadOne;
    private static Thread threadTwo;
    private static Thread threadThree;
    private static ImageView carOne;
    private static ImageView carTwo;
    private static ImageView carThree;
    private static Button start;
    private static Button pause;
    private static Button reset;
    private final Random rand = new Random();
    private static final String path = "racecar2.png";
    private boolean go;


    private void buttons(Pane button) {
        start = new Button("Start");
        pause = new Button("Pause");
        reset = new Button("Reset");

        start.setMaxWidth(100);
        start.setMaxHeight(50);

        pause.setMaxWidth(100);
        pause.setMaxHeight(50);

        reset.setMaxWidth(100);
        reset.setMaxHeight(50);

        button.getChildren().addAll(start, pause, reset);

        start.relocate(125, 15);
        pause.relocate(225, 15);
        reset.relocate(325, 15);

        start.setOnAction(this::handle);

        pause.setOnAction((event) -> {
            if(!go && threadOne != null) {
                if(threadTwo != null) {
                    if(threadThree != null) {
                        return;
                    }
                }
            }
            go = false;
        });

        reset.setOnAction((event) -> {
            go = false;

            carOne.setX(0);
            carTwo.setX(0);
            carThree.setX(0);
        });

    }

    private void raceCars(Pane car) throws FileNotFoundException {
        Image raceCar1 = new Image(new FileInputStream(path));
        Image raceCar2 = new Image(new FileInputStream(path));
        Image raceCar3 = new Image(new FileInputStream(path));

        carOne = new ImageView(raceCar1);
        carTwo = new ImageView(raceCar2);
        carThree = new ImageView(raceCar3);

        carOne.relocate(25, 70);
        carTwo.relocate(25, 120);
        carThree.relocate(25, 170);

        car.getChildren().addAll(carOne, carTwo, carThree);

    }

    private void raceTrack(Pane track) {
        Rectangle rect1 = new Rectangle(50, 70, 400, 15);
        Rectangle rect2 = new Rectangle(50, 120, 400, 15);
        Rectangle rect3 = new Rectangle(50, 170, 400, 15);

        rect1.setFill(javafx.scene.paint.Color.GRAY);
        rect2.setFill(javafx.scene.paint.Color.GRAY);
        rect3.setFill(javafx.scene.paint.Color.GRAY);

        track.getChildren().addAll(rect1, rect2, rect3);

    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Race Track");
        stage.setResizable(false);

        Pane raceTrackPane = new Pane();
        raceTrack(raceTrackPane);

        try {
            raceCars(raceTrackPane);
        } catch( FileNotFoundException e ) {
            e.printStackTrace();
        }

        buttons(raceTrackPane);

        Scene window = new Scene(raceTrackPane, 500, 200);
        stage.setScene(window);
        stage.show();
    }

    private void alert(String winner) {
        Alert a = new Alert(AlertType.INFORMATION);
        a.setContentText("Car " + winner + " Wins!");
        a.show();
    }

    private void handle(javafx.event.ActionEvent event) {
        if(go) {
            return;
        }


        go = true;
        javafx.concurrent.Task<Void> task1 = new javafx.concurrent.Task<>() {
            @Override
            protected Void call() {
                do {
                    javafx.application.Platform.runLater(() -> carOne.setX(carOne.getX() + rand.nextInt(10)));

                    try {
                        sleep(50);
                    } catch( InterruptedException e ) {
                        break;
                    }

                    if(carOne.getX() > 375) {
                        go = false;
                        javafx.application.Platform.runLater(() -> alert("One"));
                        break;
                    }
                } while(go);

                return null;
            }
        };

        javafx.concurrent.Task<Void> task2 = new javafx.concurrent.Task<>() {
            @Override
            protected Void call() {
                do {
                    javafx.application.Platform.runLater(() -> carTwo.setX(carTwo.getX() + rand.nextInt(10)));

                    try {
                        sleep(50);
                    } catch( InterruptedException e ) {
                        break;
                    }

                    if(carTwo.getX() > 375) {
                        go = false;
                        javafx.application.Platform.runLater(() -> alert("Two"));
                        break;
                    }
                } while(go);

                return null;
            }
        };

        javafx.concurrent.Task<Void> task3 = new javafx.concurrent.Task<>() {
            @Override
            protected Void call() {
                do {javafx.application.Platform.runLater(() -> carThree.setX(carThree.getX() + rand.nextInt(10)));

                    try {
                        sleep(50);
                    } catch( InterruptedException e ) {
                        break;
                    }
                    if(carThree.getX() > 375) {
                        go = false;
                        javafx.application.Platform.runLater(() -> alert("Three"));
                        break;
                    }


                } while(go);

                return null;
            }
        };

        threadOne = new Thread(task1);
        threadTwo = new Thread(task2);
        threadThree = new Thread(task3);

        threadOne.setDaemon(true);
        threadTwo.setDaemon(true);
        threadThree.setDaemon(true);

        threadOne.start();
        threadTwo.start();
        threadThree.start();
    }

    public static void main(String[] args) {
        launch();
    }
}
