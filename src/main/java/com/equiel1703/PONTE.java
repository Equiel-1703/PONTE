package com.equiel1703;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PONTE extends Application {
  private final double WINDOW_WIDTH = 800;
  private final double WINDOW_HEIGHT = 820;

  private void loadFonts() {
    Font VT323_regular = Font.loadFont(
      getClass().getResourceAsStream("fonts/VT323-Regular.ttf"),
      24
    );

    if (VT323_regular != null) {
      System.out.println("Loaded font family: '" + VT323_regular.getFamily() + "'");
    }
  }

  @Override
  public void start(Stage stage) throws IOException {
    loadFonts();

    Parent root = FXMLLoader.load(getClass().getResource("main-gui.fxml"));
    Scene scene = new Scene(root);

    // Adding stylesheet to main GUI
    scene.getStylesheets().add(getClass().getResource("main-gui.css").toExternalForm());

    stage.setWidth(WINDOW_WIDTH);
    stage.setHeight(WINDOW_HEIGHT);

    stage.setResizable(false);

    stage.setTitle("PONTE");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
