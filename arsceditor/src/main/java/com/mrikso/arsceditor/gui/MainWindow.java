package com.mrikso.arsceditor.gui;

import com.google.common.io.Files;
import com.mrikso.arsceditor.gui.dialogs.AboutDialog;
import com.mrikso.arsceditor.gui.dialogs.ErrorDialog;
import com.mrikso.arsceditor.gui.tree.ArscTableView;
import com.mrikso.arsceditor.gui.tree.ArscTreeView;
import com.mrikso.arsceditor.intrefaces.TableChangedListener;
import com.mrikso.arsceditor.valueeditor.ArscWriter;
import com.mrikso.arsceditor.valueeditor.ValueHelper;
import com.google.devrel.gmscore.tools.apk.arsc.BinaryResourceFile;
import com.google.devrel.gmscore.tools.apk.arsc.Chunk;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.util.List;

public class MainWindow extends JFrame implements TableChangedListener {

    private ArscTreeView treeView;
    private JSplitPane splitPane;
    private JMenuItem saveAs;
    private String openedFilePath;

    public MainWindow() {
        loadComponent();
    }

    protected void loadComponent() {
        ArscTableView arscTableView = new ArscTableView(this);
        arscTableView.setTableChangedListener(this);

        treeView = new ArscTreeView(this, null, arscTableView);
        treeView.setTableChangedListener(this);

        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel();
        panel.setLayout(layout);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(treeView), new JScrollPane(arscTableView));

        initSplitPane();

        panel.add(splitPane, BorderLayout.CENTER);

        this.setJMenuBar(createMenuBar());
        this.getContentPane().add(panel);

        this.setPreferredSize(new Dimension(900, 600));
        this.setMinimumSize(new Dimension(600, 600));
        this.setLocationRelativeTo(null);
        this.setTitle("Arsc Editor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Creates a menu bar.
     */
    protected JMenuBar createMenuBar() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem menuItem;

        menuItem = new JMenuItem("Open");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                InputEvent.CTRL_MASK));
        menuItem.addActionListener(ae -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Android resource files", "arsc");
            fileChooser.addChoosableFileFilter(filter);
            int result = fileChooser.showOpenDialog(getRootPane());

            if (result == JFileChooser.APPROVE_OPTION) {
                openedFilePath = fileChooser.getSelectedFile().getPath();
                openFile(openedFilePath);
            }
        });

        saveAs = new JMenuItem("Save");
        saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                InputEvent.CTRL_MASK));
        saveAs.setEnabled(false);
        saveAs.addActionListener(l -> {
            selectPathToSave();
        });

        fileMenu.add(menuItem);
        fileMenu.addSeparator();
        fileMenu.add(saveAs);

        menuItem = new JMenuItem("Exit");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                InputEvent.CTRL_MASK));
        menuItem.addActionListener(ae -> System.exit(0));
        fileMenu.add(menuItem);

        JMenu aboutMenu = new JMenu("About");
        menuItem = new JMenuItem("Open source code");
        menuItem.addActionListener(l -> gitHomepage());
        aboutMenu.add(menuItem);
        aboutMenu.addSeparator();
        menuItem = new JMenuItem("Info");
        menuItem.addActionListener(l -> new AboutDialog(MainWindow.this).showDialog());
        aboutMenu.add(menuItem);

        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);

        return menuBar;
    }

    private void gitHomepage() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/MrIkso/ArscEditor"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(), this.getTitle(),
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void initSplitPane() {
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0);
        Dimension minimumSize = new Dimension(200, 600);
        splitPane.getLeftComponent().setMinimumSize(minimumSize);
        splitPane.getRightComponent().setMinimumSize(minimumSize);
    }

    @Override
    public void tableChanged() {
        saveAs.setEnabled(true);
    }

    private void openFile(String path) {
        new SwingWorker<List<Chunk>, Chunk>() {
            @Override
            protected List<Chunk> doInBackground() throws Exception {
                byte[] resContents =
                        java.nio.file.Files.readAllBytes(Paths.get(path));
                BinaryResourceFile binaryRes = new BinaryResourceFile(resContents);
                return binaryRes.getChunks();
            }

            @Override
            protected void process(List<Chunk> chunks) {
                super.process(chunks);
            }

            @Override
            protected void done() {
                try {
                    treeView.setRootWithFile(get(), new File(path).getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    new ErrorDialog(MainWindow.this, e);
                }
            }
        }.execute();

    }

    private void selectPathToSave() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Android resource files", "arsc");
        fileChooser.addChoosableFileFilter(filter);
        String fileName = Files.getNameWithoutExtension(openedFilePath);

        fileChooser.setSelectedFile(new File(new File(openedFilePath).getParent() + "/"
                + fileName + "_mod.arsc"));
        int result = fileChooser.showSaveDialog(getRootPane());

        if (result == JFileChooser.APPROVE_OPTION) {
            saveFile(fileChooser.getSelectedFile());
        }
    }

    private void saveFile(File path) {
        new SwingWorker<Boolean, Integer>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                new ArscWriter(ValueHelper.getResourceTableChunk(), path).write();
                return true;
            }

            @Override
            protected void done() {
                try {
                    saveAs.setEnabled(false);
                    JOptionPane.showMessageDialog(MainWindow.this, "File saved!");
                } catch (Exception e) {
                    e.printStackTrace();
                    new ErrorDialog(MainWindow.this, e);
                }
            }
        }.execute();

    }
}
