package com.mrikso.arsceditor.valueeditor;

public enum ValueType {
    TYPE_STRING(0),
    TYPE_REFERENCE(1),
    TYPE_ATTRIBUTE(2),
    TYPE_COLOR(3),
    TYPE_INT_DEC(4),
    TYPE_INT_HEX(5),
    TYPE_BOOLEAN(6),
    TYPE_FLOAT(7),
    TYPE_DIMENSION(8),
    TYPE_FRACTION(9),
    TYPE_DYNAMIC_REFERENCE(10);
    private final int value;

    ValueType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
