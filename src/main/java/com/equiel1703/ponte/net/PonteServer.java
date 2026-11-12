package com.equiel1703.ponte.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ClosedByInterruptException;

import com.equiel1703.ponte.singletons.AppState;

import javafx.application.Platform;
import oshi.hardware.NetworkIF;

public class PonteServer implements Runnable {
    private static final int PONTE_SERVER_PORT = 5_000;
    private final NetworkIF networkIF;

    private volatile boolean running = true;

    public PonteServer(NetworkIF networkIF) {
        this.networkIF = networkIF;
    }

    @Override
    public void run() {
        final String ipv4 = this.networkIF.getIPv4addr()[0];

        ServerSocket serverSocket = null;
        Socket clienSocket = null;
        
        InputStreamReader in = null;
        BufferedReader br = null;

        try {
            InetAddress addr = InetAddress.getByName(ipv4);
            serverSocket = new ServerSocket(PONTE_SERVER_PORT, 0, addr);

            System.out.printf("[PonteServer] Server listening on (%s) %s:%d\n", networkIF.getName(), ipv4, PONTE_SERVER_PORT);

            clienSocket = serverSocket.accept();
            System.out.println("[PonteServer] Client: " + clienSocket.getInetAddress().toString());

            in = new InputStreamReader(clienSocket.getInputStream());
            br = new BufferedReader(in);
            
            while (running && !Thread.currentThread().isInterrupted()) {
                String line = br.readLine();

                System.out.println("Client says: " + line);

                if (line.equals("END")) {
                    running = false;
                }
            }

            System.out.println("[PonteServer] --- Server Finished ---");
        } catch (ClosedByInterruptException e) {
            System.out.println("[PonteServer] --- Interrupted during blocking call ---");
        } catch (Exception e) {
            System.out.println("[PonteServer] Something bad happened! Stopping server...");
            System.out.println("[PonteServer] ERROR: " + e.getMessage());
            
            e.printStackTrace();
            
            // If we crash unexpectedly, turn the app state to off!
            Platform.runLater(() -> AppState.getInstance().turnOff());
        } finally {
            try {
                if (serverSocket != null) serverSocket.close();
                if (clienSocket != null) clienSocket.close();
                if (in != null) in.close();
                if (br != null) br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("[PonteServer] RESOURCES CLEANED!!");
        }
    }

    public void stop() {
        this.running = false;
    }
}
