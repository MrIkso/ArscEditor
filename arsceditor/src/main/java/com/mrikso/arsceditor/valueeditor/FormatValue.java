package com.mrikso.arsceditor.valueeditor;

public class FormatValue {

    private final ValueType valueType;
    private final String value;
    private String decodedNamedValue;
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

    public FormatValue(ValueType valueType, String value, int index, String decodedNamedValue){
        this.valueType = valueType;
        this.value = value;
        this.index = index;
        this.decodedNamedValue = decodedNamedValue;
    }

    public FormatValue(ValueType valueType, String value, String decodedNamedValue){
        this.valueType = valueType;
        this.value = value;
        this.decodedNamedValue = decodedNamedValue;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public String getDecodedNamedValue() {
        return decodedNamedValue;
    }

    public ValueType getValueType() {
        return valueType;
    }
}
