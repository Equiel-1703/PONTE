package com.equiel1703.ponte.controllers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.equiel1703.ponte.net.NetUtils;
import com.equiel1703.ponte.singletons.AppState;
import com.equiel1703.ponte.urls.FXMLS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import oshi.hardware.NetworkIF;

public class MainGuiController {
  @FXML
  private HBox NetworkInterfacesHBox;

  private List<NetInterfaceController> activeNetInterfaceControllers = new LinkedList<>();

  // Called when the user clicks the "Scan Interfaces" button
  public void onScanClick(ActionEvent e) {
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

  // Called when the user flips on/off switch
  public void turnOnOff() {
    boolean active = AppState.getInstance().isActive();
    System.out.println("PONTE is " + (active ? "ON" : "OFF"));
  }
}
