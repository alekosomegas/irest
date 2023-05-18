package com.ak.irest;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsController {
    @FXML private TimerController timerController;
    public void setTimerContreller(TimerController contreller) {
        timerController = contreller;
        txt_workTimer.textProperty().addListener(((observable, oldValue, newValue)->{
            checkTimerInput(oldValue, newValue);
        }));
        txt_ShortRestTimer.textProperty().addListener(((observable, oldValue, newValue)->{
            checkTimerInput(oldValue, newValue);
        }));
        txt_LongRestTimer.textProperty().addListener(((observable, oldValue, newValue)->{
            checkTimerInput(oldValue, newValue);
        }));
    }

    @FXML Pane p_settings;
    @FXML
    public TextField txt_workTimer, txt_ShortRestTimer, txt_LongRestTimer;
    @FXML
    private Button btn_exit, btn_start;
    @FXML
    public void onBtnExitClicked(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        Platform.exit();
        System.exit(1);
    }

    private Scene timerScene;
    public void setTimerScene(Scene scene) {
        timerScene = scene;
    }

    Pattern pattern = Pattern.compile("\\d\\d : [0-5]\\d", Pattern.CASE_INSENSITIVE);

    @FXML
    public void onBtnStartClicked() {
        //Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Stage primaryStage = iRestApplication.primaryStage;
        primaryStage.setScene(timerScene);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = 0;
        double y = bounds.getHeight() - timerScene.getHeight();

        primaryStage.setX(x);
        primaryStage.setY(y);

        timerController.currentTime = txt_workTimer.getText();
        timerController.currentTimeAtStart = txt_workTimer.getText();
        timerController.runTimer();
    }


    public void checkTimerInput(String old, String newV) {
        Matcher matcher = pattern.matcher(newV);
        boolean matchFound = matcher.find();
        if(matchFound && newV.length() == 7) {
            System.out.println("Match found");
            return;
        } else {
            System.out.println("Match not found");
            txt_workTimer.setText(old);
        }
    }
}