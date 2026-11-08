package com.equiel1703.controllers;

import java.io.IOException;
import java.util.List;

import com.equiel1703.net.NetUtils;
import com.equiel1703.urls.FXMLS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import oshi.hardware.NetworkIF;

public class MainGuiController {
  @FXML
  private HBox NetworkInterfacesHBox;

  public void onScanClick(ActionEvent e) {
    System.out.println("Scanning Network Interfaces...");

    List<NetworkIF> listNetInt = NetUtils.getNetworkInterfaces();

    for (NetworkIF n : listNetInt) {
      System.out.println(NetUtils.getFormattedInfo(n));
      System.out.println("-----");

      try {
        FXMLLoader netInterfaceUiLoader = new FXMLLoader(FXMLS.NET_INTERFACE_ELEMENT.getUrl());

        Node netInterfaceRoot = netInterfaceUiLoader.load();
        NetInterfaceController controller = netInterfaceUiLoader.getController();

        controller.setInterfaceData("teste", "10.0.0.1", "255.255.255.0");

        NetworkInterfacesHBox.getChildren().add(netInterfaceRoot);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }

    System.out.println("Scanning Done.");
  }
}
