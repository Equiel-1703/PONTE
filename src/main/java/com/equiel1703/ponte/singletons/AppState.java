package com.equiel1703.ponte.singletons;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import oshi.hardware.NetworkIF;

public class AppState {
  private ObjectProperty<NetworkIF> activeInterface = new SimpleObjectProperty<>();
  private boolean ponteActive = false;

  // Constructor
  private AppState() { }
  
  // Singleton instance
  private static final AppState instance = new AppState();
  
  public static AppState getInstance() {
    return instance;
  }

  public void setActiveInterface(NetworkIF netInt) {
    activeInterface.set(netInt);

    System.out.println("Active Interface:  " + netInt.getName());
  }

  public NetworkIF getActiveInterface() {
    return this.activeInterface.get();
  }

  public ObjectProperty<NetworkIF> getActiveInterfaceProperty() {
    return this.activeInterface;
  }

  public void toggleActive() {
    this.ponteActive = !this.ponteActive;
  }

  public boolean isActive() {
    return this.ponteActive;
  }
}
