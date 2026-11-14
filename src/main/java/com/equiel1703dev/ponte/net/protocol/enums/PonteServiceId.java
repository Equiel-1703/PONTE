package com.equiel1703dev.ponte.net.protocol.enums;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum PonteServiceId {
    YOUTUBE((short) 0x0001, "YouTube");

    public static final int SIZE_BYTES = Short.BYTES; 
    private final short id;
    private final String displayName;

    PonteServiceId(short id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public short getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PonteServiceId fromId(short id) {
        return Arrays
                .stream(PonteServiceId.values())
                .filter(val -> val.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                    String.format("Unknown Ponte Service ID: 0x%04X", id))
                );
    }

    @Override
    public String toString() {
        return String.format("%s (0x%04X)", displayName, id);
    }
}
