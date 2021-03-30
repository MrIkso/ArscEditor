package com.mrikso.arsceditor.gui.dialogs;

import com.mrikso.arsceditor.gui.tree.JTableTreeNode;
import com.mrikso.arsceditor.valueeditor.ValueType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ResourceEditDialog extends JDialog implements ItemListener {

    private ValueType valueType;
    private final String resId;
    private final String resName;
    private final String resValue;

    private JTextField nameTextField;
    private JTextArea valueArea;
    private ValueChangedListener valueChangedListener;
    private boolean isComplex;
    private boolean isChildren;

    public ResourceEditDialog(JFrame parent, JTableTreeNode defaultMutableTreeNode) {
        super(parent);

        valueType = defaultMutableTreeNode.getValueType();
        resId = defaultMutableTreeNode.getId();
        resName = defaultMutableTreeNode.getName();
        resValue = defaultMutableTreeNode.getValue();
        isComplex =  defaultMutableTreeNode.isComplex();
        isChildren = defaultMutableTreeNode.isChildren();

        initUI();
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setSize(new Dimension(500, 300));
        setResizable(false);
        setTitle("Edit value");
        setModal(true);
        setLocationRelativeTo(parent);
    }

    private void initUI() {

        // CompactGrid layout is from http://sourceforge.net/projects/swinglib/
        JPanel pFields = new JPanel(new CompactGridLayout(2, 6, 12));
        JTextField idTextFiled = new JTextField(30);
        int defaultWidth = (int) idTextFiled.getPreferredSize().getWidth();

        if (resId != null) {
            idTextFiled.setText(resId);
            idTextFiled.setEditable(false);
            pFields.add(new JLabel("ID:"));
            pFields.add(idTextFiled);
        }

        nameTextField = new JTextField();
        nameTextField.setPreferredSize(new Dimension(defaultWidth, nameTextField.getPreferredSize().height));
        nameTextField.setText(resName);

        pFields.add(new JLabel("Name:"));
        pFields.add(nameTextField);

        if (valueType != null) {
            String[] smaliSearchTypes = {"String", "Reference",
                    "Attribute", "Color", "Int-dec", "Int-hex", "Boolean", "Float", "Dimension", "Fraction", "DynamicReference"};
            JComboBox<String> comboBoxTypes = new JComboBox<>(smaliSearchTypes);
            comboBoxTypes.setSelectedIndex(valueType.getValue());
            comboBoxTypes.setEditable(false);
            comboBoxTypes.addItemListener(this);
            comboBoxTypes.setPreferredSize(new Dimension(defaultWidth, comboBoxTypes.getPreferredSize().height));
            pFields.add(new JLabel("Type:"));
            pFields.add(comboBoxTypes);
        }

        if (resValue != null) {
            JScrollPane scrollPane;
            if (valueType == ValueType.TYPE_STRING) {
                valueArea = new JTextArea(resValue, 10, 27);
                valueArea.setLineWrap(true);
                valueArea.setWrapStyleWord(true);
                valueArea.setFont(idTextFiled.getFont());
            } else {
                valueArea = new JTextArea(resValue, 1, 27);
                valueArea.setLineWrap(true);
                valueArea.setWrapStyleWord(true);
                valueArea.setFont(idTextFiled.getFont());
                valueArea.getDocument().putProperty("filterNewLines", Boolean.TRUE);
                // valueField = new JTextField();
                // valueField.setPreferredSize(new Dimension(defaultWidth, valueField.getPreferredSize().height));
                // valueField.setText(resValue);
            }
            scrollPane = new JScrollPane(valueArea);

            pFields.add(new JLabel("Value:"));
            //if (valueType == ValueType.TYPE_STRING) {
            pFields.add(scrollPane);
            // } else {
            // pFields.add(valueField);
            // }
        }

        pFields.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JButton okButton = new JButton("OK");
        okButton.addActionListener(l -> {
            save();
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
    }

    public void setValueChangedListener(ValueChangedListener valueChangedListener) {
        this.valueChangedListener = valueChangedListener;
    }

    private void save() {
        if (valueChangedListener != null) {
            String resName = nameTextField.getText();
            if(isComplex){
                valueChangedListener.onResourceEdited(resName, resId, null, null, false);
            }else {
                String resValue = valueArea.getText();
                valueChangedListener.onResourceEdited(resName, resId, resValue, valueType, isChildren);
            }
        }
        setVisible(false);
    }

    public void showDialog() {
        setVisible(true);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            switch (e.getItem().toString()) {
                case "String":
                    valueType = ValueType.TYPE_STRING;
                    break;
                case "Reference":
                    valueType = ValueType.TYPE_REFERENCE;
                    break;
                case "Attribute":
                    valueType = ValueType.TYPE_ATTRIBUTE;
                    break;
                case "Color":
                    valueType = ValueType.TYPE_COLOR;
                    break;
                case "Int-dec":
                    valueType = ValueType.TYPE_INT_DEC;
                    break;
                case "Int-hex":
                    valueType = ValueType.TYPE_INT_HEX;
                    break;
                case "Boolean":
                    valueType = ValueType.TYPE_BOOLEAN;
                    break;
                case "Float":
                    valueType = ValueType.TYPE_FLOAT;
                    break;
                case "Dimension":
                    valueType = ValueType.TYPE_DIMENSION;
                    break;
                case "Fraction":
                    valueType = ValueType.TYPE_FRACTION;
                    break;
                case "DynamicReference":
                    valueType = ValueType.TYPE_DYNAMIC_REFERENCE;
                    break;
            }
        }
    }

    public interface ValueChangedListener {
        public void onResourceEdited(String name, String id, String value, ValueType resourceType, boolean isChildren);
    }
}
