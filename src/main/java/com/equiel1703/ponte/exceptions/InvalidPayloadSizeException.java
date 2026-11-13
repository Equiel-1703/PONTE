package com.equiel1703.ponte.exceptions;

import com.equiel1703.ponte.net.protocol.PontePacket;

public class InvalidPayloadSizeException extends RuntimeException {
    public InvalidPayloadSizeException(int size) {
        super(
            String.format("Invalid payload size: %d bytes. Maximum allowed is %d bytes.", 
                size, PontePacket.MAX_PAYLOAD_SIZE_BYTES)
        );
    }
    
}
