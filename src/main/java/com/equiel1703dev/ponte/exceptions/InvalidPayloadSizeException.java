package com.equiel1703dev.ponte.exceptions;

import java.io.IOException;

import com.equiel1703dev.ponte.net.protocol.PontePacket;

public class InvalidPayloadSizeException extends IOException {
    public InvalidPayloadSizeException(int size) {
        super(
            String.format("Invalid payload size: %d bytes. Maximum allowed is %d bytes.", 
                size, PontePacket.MAX_PAYLOAD_SIZE_BYTES)
        );
    }
    
}
