package com.mrikso.arsceditor.gui.tree;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public abstract class JNode extends DefaultMutableTreeNode {

    public JNode(Object name, boolean allowsChildren) {
        super(name, allowsChildren);
    }

    public JNode() {
        super();
    }

    public JNode(Object name) {
        super(name);
    }

    public abstract Icon getIcon();

    @NotNull
    public abstract String getName();

}
