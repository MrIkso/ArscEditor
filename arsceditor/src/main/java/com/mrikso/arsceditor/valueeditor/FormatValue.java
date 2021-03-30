package com.mrikso.arsceditor.valueeditor;

public class FormatValue {

    private final ValueType valueType;
    private final String value;
    private int index;

    public FormatValue(ValueType valueType, String value, int index){
        this.valueType = valueType;
        this.value = value;
        this.index = index;
    }

    public FormatValue(ValueType valueType, String value){
        this.valueType = valueType;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public ValueType getValueType() {
        return valueType;
    }
}
