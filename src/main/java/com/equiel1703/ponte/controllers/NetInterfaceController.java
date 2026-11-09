package com.equiel1703.ponte.controllers;

import com.equiel1703.ponte.net.NetUtils;
import com.equiel1703.ponte.singletons.AppState;
import com.equiel1703.ponte.urls.NetInterfaceIcon;

import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
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
  // The active state pseudo-class of the Network Interfaces
  private static final PseudoClass ACTIVE_PSEUDO_CLASS = PseudoClass.getPseudoClass("active");
  // The change listener that detects when a UI inteerface element should enter
  // the "active" state
  private final ChangeListener<NetworkIF> activeInterfaceChangeListener = (observable, oldValue, newValue) -> {
    boolean active = this.netInt.getName() == newValue.getName();
    this.NetIntRoot.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, active);
  };

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
    AppState
        .getInstance()
        .getActiveInterfaceProperty()
        .addListener(this.activeInterfaceChangeListener);
  }

  // Called when the user clicks in the interface
  public void onClicked(MouseEvent e) {
    System.out.println("The user clicked on " + this.netInt.getName());

    AppState
        .getInstance()
        .setActiveInterface(this.netInt);
  }

  // Called by the MainGUI when we want to get rid of this controller.
  // We have to remote the listener from the AppState active interface property so it
  // doesn't leak
  public void dispose() {
    AppState
        .getInstance()
        .getActiveInterfaceProperty()
        .removeListener(this.activeInterfaceChangeListener);
  }
}