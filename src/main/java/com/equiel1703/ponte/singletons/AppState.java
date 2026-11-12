package com.equiel1703.ponte.singletons;

import com.equiel1703.ponte.exceptions.NoInterfaceSelected;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import oshi.hardware.NetworkIF;

public class AppState {
  private ObjectProperty<NetworkIF> activeInterface = new SimpleObjectProperty<>(null);
  private BooleanProperty ponteActive = new SimpleBooleanProperty(false);

  // Constructor
  private AppState() {
  }

  // Singleton instance
  private static final AppState instance = new AppState();

  public static AppState getInstance() {
    return instance;
  }

  public void clearActiveInterface() {
    this.activeInterface.set(null);
    System.out.println("[AppState] Cleared Active Interface.");
  }

  public void setActiveInterface(NetworkIF netInt) {
    if (netInt == null) {
      return;
    }

    // Is the new interface the same we already have?
    NetworkIF current = this.activeInterface.get();
    if (current != null && current.getName().equals(netInt.getName())) {
      System.out.println("[AppState] New interface same as before. Ignoring...");
      return;
    }

    // If we're changing to a different interface with the server running,
    // we have to turn the server off first!
    if (this.isActive()) {
      System.out.println("[AppState] WARNING: Changing active interface while running the server!");
      this.turnOff();
    }

    this.activeInterface.set(netInt);
    System.out.println("[AppState] Active Interface: " + netInt.getName());
  }

  public NetworkIF getActiveInterface() {
    return this.activeInterface.get();
  }

  public ObjectProperty<NetworkIF> getActiveInterfaceProperty() {
    return this.activeInterface;
  }

  public BooleanProperty getOnOffProperty() {
    return this.ponteActive;
  }

  public void turnOn() throws NoInterfaceSelected {
    if (this.activeInterface.get() == null) {
      throw new NoInterfaceSelected("You must select a network interface first!");
    }

    // If the server is already on, we don't waste time
    if (this.isActive()) {
      return;
    }

    this.ponteActive.set(true);
  }

  public void turnOff() {
    // If the server is already off, we don't waste time
    if (!this.isActive()) {
      return;
    }

    this.ponteActive.set(false);
  }

  public boolean isActive() {
    return this.ponteActive.get();
  }
}
