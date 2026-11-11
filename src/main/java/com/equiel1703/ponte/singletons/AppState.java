package com.equiel1703.ponte.singletons;

import com.equiel1703.ponte.exceptions.NoInterfaceSelected;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import oshi.hardware.NetworkIF;

public class AppState {
  private ObjectProperty<NetworkIF> activeInterface = new SimpleObjectProperty<>(null);
  private ObjectProperty<Boolean> ponteActive = new SimpleObjectProperty<>(Boolean.FALSE);

  // Constructor
  private AppState() { }
  
  // Singleton instance
  private static final AppState instance = new AppState();
  
  public static AppState getInstance() {
    return instance;
  }

  public void setActiveInterface(NetworkIF netInt) {
    activeInterface.set(netInt);

    System.out.println("Active Interface:  " + (netInt != null ? netInt.getName() : "None"));
  }

  public NetworkIF getActiveInterface() {
    return this.activeInterface.get();
  }

  public ObjectProperty<NetworkIF> getActiveInterfaceProperty() {
    return this.activeInterface;
  }

  public ObjectProperty<Boolean> getOnOffProperty() {
    return this.ponteActive;
  }

  public void turnOn() throws NoInterfaceSelected {
    if (this.activeInterface.get() == null) {
      throw new NoInterfaceSelected("You must select a network interface first!");
    }

    this.ponteActive.set(Boolean.TRUE);
  }

  public void turnOff() {
    this.ponteActive.set(Boolean.FALSE);
  }

  public boolean isActive() {
    return this.ponteActive.get().booleanValue();
  }
}
