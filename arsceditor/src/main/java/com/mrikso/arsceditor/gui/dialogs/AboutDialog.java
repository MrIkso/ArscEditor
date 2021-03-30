package com.mrikso.arsceditor.gui.dialogs;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {
    /**
     * @param owner
     */
    public AboutDialog(final JFrame owner) {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);

        initComponents();
        pack();
        setTitle("About");
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    private void initComponents() {
        setLayout(new FlowLayout());

        final JButton buttonClose = new JButton("Close");
        buttonClose.addActionListener(e -> setVisible(false));

        final JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));

        final JLabel label = new JLabel("Arsc Editor");
        label.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(new JLabel(String.format("Version: %s", "1.0.0")));
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(new JLabel("Author: Mr Ikso"));
        listPane.add(Box.createRigidArea(new Dimension(0, 20)));
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(new JLabel("Free open source tool to editing *.arsc files."));
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));

        listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(buttonClose);

        final JPanel root = new JPanel();
        root.setLayout(new BorderLayout());
        root.add(Box.createVerticalGlue());
        root.add(listPane, BorderLayout.CENTER);
        root.add(buttonPane, BorderLayout.SOUTH);
        add(root);

    }

    public void showDialog() {
        setVisible(true);
    }
}
