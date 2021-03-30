package com.mrikso.arsceditor.gui.treetable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import java.awt.*;

public class JTreeTable extends JTable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected TreeTableCellRenderer tree;

    public JTreeTable() {
        super();
        init();
    }

    public JTreeTable(TreeTableModel treeTableModel) {
        super();
        init();
        updateTable(treeTableModel);
    }

    private void init() {
        tree = new TreeTableCellRenderer();
        // Force the JTable and JTree to share their row selection models.
        tree.setSelectionModel(new DefaultTreeSelectionModel() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            // Extend the implementation of the constructor, as if:
            /* public this() */ {
                setSelectionModel(listSelectionModel);
            }
        });
        // Make the tree and table row heights the same.
        tree.setRowHeight(getRowHeight());

        // Install the tree editor renderer and editor.
        setDefaultRenderer(TreeTableModel.class, tree);
        setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());

        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
    }

    public void updateTable(TreeTableModel treeTableModel) {
        // Create the tree. It will be used as a renderer and editor.
        tree.setTreeModel(treeTableModel);
        // Install a tableModel representing the visible rows in the tree.
        super.setModel(new TreeTableModelAdapter(treeTableModel, tree));

    }

    /*
     * Workaround for BasicTableUI anomaly. Make sure the UI never tries to
     * paint the editor. The UI currently uses different techniques to paint the
     * renderers and editors and overriding setBounds() below is not the right
     * thing to do for an editor. Returning -1 for the editing row in this case,
     * ensures the editor is never painted.
     */
    public int getEditingRow() {
        return (getColumnClass(editingColumn) == TreeTableModel.class) ? -1
                : editingRow;
    }

    public TreeTableCellRenderer getTree() {
        return tree;
    }

    //
    // The renderer used to display the tree nodes, a JTree.
    //

    public class TreeTableCellRenderer extends JTree implements
            TableCellRenderer {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        protected int visibleRow;
        protected TreeModel model;

        public TreeTableCellRenderer() {
            super();

        }

        public TreeTableCellRenderer(TreeModel model) {
            super(model);
            this.model = model;
        }


        public void setTreeModel(TreeModel model) {
            this.model = model;
            setModel(model);
        }

        @Override
        public void setBounds(int x, int y, int w, int h) {
            super.setBounds(x, 0, w, JTreeTable.this.getHeight());
        }

        @Override
        public void paint(Graphics g) {
            g.translate(0, -visibleRow * getRowHeight());
            super.paint(g);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus, int row,
                                                       int column) {
            if (isSelected)
                setBackground(table.getSelectionBackground());
            else
                setBackground(table.getBackground());

            visibleRow = row;
            return this;
        }
    }

    //
    // The editor used to interact with tree nodes, a JTree.
    //

    public class TreeTableCellEditor extends AbstractCellEditor implements
            TableCellEditor {
        public Component getTableCellEditorComponent(JTable table,
                                                     Object value, boolean isSelected, int r, int c) {
            return tree;
        }
    }

}
