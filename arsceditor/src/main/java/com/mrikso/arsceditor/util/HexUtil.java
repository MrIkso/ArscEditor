package com.mrikso.arsceditor.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HexUtil {

    public static String byteToHexString(byte a) {
        int a1 = byteToInt(a);
        return intToHexString(a1);
    }

    public static String littleEndianBytesToHexString(byte a, byte b) {
        return intToHexString(littleEndianBytesToInt(a, b));
    }

    public static String littleEndianBytesToHexString(byte a, byte b, byte c, byte d) {
        return intToHexString(littleEndianBytesToInt(a, b, c, d));
    }

    public static String littleEndianBytesToHexString(byte[] value, int startPos, int length) {
        return intToHexString(littleEndianBytesToInt(value, startPos, length));
    }

    public static int littleEndianBytesToInt(byte a, byte b) {
        int a1 = byteToInt(a);
        int b1 = byteToInt(b);
        return (b1 << 8) + a1;
    }

    public static int littleEndianBytesToInt(byte a, byte b, byte c, byte d) {
        int a1 = byteToInt(a);
        int b1 = byteToInt(b);
        int c1 = byteToInt(c);
        int d1 = byteToInt(d);
        return (d1 << 24) + (c1 << 16) + (b1 << 8) + a1;
    }

    public static int littleEndianBytesToInt(byte[] value, int startPos, int length) {
        if (length == 1) {
            return byteToInt(value[startPos]);
        } else if (length == 2) {
            return littleEndianBytesToInt(value[startPos], value[startPos + 1]);
        } else if (length == 4) {
            return littleEndianBytesToInt(value[startPos], value[startPos + 1], value[startPos + 2], value[startPos + 3]);
        } else {
            return 0;
        }
    }

    public static int byteToInt(byte a) {
        if (a < 0) {
            return a + 256;
        }
        return a;
    }

    public static String intToHexString(int a) {
        return "0x" + Integer.toHexString(a);
    }

    public static String getString(byte[] value, int startPos, int length) throws IOException {
        byte[] data = new byte[length];
        if (data.length >= 0) System.arraycopy(value, startPos, data, 0, data.length);
        return new String(data, StandardCharsets.UTF_8);
    }

}