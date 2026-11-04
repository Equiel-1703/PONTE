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
    // Loads /com/equiel1703/main-view.fxml from the classpath/JAR
    FXMLLoader loader = new FXMLLoader(PONTE.class.getResource("main-view.fxml"));
    Parent root = loader.load();

    Scene scene = new Scene(root, 640, 480);
    stage.setTitle("PONTE");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
