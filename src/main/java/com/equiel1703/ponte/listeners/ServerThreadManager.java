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
        if (newValue.booleanValue()) {
            serverOn();
        } else {
            serverOff();
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

    private void serverOff() {
        if (this.ponteServer != null && this.ponteServerThread != null)
        {
            // Set running = false and close sockets to immediately unblock I/O
            this.ponteServer.stop();
            // Interrupt in case the thread is in a non-socket blocking call (like sleep/wait)
            this.ponteServerThread.interrupt();
    
            try {
                // Force to wait for the thread to finish!
                // We want to guarantee the socket is released and everything is ok
                // before proceeding. We will wait at most 200ms before proceeding.
                this.ponteServerThread.join(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("[ServerThreadManager] Interrupted while stopping PONTE server.");
            } finally {
                this.ponteServer = null;
                this.ponteServerThread = null;
            }    
        }
    }
}
