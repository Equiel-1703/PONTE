package com.equiel1703.ponte.exceptions;

import com.equiel1703.ponte.net.protocol.PontePacket;

public class MagicNumberException extends RuntimeException {
    public MagicNumberException(short magicNumber) {
        super(
            String.format("Invalid magic number received: 0x%04X ('%c%c'). Expected: 0x%04X ('%c%c')",
                magicNumber, (char)((magicNumber >> 8) & 0xFF), (char)(magicNumber & 0xFF),
                PontePacket.MAGIC_NUMBER, (char)((PontePacket.MAGIC_NUMBER >> 8) & 0xFF), (char)(PontePacket.MAGIC_NUMBER & 0xFF))
        );
    }
}
