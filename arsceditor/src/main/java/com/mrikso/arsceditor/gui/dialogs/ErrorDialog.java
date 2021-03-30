package com.mrikso.arsceditor.gui.dialogs;

import com.google.common.base.Throwables;

import javax.swing.*;
import java.awt.*;

public class ErrorDialog extends JDialog {

    public ErrorDialog(JFrame owner, Exception error){
        super(owner);

        JPanel pContent = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea(30, 70);
        textArea.setEditable(false);
        textArea.setText(Throwables.getStackTraceAsString(error));
        textArea.setCaretPosition(0);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(l-> setVisible(false));
        JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pButtons.add(okButton);

        pContent.add(scrollPane, BorderLayout.CENTER);
        pContent.add(pButtons, BorderLayout.SOUTH);

        getContentPane().add(pContent);

        pack();
        ///setSize(new Dimension(100, 300));
        setModal(true);
        setResizable(false);
        setTitle("Error");
        setLocationRelativeTo(owner);
        setVisible(true);
    }

}
