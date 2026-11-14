package com.equiel1703dev.ponte.net.protocol;

import java.nio.ByteBuffer;

import com.equiel1703dev.ponte.net.protocol.enums.Architecture;
import com.equiel1703dev.ponte.net.protocol.enums.VideoEncoding;

public record ClientCapabilities(
        Architecture architecture,
        VideoEncoding preferredVideoEncoding,
        byte preferredFps,
        int cpuClockMhz,
        int ramMb,
        int maxVideoWidthPixels,
        int maxVideoHeightPixels) {

    public static final int SIZE_BYTES = Architecture.SIZE_BYTES
            + VideoEncoding.SIZE_BYTES
            + Byte.BYTES
            + 4 * Integer.BYTES;

    public static ClientCapabilities fromPayload(byte[] payload) {
        if (payload.length != ClientCapabilities.SIZE_BYTES) {
            String msg = String.format(
                    "Invalid Client Capabilities payload size! Expected %d bytes, got %d bytes",
                    ClientCapabilities.SIZE_BYTES, payload.length);

            throw new IllegalArgumentException(msg);
        }

        // Get bytebuffer from payload
        ByteBuffer payloadByteBuffer = ByteBuffer.wrap(payload);

        // Getting fields from packet in this EXACT ORDER
        Architecture pArchitecture = Architecture.fromId(payloadByteBuffer.get());
        VideoEncoding pVideoEncoding = VideoEncoding.fromId(payloadByteBuffer.get());
        byte pPreferredFps = payloadByteBuffer.get();
        int pCpuClockMhz = payloadByteBuffer.getInt();
        int pRamMb = payloadByteBuffer.getInt();
        int pMaxVideoWidthPixels = payloadByteBuffer.getInt();
        int pMaxVideoHeightPixels = payloadByteBuffer.getInt();

        return new ClientCapabilities(
                pArchitecture,
                pVideoEncoding,
                pPreferredFps,
                pCpuClockMhz,
                pRamMb,
                pMaxVideoWidthPixels,
                pMaxVideoHeightPixels);
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ClientCapabilities {")
                .append("\n  architecture: ").append(architecture.name())
                .append("\n  preferredVideoEncoding: ").append(preferredVideoEncoding.name())
                .append("\n  preferredFps: ").append(preferredFps)
                .append("\n  cpuClockMhz: ").append(cpuClockMhz)
                .append("\n  ramMb: ").append(ramMb)
                .append("\n  maxVideoWidthPixels: ").append(maxVideoWidthPixels)
                .append("\n  maxVideoHeightPixels: ").append(maxVideoHeightPixels)
                .append("\n}");
        return sb.toString();
    }
}
