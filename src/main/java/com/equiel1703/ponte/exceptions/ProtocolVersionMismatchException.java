package com.equiel1703.ponte.exceptions;

import com.equiel1703.ponte.net.protocol.PontePacket;

public class ProtocolVersionMismatchException extends RuntimeException {
    public ProtocolVersionMismatchException(byte version) {
        super(String.format("Protocol version mismatch. Expected: %d, Received: %d", PontePacket.VERSION, version));
    }
}
