package com.equiel1703.ponte.net.protocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.equiel1703.ponte.exceptions.InvalidOperationIdException;
import com.equiel1703.ponte.exceptions.InvalidPayloadSizeException;
import com.equiel1703.ponte.exceptions.MagicNumberException;
import com.equiel1703.ponte.exceptions.ProtocolVersionMismatchException;

public class PontePacket {
    public static final short MAGIC_NUMBER = 0x5054;            // Our magic number. Stands for 'P''T'
    public static final byte VERSION = 0x01;                    // 0.1
    public static final int MAX_PAYLOAD_SIZE_BYTES = 64 * 1024; // 64 KiB max payload size

    public static final int HEADER_SIZE_BYTES = 
            Short.BYTES                         // magic number
            + Byte.BYTES                        // PONTE protocol version
            + PonteOperation.SIZE_BYTES         // operation id
            + Integer.BYTES;                    // payload lenght

    public static final byte[] EMPTY_PAYLOAD = new byte[0];

    private final PonteOperation op;
    private final int payloadLen;
    private final byte[] payload;

    // Constructor for no payload operations
    public PontePacket(PonteOperation op) throws InvalidPayloadSizeException {
        this(op, EMPTY_PAYLOAD);
    }

    // Constructor for operations with payload
    public PontePacket(PonteOperation op, byte[] payload) throws InvalidPayloadSizeException {
        if (payload.length < 0 || payload.length > MAX_PAYLOAD_SIZE_BYTES) {
            throw new InvalidPayloadSizeException(payload.length);
        }

        this.op = op;
        this.payload = payload;
        this.payloadLen = payload.length;
    }

    public PonteOperation getOperation() {
        return this.op;
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public int getPayloadLen() {
        return this.payloadLen;
    }

    public byte[] getPacketAsBytes() {
        final int payloadSizeBytes = payloadLen * Byte.BYTES;

        return ByteBuffer
                .allocate(HEADER_SIZE_BYTES + payloadSizeBytes)
                .putShort(MAGIC_NUMBER)
                .put(VERSION)
                .put(this.op.getId())
                .putInt(payloadLen)
                .put(payload)
                .array();
    }

    public static PontePacket readPacketFromStream(DataInputStream in)
            throws MagicNumberException, ProtocolVersionMismatchException, InvalidOperationIdException,
            InvalidPayloadSizeException, IOException {
        // Get magic word
        short pMagic = in.readShort();
        if (pMagic != PontePacket.MAGIC_NUMBER) {
            throw new MagicNumberException(pMagic);
        }

        // Get protocol version
        byte pVersion = in.readByte();
        if (pVersion != PontePacket.VERSION) {
            throw new ProtocolVersionMismatchException(pVersion);
        }

        // Get operation
        byte pOp = in.readByte();
        PonteOperation op = PonteOperation.fromId(pOp).orElseThrow(() -> new InvalidOperationIdException(pOp));

        // Get payload
        final int pPayloadLen = in.readInt();
        if (pPayloadLen < 0 || pPayloadLen > PontePacket.MAX_PAYLOAD_SIZE_BYTES) {
            throw new InvalidPayloadSizeException(pPayloadLen);
        }
        byte[] pPayload = new byte[pPayloadLen];
        in.readFully(pPayload);

        return new PontePacket(op, pPayload);
    }

    @Override
    public String toString() {
        return String.format(
                "PontePacket{magic=0x%04X, version=0x%02X, op=%s, payloadLen=%d}",
                MAGIC_NUMBER,
                VERSION,
                op.name(),
                payloadLen);
    }
}
