package org.example.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Atelier 6: Connexion interfaces Java & MongoDB");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Client", new ClientPanel());
        tabs.addTab("Article", new ArticlePanel());
        tabs.addTab("Commande", new CommandePanel());
        add(tabs);
    }
}
