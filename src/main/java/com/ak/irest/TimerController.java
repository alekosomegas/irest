package com.ak.irest;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.*;

public class TimerController {
    public Stage primaryStage;
    public void setApp(Stage stage) {
        this.primaryStage = stage;
    }
    public boolean timerRunning = false;
    public String currentTime;
    public String currentTimeAtStart;
    public Timer timer = new Timer();

    public List<String> timerSchedule = Arrays.asList(
            "Work", "Short Break", "Work","Short Break", "Work", "Long Break");
    public int currentStateIndex = 0;

    @FXML private Button btn_settings, btn_reset;
    @FXML private ToggleButton btn_pause;
    @FXML private TextField txt_timer;
    @FXML private ProgressBar progressBar;

    private Label lbl_quote;
    private Pane p_quote;
    private Button btn_snooze, btn_rest;


    private Scene settingsScene;
    public void setSettingsScene(Scene scene) {
        settingsScene = scene;
    }

    double totalTimeInSeconds = 0;

    private Scene breakScene;
    public void setBreakScene(Scene scene) {
        breakScene = scene;
    }

    @FXML
    private void onBtnSettingsClicked(ActionEvent actionEvent) {
        cancelTimer();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = (bounds.getWidth() - settingsScene.getWidth()) / 2;
        double y = (bounds.getHeight() - settingsScene.getHeight()) / 2;

        primaryStage.setX(x);
        primaryStage.setY(y);

        primaryStage.setScene(settingsScene);
    }
    @FXML
    private void onBtnResetClicked() {

        currentTime = currentTimeAtStart;
        progressBar.setProgress(0);

        if (btn_reset.getText().equals("START")) {
            btn_reset.setText("Reset");
            runTimer();
        } else {
            txt_timer.setText(currentTime);
            btn_reset.setText("START");
            timerRunning = false;
        }
    }

    @FXML
    private void onBtnPauseClicked() {
        if (btn_pause.isSelected()) {
            btn_pause.setText("Resume");
            timerRunning = false;
        } else {
            btn_pause.setText("Pause");
            runTimer();
        }
    }

    public void runTimer() {
        txt_timer.setText(currentTime);
        timerRunning = true;
        waitASecAndDecrementTime();
    }

    public void decrementTime() throws InterruptedException {
        if (timerRunning && !Objects.equals(currentTime, "00 : 00")) {
            ArrayList<Integer> time = getCurrentTime();
            int minutes = time.get(0);
            int seconds = time.get(1);

            double increment = 1 / totalTimeInSeconds;

            progressBar.setProgress(progressBar.getProgress() + increment);

            if (seconds > 0) {seconds--;}
            else {
                seconds = 59;
                minutes--;
            }
            String stSeconds = Integer.toString(seconds);
            String stMinutes = Integer.toString(minutes);
            if (seconds < 10) {stSeconds = "0" + seconds;}
            if (minutes < 10) {stMinutes = "0" + minutes;}

            currentTime = stMinutes + " : " + stSeconds;
            txt_timer.setText(currentTime);

            waitASecAndDecrementTime();
        }
        else if (timerRunning && Objects.equals(currentTime, "00 : 00")) {
            System.out.println("Break");


            breakScene.setFill(Color.TRANSPARENT);


            loadBackGround();

            primaryStage.setScene(breakScene);

            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

            primaryStage.setWidth(bounds.getWidth() * 0.9);
            primaryStage.setHeight(bounds.getHeight() * 0.9);

            double x = (bounds.getWidth() - primaryStage.getWidth()) / 2;
            double y = (bounds.getHeight() - primaryStage.getHeight()) / 2;

            primaryStage.setX(x);
            primaryStage.setY(y);

            p_quote = iRestApplication.breakController.p_quote;
            p_quote.setLayoutX(bounds.getWidth()/2 - p_quote.getWidth());
            p_quote.setLayoutY(bounds.getHeight()/2 - p_quote.getHeight());



            progressBar.setProgress(0);
            currentStateIndex++;
            currentStateIndex = currentStateIndex % 6;
            String nextUp = timerSchedule.get(currentStateIndex);
            System.out.println(nextUp);

            if (nextUp.equals("Short Break")) {
                currentTime = "00 : 02";

            } else if (nextUp.equals("Long Break")) {
                currentTime = "00 : 04";
            } else if (nextUp.equals("Work")) {
                currentTime = "00 : 02";
            }
            waitASecAndDecrementTime();

        }
    }

    public void cancelTimer() {
        timerRunning = false;
    }

    public void waitASecAndDecrementTime() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater( () -> {
                    try {
                        decrementTime();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }}, 1000);
    }

    public ArrayList<Integer> getCurrentTime() {
        String[] time = currentTime.split(" : ");
        int minutes, seconds;
        minutes = Integer.parseInt(time[0]);
        seconds = Integer.parseInt(time[1]);
        ArrayList<Integer> returned = new ArrayList<>();
        returned.add(minutes);
        returned.add(seconds);

        if (totalTimeInSeconds == 0) {
             totalTimeInSeconds = minutes * 60 + seconds;
        }

        return returned;
    }

    public void loadBackGround() {
        Random random = new Random();
        int rand = random.nextInt(5 - 1 + 1) + 1;
        String image = iRestApplication.class.getResource("/YosemitePics/" + rand + ".jpg").toExternalForm();
        breakScene.getRoot().setStyle("-fx-background-image: url('" + image + "'); " +
                "-fx-background-position: center center; " +
                "-fx-background-repeat: stretch;");
    }
}
