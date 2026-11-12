package com.equiel1703.ponte;

import java.io.IOException;

import com.equiel1703.ponte.singletons.AppState;
import com.equiel1703.ponte.urls.FXMLS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PONTE extends Application {
  private final double WINDOW_WIDTH = 800;
  private final double WINDOW_HEIGHT = 820;

  // Load custom fonts from resources folder
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
    // First, we load our custom fonts
    loadFonts();

    // Now we can create the scene from the FXML
    Parent root = FXMLLoader.load(FXMLS.MAIN_GUI.getUrl());
    Scene scene = new Scene(root);

    stage.setWidth(WINDOW_WIDTH);
    stage.setHeight(WINDOW_HEIGHT);

    stage.setResizable(false);

    stage.setTitle("PONTE");
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    // Force the server to stop and clean resources before exit
    AppState.getInstance().turnOff();
    super.stop();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
