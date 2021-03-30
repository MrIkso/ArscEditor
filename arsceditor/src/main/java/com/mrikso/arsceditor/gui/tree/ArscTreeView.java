package com.mrikso.arsceditor.gui.tree;

import com.google.devrel.gmscore.tools.apk.arsc.*;
import com.mrikso.arsceditor.gui.MainWindow;
import com.mrikso.arsceditor.gui.dialogs.ErrorDialog;
import com.mrikso.arsceditor.gui.dialogs.PackageEditDialog;
import com.mrikso.arsceditor.model.ResourceModel;
import com.mrikso.arsceditor.model.ResourceTypeTableModel;
import com.mrikso.arsceditor.valueeditor.ValueHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pink.madis.apk.arsc.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ArscTreeView extends JTree implements MouseListener, PackageEditDialog.ValueChangedListener {

    protected ArscTableView arscTableView;
    protected MainWindow mainWindow;
    @NotNull
    private ArscNode selectedNode;
    private ResourceTableChunk resourceTableChunk;

    public ArscTreeView() {

    }

    public ArscTreeView(MainWindow mainWindow, DefaultMutableTreeNode root, ArscTableView arscTableView) {
        super(root);
        this.mainWindow = mainWindow;
        this.arscTableView = arscTableView;
        setShowsRootHandles(true);
        setCellRenderer(new ArscTreeCellRenderer());
        addMouseListener(this);
    }

    public void setRootWithFile(@NotNull List<Chunk> chunks, String fileName) {
        try {
            ArscNode root = new ArscNode(fileName);
            arscReader(chunks, root);
            setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void arscReader(List<Chunk> chunks, ArscNode root) throws IOException {
        if (chunks.isEmpty()) {
            throw new IOException("no chunks");
        }

        if (!(chunks.get(0) instanceof ResourceTableChunk)) {
            throw new IOException("no res table chunk");
        }

        for (Chunk chunk : chunks) {
            resourceTableChunk = (ResourceTableChunk) chunk;
            ValueHelper.setResourceTableChunk(resourceTableChunk);
            ValueHelper.setStringPoolChunk(resourceTableChunk.getStringPool());

            // getting all packages
            for (PackageChunk packageChunk : resourceTableChunk.getPackages()) {
                String packageName = packageChunk.getPackageName();
                ArscNode resTableNode = new ArscNode(String.format("%s (%s)", packageName, packageChunk.getId()), packageName);
                resTableNode.setId(packageChunk.getId());
                resTableNode.setRootPackage(true);
                resTableNode.setPackageChunk(packageChunk);
                // getting all types
                for (TypeSpecChunk typeSpecChunk : packageChunk.getTypeSpecChunks()) {
                    String typeName = typeSpecChunk.getTypeName();
                    ArscNode typeNode = new ArscNode(typeName);

                    // getting all configs from types
                    for (TypeChunk typeChunk : packageChunk.getTypeChunks(typeSpecChunk.getId())) {
                        String config = typeChunk.getConfiguration().toString();
                        ArscNode configNode;
                        if (config.equals("default")) {
                            configNode = new ArscNode(config);
                        } else {
                            configNode = new ArscNode(String.format("%s-%s", typeName, config));
                        }
                        //configNode.setStringPool(resourceTableChunk.getStringPool());
                        configNode.setPackageChunk(packageChunk);
                        configNode.setTypeSpec(typeSpecChunk);
                        configNode.setType(typeChunk);
                        typeNode.add(configNode);
                    }
                    resTableNode.add(typeNode);
                }

                root.add(resTableNode);
            }

        }

    }

    private void setRoot(DefaultMutableTreeNode root) {
        DefaultTreeModel treeModel = (DefaultTreeModel) getModel();
        treeModel.setRoot(root);
    }

    @Nullable
    public ArscNode getSelectedNode() {
        TreePath path = getSelectionPath();
        assert path != null;
        Object component = path.getLastPathComponent();
        return component instanceof ArscNode ? (ArscNode) component : null;
    }

    private void openNode() {
        final ArscNode node = Objects.requireNonNull(getSelectedNode());

        if (!node.children().hasMoreElements()) {
            arscTableView.setTreeTableModel(createModel(node));
            arscTableView.updateTable();
        }
    }

    protected ResourceTypeTableModel createModel(ArscNode node) {
        ResourceModel resourceModel = new ResourceModel(ValueHelper.getStringPoolChunk(), node.getPackageChunk(),
                node.getTypeSpec(), node.getTypeChunk(), node.getName());
        resourceModel.readTable();
        return new ResourceTypeTableModel(resourceModel.getRoot());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = getRowForLocation(e.getX(), e.getY());
        if (row >= 0) {
            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                Thread someThread = new Thread(this::openNode);
                someThread.start();

            } else if (SwingUtilities.isRightMouseButton(e)) {
                setSelectionRow(row);
                selectedNode = getSelectedNode();
                if (selectedNode != null)
                    if (selectedNode.isRootPackage()) {
                        PackageEditDialog packageEditDialog = new PackageEditDialog(mainWindow, selectedNode);
                        packageEditDialog.setValueChangedListener(this);
                        packageEditDialog.showDialog();
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

    @Override
    public void onEdited(String name, int id) {
        try {
            selectedNode.setName(String.format("%s (%d)", name, id));
            selectedNode.setPackageName(name);
            selectedNode.setId(id);
            PackageChunk packageChunk = selectedNode.getPackageChunk();
            packageChunk.setPackageName(name);
        } catch (Exception ex) {
            ex.printStackTrace();
            new ErrorDialog(mainWindow, ex);
        }
    }
}
