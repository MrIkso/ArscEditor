package com.mrikso.arsceditor;

import com.mrikso.arsceditor.gui.MainWindow;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        try {
            // UIManager.setDefaultLookAndFeelDecorated
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.invokeLater(MainWindow::new);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
