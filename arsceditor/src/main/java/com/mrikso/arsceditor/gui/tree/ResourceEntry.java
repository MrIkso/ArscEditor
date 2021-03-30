package com.mrikso.arsceditor.gui.tree;

import com.mrikso.arsceditor.valueeditor.ValueType;

public class ResourceEntry extends JTableTreeNode {
    private static final long serialVersionUID = 1L;
    private String name;
    private String value;
    private ValueType valueType;
    private int nameIndex, valueIndex, entryIndex, entryParentIndex = -1;
    private boolean isChildren =true;

    public ResourceEntry() {
        super();
    }

    public ResourceEntry(String id) {
        super(id);
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
    public ValueType getValueType() {
        return valueType;
    }

    @Override
    public void setValueType(ValueType value) {
        valueType = value;
    }

    /*@Override
    public TypeChunk getTypeChunk() {
        return null;
    }

    @Override
    public void setTypeChunk(TypeChunk value) {

    }
*/
    @Override
    public boolean isComplex() {
        return false;
    }

    @Override
    public void setIsComplex(boolean isComplex) {

    }

    @Override
    public boolean isChildren() {
        return true;
    }

    @Override
    public void setIsChildren(boolean isChildren) {

    }

    @Override
    public String toString() {
        return getName();
    }
}
