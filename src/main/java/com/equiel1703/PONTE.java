package com.equiel1703;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PONTE extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("main-gui.fxml"));
    Scene scene = new Scene(root);

    // Adding stylesheet to main GUI
    scene.getStylesheets().add(getClass().getResource("main-gui.css").toExternalForm());

    stage.setWidth(640);
    stage.setHeight(600);

    stage.setResizable(false);

    stage.setTitle("PONTE");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
