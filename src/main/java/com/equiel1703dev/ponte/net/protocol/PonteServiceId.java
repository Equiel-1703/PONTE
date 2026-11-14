package com.equiel1703dev.ponte.net.protocol;

import java.util.Arrays;
import java.util.Optional;

public enum PonteServiceId {
    YOUTUBE((short) 0x0001, "YouTube");

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

    public static Optional<PonteServiceId> fromId(short id) {
        return Arrays
                .stream(PonteServiceId.values())
                .filter(val -> val.getId() == id)
                .findFirst();
    }
}
