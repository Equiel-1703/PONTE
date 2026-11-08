package com.equiel1703.urls;

import java.net.URL;

import com.equiel1703.PONTE;

public enum CSS {
  MAIN_GUI("main-gui.css"),
  NET_INTERFACE_ELEMENT("net-interface-element.css");

  private URL url;

  CSS(String path) {
    this.url = PONTE.class.getResource(path);
  }

  public URL getUrl() {
    return this.url;
  }
}
