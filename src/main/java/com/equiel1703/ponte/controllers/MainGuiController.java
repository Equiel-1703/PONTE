package com.equiel1703.ponte.controllers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.equiel1703.ponte.exceptions.NoInterfaceSelected;

import com.equiel1703.ponte.net.NetUtils;
import com.equiel1703.ponte.singletons.AppState;
import com.equiel1703.ponte.urls.FXMLS;
import com.equiel1703.ponte.utils.StyledAlert;

import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;

import oshi.hardware.NetworkIF;

public class MainGuiController {
  private static final PseudoClass ON_PSEUDO_CLASS = PseudoClass.getPseudoClass("on");

  @FXML
  private HBox NetworkInterfacesHBox;
  @FXML
  private Button OnOffButton;

  private List<NetInterfaceController> activeNetInterfaceControllers = new LinkedList<>();

  // Called automatically after FXML injection completes
  @FXML
  public void initialize() {
    AppState
        .getInstance()
        .getOnOffProperty()
        .addListener(
            (observable, oldValue, newValue) -> this.OnOffButton.pseudoClassStateChanged(ON_PSEUDO_CLASS, newValue.booleanValue()));
  }

  // Called when the user clicks the "Scan Interfaces" button
  public void onScanClick(ActionEvent e) {
    AppState appState = AppState.getInstance();

    if (appState.isActive()) {
      appState.turnOff();
    }
    
    // When we rescan, it's best to deselect the active interface and turn PONTE off
    // for stability sake
    appState.setActiveInterface(null);
    NetworkInterfacesHBox.getChildren().clear();
    activeNetInterfaceControllers.forEach(NetInterfaceController::dispose);

    System.out.println("Scanning Network Interfaces...");
    List<NetworkIF> listNetInt = NetUtils.getNetworkInterfaces();

    for (NetworkIF n : listNetInt) {
      System.out.println(NetUtils.getFormattedInfo(n));
      System.out.println("-----");

      try {
        FXMLLoader netInterfaceUILoader = new FXMLLoader(FXMLS.NET_INTERFACE_ELEMENT.getUrl());

        Node netInterfaceUIRootNode = netInterfaceUILoader.load();
        NetInterfaceController netInterfaceController = netInterfaceUILoader.getController();

        netInterfaceController.setInterfaceData(n);
        activeNetInterfaceControllers.add(netInterfaceController);

        NetworkInterfacesHBox.getChildren().add(netInterfaceUIRootNode);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }

    System.out.println("- Scanning Done -");
  }

  // Called when the user flips the on/off switch
  public void turnOnOff() {
    AppState appState = AppState.getInstance();

    try {
      if (appState.isActive()) {
        appState.turnOff();
      } else {
        appState.turnOn();
      }
    } catch (NoInterfaceSelected e) {
      System.out.println(e.getMessage());

      StyledAlert alert = new StyledAlert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("No Network Interface Selected");
      alert.setContentText(e.getMessage());

      alert.showAndWait();
    }

    boolean active = appState.isActive();
    System.out.println("PONTE is " + (active ? "ON" : "OFF"));
  }
}
