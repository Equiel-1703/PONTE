package com.equiel1703.ponte.net.protocol;

import java.util.Arrays;
import java.util.Optional;

public enum PonteOperation {
    OP_REQ_SYSINFO((byte)0x01),
    OP_RES_SYSINFO((byte)0x02),
    OP_REQ_BIND_SERVICE((byte)0x10),
    OP_RES_BIND_SERVICE((byte)0x11),
    OP_ACK_BIND_SERVICE((byte)0x12),
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
