package com.equiel1703dev.ponte.controllers;

import com.equiel1703dev.ponte.listeners.NetInterfaceListener;
import com.equiel1703dev.ponte.net.NetUtils;
import com.equiel1703dev.ponte.singletons.AppState;
import com.equiel1703dev.ponte.urls.NetInterfaceIcon;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import oshi.hardware.NetworkIF;

public class NetInterfaceController {
  // The network interface this node is controlling
  private NetworkIF netInt;
  // The listener for when the active interface is changed
  private NetInterfaceListener netInterfaceListener;

  @FXML
  private HBox NetIntRoot;
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

  // Called by MainGUI when adding an interface to the HBox
  public void setInterfaceData(NetworkIF netInt) {
    this.netInt = netInt;

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

    // Add a listener to the active interface ObjectProperty
    this.netInterfaceListener = new NetInterfaceListener(netInt, NetIntRoot);
    AppState
        .getInstance()
        .getActiveInterfaceProperty()
        .addListener(this.netInterfaceListener);
  }

  // Called when the user clicks in the interface
  public void onClicked(MouseEvent e) {
    System.out.println("[NetInterfaceController] The user clicked on " + this.netInt.getName());

    AppState
        .getInstance()
        .setActiveInterface(this.netInt);
  }

  // Called by the MainGUI when we want to get rid of this controller.
  // We have to remote the listener from the AppState active interface property so
  // it doesn't leak
  public void dispose() {
    AppState
        .getInstance()
        .getActiveInterfaceProperty()
        .removeListener(this.netInterfaceListener);

    this.netInterfaceListener = null;
  }
}