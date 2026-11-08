package com.equiel1703.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class NetInterfaceController {
  @FXML 
  private ImageView NetIntIcon;

  @FXML 
  private Label NetIntName;
  
  @FXML 
  private Text NetIntIP;

  @FXML 
  private Text NetIntMask;

  public void setInterfaceData(String name, String IP, String mask) {
    this.NetIntName.setText(name);
    this.NetIntIP.setText(IP);
    this.NetIntMask.setText(mask);
  }
}
