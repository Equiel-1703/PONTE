package com.equiel1703dev.ponte.net.protocol.enums;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Supported video encodings for the PONTE protocol. These are used by the clients to inform
 * the server about the video encoding they prefer to use. This is used by services that provide
 * some form of video streaming. The services can choose to support only a subset of these.
 */
public enum VideoEncoding {
    MPEG2((byte)0x01),
    MPEG4((byte)0x02),
    H264((byte)0x03),
    H265((byte)0x04),
    VP8((byte)0x05),
    VP9((byte)0x06),
    AV1((byte)0x07);

    public static final int SIZE_BYTES = Byte.BYTES;
    private final byte id;

    VideoEncoding(byte id) {
        this.id = id;
    }

    public byte getId() {
        return this.id;
    }

    public static VideoEncoding fromId(byte id) {
        return Arrays
                .stream(VideoEncoding.values())
                .filter(val -> val.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                    String.format("Unknown Preferred Video Encoding ID: 0x%02X", id))
                );
    }
}
