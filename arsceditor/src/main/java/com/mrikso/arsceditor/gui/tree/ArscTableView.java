package com.mrikso.arsceditor.gui.tree;

import com.mrikso.arsceditor.gui.MainWindow;
import com.mrikso.arsceditor.gui.dialogs.ErrorDialog;
import com.mrikso.arsceditor.gui.dialogs.ResourceEditDialog;
import com.mrikso.arsceditor.gui.treetable.JTreeTable;
import com.mrikso.arsceditor.gui.treetable.TreeTableModel;
import com.mrikso.arsceditor.intrefaces.TableChangedListener;
import com.mrikso.arsceditor.valueeditor.Converter;
import com.mrikso.arsceditor.valueeditor.ValueHelper;
import com.mrikso.arsceditor.valueeditor.ValueType;
import org.jetbrains.annotations.Nullable;
import com.google.devrel.gmscore.tools.apk.arsc.BinaryResourceValue;
import com.google.devrel.gmscore.tools.apk.arsc.ResourceTableChunk;
import com.google.devrel.gmscore.tools.apk.arsc.StringPoolChunk;
import com.google.devrel.gmscore.tools.apk.arsc.TypeChunk;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ArscTableView extends JTreeTable implements MouseListener, ResourceEditDialog.ValueChangedListener {

    protected final MainWindow mainWindow;
    protected TreeTableModel treeTableModel;
    private TableChangedListener tableChangedListener;
    private JTableTreeNode selectedNode;

    public ArscTableView(MainWindow window) {
        super();
        this.mainWindow = window;
        setDefaultRenderer(Object.class, new ArscStringRenderer());
        addMouseListener(this);
        getTree().setCellRenderer(new ArscTreeCellRenderer());
    }

    public void setTableChangedListener(TableChangedListener tableChangedListener) {
        this.tableChangedListener = tableChangedListener;
    }

    public void setTreeTableModel(TreeTableModel treeTableModel) {
        this.treeTableModel = treeTableModel;
    }

    public void updateTable() {
        updateTable(treeTableModel);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = getTree().getRowForLocation(e.getX(), e.getY());
        if (row >= 0) {
           /* if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                DefaultMutableTreeNode selectedNode = getSelectedNode();
                if (selectedNode != null) {
                    if (selectedNode instanceof ResourceModel.ResourceDirectory) {
                        ResourceModel.ResourceDirectory resourceDirectory = (ResourceModel.ResourceDirectory) selectedNode;
                        System.out.println(resourceDirectory.getName());
                    } else if (selectedNode instanceof ResourceModel.ResourceEntry) {
                        ResourceModel.ResourceEntry resourceEntry = (ResourceModel.ResourceEntry) selectedNode;
                        System.out.println(resourceEntry.getName());
                    }
                }

            } else*/
            if (SwingUtilities.isRightMouseButton(e)) {
                getTree().setSelectionRow(row);
                selectedNode = getSelectedNode();
                if (selectedNode != null) {
                    if (!selectedNode.isRoot()) {
                        ResourceEditDialog resourceEditDialog = new ResourceEditDialog(mainWindow, selectedNode);
                        resourceEditDialog.setValueChangedListener(this);
                        resourceEditDialog.showDialog();
                    }
                }
                //  popupMenu.show(tree, e.getX(), e.getY());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Nullable
    public JTableTreeNode getSelectedNode() {
        TreePath path = getTree().getSelectionPath();
        if (path != null) {
            Object component = path.getLastPathComponent();
            return component instanceof JTableTreeNode ? (JTableTreeNode) component : null;
        }
        return null;
    }

    @Override
    public void onResourceEdited(String name, String id, String value, ValueType resourceType, boolean isChildren) {
        try {
            selectedNode.setId(id);
            selectedNode.setName(name);
            selectedNode.setValue(value);
            selectedNode.setValueType(resourceType);

            StringPoolChunk poolChunk = ValueHelper.getStringPoolChunk();

            int index = selectedNode.getEntryIndex();

            TypeChunk.Entry entry = ValueHelper.getTypeChunk().getEntries().get(index);

            // editing children node on complex value
            if (isChildren) {
                if (resourceType == ValueType.TYPE_STRING) {
                    // update the string value
                    poolChunk.updateString(selectedNode.getValueIndex(), value);

                    // update the name
                    //entry.setKey(selectedNode.getNameIndex(), name);
                } else {
                    BinaryResourceValue resourceValue = Converter.convertValue(value, resourceType);
                    entry.updateValue(selectedNode.getEntryChildrenIndex(), resourceValue);
                }
            } else {
                if (resourceType == ValueType.TYPE_STRING) {
                    // update the string value
                    poolChunk.updateString(selectedNode.getValueIndex(), value);

                    // update the name
                    entry.updateKey(selectedNode.getNameIndex(), name);
                } else if (selectedNode.isComplex()) {
                    // update the name in complex values
                    entry.updateKey(selectedNode.getNameIndex(), name);
                } else {
                    // not complex
                    BinaryResourceValue resourceValue = Converter.convertValue(value, resourceType);
                    entry.updateValue(resourceValue);
                }
            }

            // update the entry
            ValueHelper.getTypeChunk().overrideEntry(index, entry);
            if (tableChangedListener != null)
                tableChangedListener.tableChanged();
        }catch (Exception ex){
            ex.printStackTrace();
            new ErrorDialog(mainWindow, ex);
        }
    }

    static class ArscStringRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        public ArscStringRenderer() {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value != null) {
                setText(value.toString());
            } else {
                setText(null);
            }
            return this;
        }

    }
}
