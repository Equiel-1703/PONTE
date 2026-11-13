package com.equiel1703.ponte.controllers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.equiel1703.ponte.exceptions.NoInterfaceSelectedException;
import com.equiel1703.ponte.listeners.OnOffButtonListener;
import com.equiel1703.ponte.listeners.ServerThreadManager;
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
  @FXML
  private HBox NetworkInterfacesHBox;
  @FXML
  private Button OnOffButton;
  private static final PseudoClass ON_PSEUDO_CLASS = PseudoClass.getPseudoClass("on");

  private OnOffButtonListener onOffButtonListener;
  private ServerThreadManager serverThreadManager;
  
  private List<NetInterfaceController> onScreenNetInterfaceControllers = new LinkedList<>();

  // Called automatically after FXML injection completes
  @FXML
  public void initialize() {
    this.onOffButtonListener = new OnOffButtonListener(OnOffButton, ON_PSEUDO_CLASS);
    this.serverThreadManager = new ServerThreadManager();

    AppState appState = AppState.getInstance();
    appState
        .getOnOffProperty()
        .addListener(this.onOffButtonListener);
    appState
        .getOnOffProperty()
        .addListener(this.serverThreadManager);
  }

  // Called when the user clicks the "Scan Interfaces" button
  public void onScanClick(ActionEvent ev) {
    // I don't like the idea of scanning new interfaces with the server on, so I'm
    // turning it off first
    AppState appState = AppState.getInstance();
    appState.turnOff();

    // Deselect the active interface
    appState.clearActiveInterface();

    // Clear HBox
    NetworkInterfacesHBox.getChildren().clear();

    // Dispose controllers and clear their LinkedList
    onScreenNetInterfaceControllers.forEach(NetInterfaceController::dispose);
    onScreenNetInterfaceControllers.clear();

    System.out.println("[MainGuiController] Scanning Network Interfaces...");
    List<NetworkIF> listNetInt = NetUtils.getNetworkInterfaces();

    for (NetworkIF n : listNetInt) {
      System.out.println(NetUtils.getFormattedInfo(n));
      System.out.println("-----");

      try {
        FXMLLoader netInterfaceUILoader = new FXMLLoader(FXMLS.NET_INTERFACE_ELEMENT.getUrl());

        Node netInterfaceUIRootNode = netInterfaceUILoader.load();
        NetInterfaceController netInterfaceUIController = netInterfaceUILoader.getController();

        netInterfaceUIController.setInterfaceData(n);
        onScreenNetInterfaceControllers.add(netInterfaceUIController);

        NetworkInterfacesHBox.getChildren().add(netInterfaceUIRootNode);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    System.out.println("[MainGuiController] - Scanning Done -");
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
    } catch (NoInterfaceSelectedException e) {
      System.out.println(e.getMessage());

      StyledAlert alert = new StyledAlert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("No Network Interface Selected");
      alert.setContentText(e.getMessage());

      alert.showAndWait();
    }

    System.out.println("[MainGuiController] PONTE is " + (appState.isActive() ? "ON" : "OFF"));
  }
}
