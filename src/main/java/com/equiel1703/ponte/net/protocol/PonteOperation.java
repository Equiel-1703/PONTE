package com.equiel1703.ponte.net.protocol;

import java.util.Arrays;
import java.util.Optional;

public enum PonteOperation {
    OP_REQ_SYSINFO((byte)0x01),
    OP_RES_SYSINFO((byte)0x02),
    OP_BIND_SERVICE((byte)0x10),
    OP_BIND_ACK((byte)0x11),
    OP_CHANGE_PROTOCOL((byte)0x20),
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
