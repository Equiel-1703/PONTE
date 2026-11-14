package com.equiel1703dev.ponte.urls;

import java.net.URL;

import com.equiel1703dev.ponte.PONTE;

public enum NetInterfaceIcon {
  ETHERNET("imgs/cabled.png"),
  GENERIC("imgs/generic.png"),
  WIRELESS("imgs/wifi.png");

  private URL url;
  
  NetInterfaceIcon(String path) {
    this.url = PONTE.class.getResource(path);
  }

  public URL getUrl() {
    return this.url;
  }
}
