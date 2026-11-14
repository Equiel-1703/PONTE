package com.equiel1703dev.ponte.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import com.equiel1703dev.ponte.exceptions.InvalidOperationIdException;
import com.equiel1703dev.ponte.exceptions.InvalidPayloadSizeException;
import com.equiel1703dev.ponte.exceptions.MagicNumberException;
import com.equiel1703dev.ponte.exceptions.ProtocolVersionMismatchException;
import com.equiel1703dev.ponte.net.protocol.enums.PonteOperation;
import com.equiel1703dev.ponte.net.protocol.enums.PonteServiceId;
import com.equiel1703dev.ponte.net.protocol.ClientCapabilities;
import com.equiel1703dev.ponte.net.protocol.PontePacket;
import com.equiel1703dev.ponte.singletons.AppState;

import javafx.application.Platform;
import oshi.hardware.NetworkIF;

public class PonteServer implements Runnable {
    private static final int PONTE_SERVER_PORT = 20_564;
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

            // This loop will keep the server looking for new clients
            while (running) {
                // Inner try-with-resources to manage the client Socket and it's
                // DataInputStream and DataOutputStream lifecycles
                try (Socket cli = srv.accept();
                        DataInputStream in = new DataInputStream(cli.getInputStream());
                        DataOutputStream out = new DataOutputStream(cli.getOutputStream())) {
                    this.clientSocket = cli;

                    System.out.printf(
                            "[PonteServer] Client connected: %s:%d%n",
                            cli.getInetAddress().toString(), cli.getPort());

                    // Perform PONTE protocol handshake
                    clientHandshake(in, out);

                    // After a successful handshake, this loop will keep the server reading packets from the client
                    // until the client disconnects, the server is stopped or an exception occurs
                    while (running) {
                        PontePacket packet = PontePacket.readPacketFromStream(in);
                        System.out.println("[PonteServer] Received packet from client: " + packet.toString());
                    }
                } catch (EOFException e) {
                    // This exception can happen when the client simply disconnects, we can ignore it
                    System.out.println("[PonteServer] EOFException: Client disconnected. Everything is fine.");
                } catch (MagicNumberException
                        | ProtocolVersionMismatchException
                        | InvalidOperationIdException
                        | InvalidPayloadSizeException e) {
                    // These exceptions are thrown when the client sends a malformed packet
                    System.err.println("[PonteServer] Malformed packet received from client: " + e.getMessage());
                } finally {
                    this.clientSocket = null;
                }

                System.out.println("[PonteServer] Client disconnected. Waiting for new one...");
            }
            System.out.println("[PonteServer] --- Server Finished ---");
        } catch (SocketException e) {
            if (running == false) {
                // If running is false, then this exception was thrown when stop()
                // closed the socket to unblock accept() or a read. It's an expected
                // behavior, not a real error.
                System.out.println("[PonteServer] --- Server stopped by user ---");
            } else {
                // If running was not false, then it's a real error.
                handleUnexpectedException(e);
            }
        } catch (Exception e) {
            // Other exceptions are unexpected and should be logged as errors
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

    private static IOException unexpectedHandshakeOperation(String expected, String got) {
        String msg = String.format(
                "Handshake failed: Expected '%s' packet from client, got %s%n",
                expected, got);

        return new IOException(msg);
    }

    private void clientHandshake(DataInputStream in, DataOutputStream out) throws IOException {
        // Set a 5-second timeout for handshake steps so broken clients don't hang
        // forever
        this.clientSocket.setSoTimeout(5_000);

        // Ask client for its capabilities (OP_SYSINFO_REQ)
        PontePacket.writePacketToStream(out, new PontePacket(PonteOperation.OP_SYSINFO_REQ));

        // Read client capabilities (OP_SYSINFO_RES)
        PontePacket sysInfoPacket = PontePacket.readPacketFromStream(in);
        if (sysInfoPacket.getOperation() != PonteOperation.OP_SYSINFO_RES) {
            throw unexpectedHandshakeOperation(PonteOperation.OP_SYSINFO_RES.name(),
                    sysInfoPacket.getOperation().name());
        }
        ClientCapabilities clientCapabilities = ClientCapabilities.fromPayload(sysInfoPacket.getPayload());
        System.out.println("[PonteServer] Client Capabilities: " + clientCapabilities.toString());

        // Ask client which service it wants (OP_BIND_SERVICE_REQ)
        PontePacket.writePacketToStream(out, new PontePacket(PonteOperation.OP_BIND_SERVICE_REQ));

        // Read requested service from client (OP_BIND_SERVICE_RES)
        PontePacket bindResPacket = PontePacket.readPacketFromStream(in);
        if (bindResPacket.getOperation() != PonteOperation.OP_BIND_SERVICE_RES) {
            throw unexpectedHandshakeOperation(PonteOperation.OP_BIND_SERVICE_RES.name(),
                    bindResPacket.getOperation().name());
        }
        PonteServiceId serviceId = PonteServiceId.fromId(
                ByteBuffer.wrap(bindResPacket.getPayload()).getShort());
        System.out.println("Service requested by client: " + serviceId.toString());

        // Acknowledge service binding (OP_BIND_SERVICE_ACK)
        PontePacket.writePacketToStream(out, new PontePacket(PonteOperation.OP_BIND_SERVICE_ACK));

        System.out.println("--- CLIENT HANDSHAKE COMPLETED WITH SUCCESS ---");

        // Remove timeout for the session
        this.clientSocket.setSoTimeout(0);
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
        } catch (IOException igException) {
        }

        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
        } catch (IOException igException) {
        }
    }
}
