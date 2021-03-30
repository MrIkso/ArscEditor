package com.mrikso.arsceditor.gui.tree;

import com.mrikso.arsceditor.valueeditor.ValueType;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class JTableTreeNode extends DefaultMutableTreeNode {

    public JTableTreeNode() {
        super();
    }

    public JTableTreeNode(String id) {
        super(id);
    }

    public abstract String getName();

    public abstract void setName(String name);

    public abstract void setId(String id);

    public abstract String getId();

    public abstract void setNameIndex(int index);

    public abstract int getNameIndex();

    public abstract void setValueIndex(int index);

    public abstract int getValueIndex();

    public abstract void setEntryIndex(int index);

    public abstract int getEntryIndex();

    public abstract void setEntryChildrenIndex(int index);

    public abstract int getEntryChildrenIndex();

    public abstract String getValue();

    public abstract void setValue(String value);

    public abstract ValueType getValueType();

    public abstract void setValueType(ValueType value);

  //  public abstract TypeChunk getTypeChunk();

 //   public abstract void setTypeChunk(TypeChunk value);

    public abstract boolean isComplex();

    public abstract void setIsComplex(boolean isComplex);

    public abstract boolean isChildren();

    public abstract void setIsChildren(boolean isChildren);
}
