package com.mrikso.arsceditor.gui.treetable;

import javax.swing.event.TableModelListener;
import javax.swing.tree.TreeModel;

public interface TreeTableModel extends TreeModel {
    /**
     * Returns the number of available columns.
     */
    public int getColumnCount();

    /**
     * Returns the name for column number <code>column</code>.
     */
    public String getColumnName(int column);

    /**
     * Returns the type for column number <code>column</code>.
     */
    public Class<?> getColumnClass(int column);

    /**
     * Returns the value to be displayed for node <code>node</code>, at column
     * number <code>column</code>.
     */
    public Object getValueAt(Object node, int column);

    /**
     * Indicates whether the the value for node <code>node</code>, at column
     * number <code>column</code> is editable.
     */
    public boolean isCellEditable(Object node, int column);

    /**
     * Sets the value for node <code>node</code>, at column number
     * <code>column</code>.
     */
    public void setValueAt(Object aValue, Object node, int column);

    /**
     * Adds a listener to the list that is notified each time a change to the
     * data model occurs.
     */
    public void addTableModelListener(TableModelListener l);

    /**
     * Removes a listener from the list that is notified each time a change to
     * the data model occurs.
     */
    public void removeTableModelListener(TableModelListener l);
}
