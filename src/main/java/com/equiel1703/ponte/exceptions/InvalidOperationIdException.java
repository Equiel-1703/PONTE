package com.equiel1703.ponte.exceptions;

import java.io.IOException;

public class InvalidOperationIdException extends IOException {
    public InvalidOperationIdException(byte id) {
        super(String.format("Invalid operation ID received: 0x%02X", id));
    }
}