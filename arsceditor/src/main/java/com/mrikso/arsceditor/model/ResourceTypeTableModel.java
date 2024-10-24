package com.mrikso.arsceditor.model;

import com.mrikso.arsceditor.gui.tree.JTableTreeNode;
import com.mrikso.arsceditor.gui.treetable.DynamicTreeTableModel;
import com.mrikso.arsceditor.gui.treetable.TreeTableModel;

public class ResourceTypeTableModel extends DynamicTreeTableModel {
    /**
     * Names of the columns.
     */
    private static final String[] columnNames = { "ID", "Name",
            "Value"};
    /**
     * Method names used to access the data to display.
     */
    private static final String[] methodNames = { "getId", "getName",
            "getValue" };
    /**
     * Method names used to set the data.
     */
    private static final String[] setterMethodNames = { "setId","setName",
            "setValue"};
    /**
     * Classes presenting the data.
     */
    private static final Class<?>[] classes = { TreeTableModel.class,
            String.class, String.class, String.class};
    /**
     * Constructor for creating a DynamicTreeTableModel.
     *
     * @param root
     */
    public ResourceTypeTableModel(JTableTreeNode root) {
        super(root, columnNames, methodNames, setterMethodNames, classes);
    }


    @Override
    public boolean isCellEditable(Object node, int column) {
        switch (column) {
            case 0:
                // Allow editing of the name, as long as not the root.
                return (node != getRoot());
            case 1:
                // Allow editing of the location, as long as not a
                // directory
                return (node instanceof ResourceModel);
            default:
                // Don't allow editing of the date fields.
                return false;
        }
    }

}
