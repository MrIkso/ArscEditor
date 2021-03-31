package com.mrikso.arsceditor.gui.dialogs;

import com.mrikso.arsceditor.gui.tree.ArscNode;

import javax.swing.*;
import java.awt.*;

public class PackageEditDialog extends JDialog {

    private ValueChangedListener valueChangedListener;

    public PackageEditDialog(JFrame parent, ArscNode node) {
        super(parent);

        JTextField packageNameFiled = new JTextField(20);
        packageNameFiled.setText(node.getPackageName());

        JTextField packageIdField = new JTextField(20);
        packageIdField.setText(String.valueOf(node.getId()));
        packageIdField.setEditable(false);

        // CompactGrid layout is from http://sourceforge.net/projects/swinglib/
        JPanel pFields = new JPanel(new CompactGridLayout(2, 6, 12));
        pFields.add(new JLabel("Package Name:"));
        pFields.add(packageNameFiled);
        pFields.add(new JLabel("Package Id:"));
        pFields.add(packageIdField);
        pFields.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JButton okButton = new JButton("OK");
        okButton.addActionListener(l -> {
            save(packageNameFiled.getText(), Integer.parseInt(packageIdField.getText()));
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(l -> {
            setVisible(false);
        });
        JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pButtons.add(okButton);
        pButtons.add(cancelButton);

        JPanel pContent = new JPanel(new BorderLayout());
        pContent.add(pFields, BorderLayout.CENTER);
        pContent.add(pButtons, BorderLayout.SOUTH);

        setContentPane(pContent);
        pack();
        //  setSize(new Dimension(300, 100));
        setTitle("Edit Package");
        setLocationRelativeTo(parent);
        setModal(true);
        setResizable(false);
    }


    private void save(String name, int id) {
        if (valueChangedListener != null) {
            valueChangedListener.onEdited(name, id);
        }
        setVisible(false);
    }

    public void setValueChangedListener(ValueChangedListener valueChangedListener) {
        this.valueChangedListener = valueChangedListener;
    }

    public void showDialog() {
        setVisible(true);
    }

    public interface ValueChangedListener {
        public void onEdited(String name, int id);
    }
}
