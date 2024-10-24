package com.mrikso.arsceditor.gui.dialogs;

import com.google.common.base.Throwables;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class ErrorDialog extends JDialog {

    public ErrorDialog(JFrame owner, Exception error){
        super(owner);

        JPanel pContent = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea(30, 70);
        textArea.setEditable(false);
        textArea.setText(Throwables.getStackTraceAsString(error));
        textArea.setCaretPosition(0);
        textArea.setFont(textArea.getFont().deriveFont(12f));
        JScrollPane scrollPane = new JScrollPane(textArea);
        JButton copyError = new JButton("Copy Error");
        copyError.addActionListener(l-> copyTextToClipboard(Throwables.getStackTraceAsString(error)));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(l-> setVisible(false));
        JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pButtons.add(copyError);
        pButtons.add(okButton);

        pContent.add(scrollPane, BorderLayout.CENTER);
        pContent.add(pButtons, BorderLayout.SOUTH);

        getContentPane().add(pContent);

        pack();
        //setSize(new Dimension(100, 300));
        setModal(true);
        setResizable(false);
        setTitle("Error");
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void copyTextToClipboard(String text){
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(
                        new StringSelection(text),
                        null
                );
    }
}
