package com.equiel1703.ponte.listeners;

import com.equiel1703.ponte.net.PonteServer;
import com.equiel1703.ponte.singletons.AppState;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import oshi.hardware.NetworkIF;

public class ServerThreadManager implements ChangeListener<Boolean> {
    private Thread ponteServerThread = null;
    private PonteServer ponteServer = null;

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            serverOn();
        } else {
            try {
                serverOff();
            } catch (InterruptedException e) {
                System.out.println("[ServerThreadManager] Something went wrong when trying to stop PONTE server.");
                e.printStackTrace();
            }
        }
    }

    private void serverOn() {
        NetworkIF activeNetworkIF = AppState
                .getInstance()
                .getActiveInterface();

        this.ponteServer = new PonteServer(activeNetworkIF);
        this.ponteServerThread = Thread
                .ofVirtual()
                .name("ponte-server-vthread")
                .start(this.ponteServer);
    }

    private void serverOff() throws InterruptedException {
        // Signal to internal loop to terminate
        this.ponteServer.stop();
        // Wake up the thread immediately if it's in blocking operation
        this.ponteServerThread.interrupt();

        // Force wait for the thread to finish!
        // We want to guarantee the socket is released and everything is ok
        // before proceeding. We will wait at most 1s before proceeding.
        this.ponteServerThread.join(1_000);

        this.ponteServer = null;
        this.ponteServerThread = null;
    }
}
