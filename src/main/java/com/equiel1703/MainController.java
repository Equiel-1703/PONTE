package com.equiel1703;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

  // Must match fx:id="statusLabel" in main-view.fxml
  @FXML
  private Label statusLabel;

  private int refreshCount = 0;

  // Called automatically after FXML fields are injected
  @FXML
  public void initialize() {
    String javaVer = System.getProperty("java.version");
    String fxVer = System.getProperty("javafx.version");
    statusLabel.setText("Running JavaFX " + fxVer + " on Java " + javaVer);
  }

  // Must match onAction="#onRefreshClick" in main-view.fxml
  @FXML
  private void onRefreshClick(ActionEvent event) {
    refreshCount++;
    statusLabel.setText("Refreshed " + refreshCount + " time(s)!");
  }
}
