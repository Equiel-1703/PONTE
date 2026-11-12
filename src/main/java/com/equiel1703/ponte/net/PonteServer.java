package com.equiel1703.ponte.net;

import com.equiel1703.ponte.singletons.AppState;

import javafx.application.Platform;

public class PonteServer implements Runnable {
    private volatile boolean running = true;

    @Override
    public void run() {
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                System.out.println("Ponte server trabalhando...");
                Thread.sleep(500);
            }
            System.out.println("- Ponte server terminado -");
        } catch (InterruptedException e) {
            System.out.println("Ponte server foi interrompido no meio de uma operação bloqueante.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            
            // If we crash unexpectedly, turn the app state to off!
            Platform.runLater(() -> AppState.getInstance().turnOff());
        } finally {
            // Cleanup goes here!
        }
    }

    public void stop() {
        this.running = false;
    }
}
