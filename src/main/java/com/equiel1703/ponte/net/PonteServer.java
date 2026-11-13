package com.equiel1703.ponte.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

import com.equiel1703.ponte.singletons.AppState;

import javafx.application.Platform;
import oshi.hardware.NetworkIF;

public class PonteServer implements Runnable {
    private static final int PONTE_SERVER_PORT = 5_000;
    private final NetworkIF networkIF;

    private volatile ServerSocket serverSocket = null;
    private volatile Socket clientSocket = null;
    private volatile boolean running = true;

    public PonteServer(NetworkIF networkIF) {
        this.networkIF = networkIF;
    }

    @Override
    public void run() {
        // Outer try-with-resources to manage the ServerSocket lifecycle
        try (ServerSocket srv = new ServerSocket()) {
            final String ipv4 = this.networkIF.getIPv4addr()[0];
            final InetAddress addr = InetAddress.getByName(ipv4);

            // Allow immediate port re-binding if the user rapidly toggles ON/OFF
            this.serverSocket = srv;
            srv.setReuseAddress(true);
            srv.bind(new InetSocketAddress(addr, PONTE_SERVER_PORT));

            System.out.printf("[PonteServer] Server listening on (%s) %s:%d%n",
                    networkIF.getName(), ipv4, PONTE_SERVER_PORT);

            // Inner try-with-resources to manage the Client Socket and Readers
            try (Socket cli = srv.accept();
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(cli.getInputStream(), StandardCharsets.UTF_8))
                ) {
                this.clientSocket = cli;
                System.out.println("[PonteServer] Client connected: " + cli.getInetAddress().toString());

                String line;
                // readLine() will return null if the client disconnects
                while (running && (line = br.readLine()) != null) {
                    System.out.println("Client says: " + line);

                    if ("END".equals(line)) {
                        running = false;
                    }
                }
            }
            System.out.println("[PonteServer] --- Server Finished ---");
        } catch (SocketException e) {
            if (running == false) {
                // If running is false, then this exception was thrown when stop()
                // closed the socket to unblock accept() or readLine(). It's an expected
                // behavior, not a real error.
                System.out.println("[PonteServer] --- Server stopped by user ---");
            } else {
                // If running was not false, it's a real error.
                handleUnexpectedException(e);
            }
        } catch (Exception e) {
            handleUnexpectedException(e);
        } finally {
            this.clientSocket = null;
            this.serverSocket = null;

            // Always turn the AppState to off when server is done!
            // This will update the UI and call the appropriate listeners
            Platform.runLater(() -> AppState.getInstance().turnOff());
            
            System.out.println("[PonteServer] RESOURCES CLEANED!!");
        }
    }

    private void handleUnexpectedException(Exception e) {
        System.err.println("[PonteServer] Something bad happened! Stopping server...");
        System.err.println("[PonteServer] ERROR: " + e.getMessage());
        e.printStackTrace();
    }

    // Called by other threads to make the server stop
    public void stop() {
        this.running = false;

        // Closing the sockets to unblock the server thread immediately
        // if it's waiting on accept() or readLine()
        try {
            if (this.clientSocket != null) {
                this.clientSocket.close();
            }
        } catch (IOException igException) { }

        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
        } catch (IOException igException) { }
    }
}
