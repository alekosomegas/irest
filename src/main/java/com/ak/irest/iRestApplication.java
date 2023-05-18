package com.ak.irest;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class iRestApplication extends Application {
    static BreakController breakController;
    static Stage primaryStage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader settingsPaneLoader = new FXMLLoader(iRestApplication.class.getResource("setting-view.fxml"));
        FXMLLoader timerPaneLoader = new FXMLLoader(iRestApplication.class.getResource("timer-view.fxml"));
        FXMLLoader breakPaneLoader = new FXMLLoader(iRestApplication.class.getResource("break-view.fxml"));

        Scene settingsScene = new Scene(settingsPaneLoader.load(), 385, 250);
        Scene timerScene = new Scene(timerPaneLoader.load());
        Scene breakScene = new Scene(breakPaneLoader.load());

        // injecting second scene into the controller of the first scene
        SettingsController settingsController = settingsPaneLoader.getController();
        settingsController.setTimerScene(timerScene);

        // injecting second scene into the controller of the first scene
        TimerController timerController = timerPaneLoader.getController();
        timerController.setSettingsScene(settingsScene);
        timerController.setBreakScene(breakScene);

        BreakController lbreakController = breakPaneLoader.getController();
        breakController = lbreakController;
        primaryStage = stage;

        settingsController.setTimerContreller(timerController);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Hello!");
        stage.setScene(settingsScene);
        stage.show();


        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = (bounds.getWidth() - settingsScene.getWidth()) / 2;
        double y = (bounds.getHeight() - settingsScene.getHeight()) / 2;

        stage.setX(x);
        stage.setY(y);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(1);
            }
        });

        timerController.setApp(stage);

        settingsController.p_settings.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                settingsController.onBtnStartClicked();
            }
        } );
    }

    public static void main(String[] args) {
        launch();
    }
}