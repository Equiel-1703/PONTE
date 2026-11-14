package com.equiel1703dev.ponte.net.protocol.enums;

import java.util.Arrays;
import java.util.Optional;

public enum PonteOperation {
    OP_SYSINFO_REQ((byte)0x01),
    OP_SYSINFO_RES((byte)0x02),
    OP_BIND_SERVICE_REQ((byte)0x10),
    OP_BIND_SERVICE_RES((byte)0x11),
    OP_BIND_SERVICE_ACK((byte)0x12),
    OP_ERROR((byte)0xFF);

    public static final int SIZE_BYTES = Byte.BYTES;
    private final byte id;

    PonteOperation(byte id) {
        this.id = id;
    }

    public byte getId() {
        return this.id;
    }

    public static Optional<PonteOperation> fromId(byte id) {
        return Arrays
                .stream(PonteOperation.values())
                .filter(val -> val.getId() == id)
                .findFirst();
    }
}
