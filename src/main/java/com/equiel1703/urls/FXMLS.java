package com.equiel1703.urls;

import java.net.URL;

import com.equiel1703.PONTE;

public enum FXMLS {
  MAIN_GUI("main-gui.fxml"),
  NET_INTERFACE_ELEMENT("net-interface-element.fxml");

  private URL url;

  FXMLS(String path) {
    this.url = PONTE.class.getResource(path);
  }

  public URL getUrl() {
    return this.url;
  }
}
