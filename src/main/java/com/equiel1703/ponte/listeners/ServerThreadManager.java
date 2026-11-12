package com.equiel1703.ponte.listeners;

import com.equiel1703.ponte.net.PonteServer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ServerThreadManager implements ChangeListener<Boolean> {
    private Thread ponteServerThread = null;
    private PonteServer ponteServer = null;

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            this.ponteServer = new PonteServer();
            this.ponteServerThread = Thread
                    .ofVirtual()
                    .name("ponte-server-vthread")
                    .start(this.ponteServer);
        } else {
            // Signal to internal loop to terminate
            this.ponteServer.stop();
            // Wake up the thread immediately if it's in blocking operation
            this.ponteServerThread.interrupt();

            this.ponteServer = null;
            this.ponteServerThread = null;
        }
    }

}
