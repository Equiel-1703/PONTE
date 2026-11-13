package com.equiel1703.ponte.exceptions;

public class InvalidOperationIdException extends RuntimeException {
    public InvalidOperationIdException(byte id) {
        super(String.format("Invalid operation ID received: 0x%02X", id));
    }
}