package com.mrikso.arsceditor.gui.tree;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class ArscTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(@NotNull JTree tree,
                                                  Object value,
                                                  boolean selected,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus) {
       super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (value instanceof ArscNode) {
            ArscNode node = (ArscNode) value;
            setText(node.getName());
            setIcon(node.getIcon());
        }
        else {
            if(value!= null) {
                setText(value.toString());
            }else {
                setText(null);
            }
            setIcon(new TreeIcon());
        }
        return this;
    }


    static class TreeIcon implements Icon {

        private int SIZE = 0;

        public TreeIcon() {
        }

        public int getIconWidth() {
            return SIZE;
        }

        public int getIconHeight() {
            return SIZE;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            //System.out.println(c.getWidth() + " " + c.getHeight() + " " + x + " " + y);
        }
    }
}
