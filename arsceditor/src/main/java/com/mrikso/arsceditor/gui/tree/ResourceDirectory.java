package com.mrikso.arsceditor.gui.tree;

import com.mrikso.arsceditor.valueeditor.ValueType;
import com.google.devrel.gmscore.tools.apk.arsc.TypeChunk;

public class ResourceDirectory extends JTableTreeNode {

    private static final long serialVersionUID = 1L;
    private String value;
    private String name;
    private String decodedName;
    private String decodedValue;
    private ValueType valueType;
    private TypeChunk typeChunk;
    private int nameIndex, valueIndex, entryIndex, entryParentIndex = -1;
    private boolean isComplex;

    public ResourceDirectory() {
        super();
    }

    public ResourceDirectory(String id) {
        super(id);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDecodedName() {
        return decodedName;
    }

    @Override
    public void setDecodedName(String name) {
        decodedName = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setId(String id) {
        setUserObject(id);
    }

    @Override
    public String getId() {
        return (String) getUserObject();
    }

    @Override
    public void setNameIndex(int index) {
        this.nameIndex = index;
    }

    @Override
    public int getNameIndex() {
        return nameIndex;
    }

    @Override
    public void setValueIndex(int index) {
        valueIndex = index;
    }

    @Override
    public int getValueIndex() {
        return valueIndex;
    }

    @Override
    public void setEntryIndex(int index) {
        this.entryIndex = index;
    }

    @Override
    public int getEntryIndex() {
        return entryIndex;
    }

    @Override
    public void setEntryChildrenIndex(int index) {
        this.entryParentIndex = index;
    }

    @Override
    public int getEntryChildrenIndex() {
        return entryParentIndex;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getDecodeValue() {
        return decodedValue;
    }

    @Override
    public void setDecodedValue(String value) {
        decodedValue = value;
    }

    @Override
    public ValueType getValueType() {
        return valueType;
    }

    @Override
    public void setValueType(ValueType value) {
        valueType = value;
    }

   /* @Override
    public TypeChunk getTypeChunk() {
        return typeChunk;
    }

    @Override
    public void setTypeChunk(TypeChunk value) {
        typeChunk = value;
    }*/

    @Override
    public boolean isComplex() {
        return isComplex;
    }

    @Override
    public void setIsComplex(boolean isComplex) {
        this.isComplex = isComplex;
    }

    @Override
    public boolean isChildren() {
        return false;
    }

    @Override
    public void setIsChildren(boolean isChildren) {

    }
}

