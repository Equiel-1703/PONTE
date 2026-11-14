package com.equiel1703dev.ponte.net.protocol.enums;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Supported architectures for the PONTE protocol. These are used by the clients to inform
 * the server about the architecture they are running on. This does not mean that the services
 * are compatible with all these architectures. This is just a standard and unified way to 
 * identify them. It's easy to add more and easy for the services to identify and add support
 * for them. The services can choose to support only a subset of these.
 */
public enum Architecture {
    MIPSR5900((byte)0x01),
    i386((byte)0x02),
    Motorola68k((byte)0x03),
    MOS6502((byte)0x04),
    ARM((byte)0x05),
    PowerPC((byte)0x06);

    public static final int SIZE_BYTES = Byte.BYTES;
    private final byte id;

    Architecture(byte id) {
        this.id = id;
    }

    public byte getId() {
        return this.id;
    }

    public static Architecture fromId(byte id) {
        return Arrays
                .stream(Architecture.values())
                .filter(val -> val.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                    String.format("Unknown Architecture ID: 0x%02X", id))
                );
    }
}
