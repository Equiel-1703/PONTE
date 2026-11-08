package com.equiel1703.controllers;

import com.equiel1703.net.NetUtils;
import com.equiel1703.urls.NetInterfaceIcon;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import oshi.hardware.NetworkIF;

public class NetInterfaceController {
  @FXML
  private ImageView NetIntIcon;

  @FXML
  private Label NetIntName;

  @FXML
  private Text NetIntIP;

  @FXML
  private Text NetIntMask;

  @FXML 
  private Tooltip NetIntTooltip;

  public void setInterfaceData(NetworkIF netInt) {
    Image icon;

    switch (NetUtils.getInterfaceType(netInt)) {
      case NetUtils.InterfaceType.ETHERNET:
        icon = new Image(NetInterfaceIcon.ETHERNET.getUrl().toString());
        break;

      case NetUtils.InterfaceType.WIRELESS:
        icon = new Image(NetInterfaceIcon.WIRELESS.getUrl().toString());
        break;

      default:
        icon = new Image(NetInterfaceIcon.GENERIC.getUrl().toString());
        break;
    }

    this.NetIntIcon.setImage(icon);
    this.NetIntName.setText(netInt.getName());
    this.NetIntIP.setText(netInt.getIPv4addr()[0]);
    this.NetIntMask.setText(NetUtils.cidrToNetmask(netInt.getSubnetMasks()[0]));
    this.NetIntTooltip.setText(netInt.getDisplayName());
  }
}
