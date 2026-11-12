package com.equiel1703.ponte.listeners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import oshi.hardware.NetworkIF;

public class NetInterfaceListener implements ChangeListener<NetworkIF> {
    private static final PseudoClass ACTIVE_PSEUDO_CLASS = PseudoClass.getPseudoClass("active");
    private final NetworkIF networkIF;
    private final Node netIntRoot;

    public NetInterfaceListener(NetworkIF networkIF, Node netIntRoot) {
        this.networkIF = networkIF;
        this.netIntRoot = netIntRoot;
    }

    @Override
    public void changed(ObservableValue<? extends NetworkIF> observable, NetworkIF oldValue, NetworkIF newValue) {
        System.out.println("[NetInterfaceListener] 'changed' method called.");

        boolean active = false;
        if (newValue != null) {
            active = this.networkIF.getName().equals(newValue.getName());
        }

        this.netIntRoot.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, active);
    }

}
