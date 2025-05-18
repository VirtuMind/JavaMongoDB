package org.example.ui;

import org.example.dao.CommandeDao;
import org.example.dao.LigneCmdDao;
import org.example.model.Commande;
import org.bson.types.ObjectId;
import org.example.model.LigneCmd;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CommandePanel extends JPanel {
    private CommandeDao commandeDao;
    private LigneCmdDao ligneCmdDao;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField numField, dateField, adresseField, clientIdField, montantField, articleIdField, quanitieField;
    // private SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");

    public CommandePanel() {
        commandeDao = new CommandeDao("mongodb://localhost:27017", "javadb", "commande");
        ligneCmdDao = new LigneCmdDao("mongodb://localhost:27017", "javadb", "ligne_cmd");
        setLayout(new BorderLayout());
        buildUI();
        loadData();
    }

    private void buildUI() {
        JPanel inputPanel = new JPanel(new FlowLayout());
        numField = new JTextField(5);
        dateField = new JTextField(8);
        adresseField = new JTextField(12);
        clientIdField = new JTextField(12);
        montantField = new JTextField(5);
        articleIdField = new JTextField(12);
        quanitieField = new JTextField(5);
        inputPanel.add(new JLabel("Num:")); inputPanel.add(numField);
        inputPanel.add(new JLabel("Date (yyyy-MM-dd):")); inputPanel.add(dateField);
        inputPanel.add(new JLabel("Adresse Livraison:")); inputPanel.add(adresseField);
        inputPanel.add(new JLabel("ID Client:")); inputPanel.add(clientIdField);
        inputPanel.add(new JLabel("ID Article:")); inputPanel.add(articleIdField);
        inputPanel.add(new JLabel("Quantité:")); inputPanel.add(quanitieField);
        inputPanel.add(new JLabel("Montant:")); inputPanel.add(montantField);

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(this::addCommande);
        inputPanel.add(addBtn);

        tableModel = new DefaultTableModel(
            new Object[]{"ID","Num","Date","Adresse","ClientID","Montant", "Actions"}, 0
        ) {
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // set up custom renderer/editor for the last column
        int actionCol = table.getColumnCount() - 1;
        table.getColumnModel().getColumn(actionCol).setCellRenderer(new ActionCellRenderer());
        table.getColumnModel().getColumn(actionCol).setCellEditor(new ActionCellEditor(
                this::applyUpdate,
                this::applyDelete
        ));

    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Commande> list = commandeDao.getAllCommandes();
        for (Commande c : list) {
            tableModel.addRow(new Object[]{
                    c.getId().toHexString(),
                    c.getNum(),
                    c.getDate(),
                    c.getAdresseLivraison(),
                    c.getClientId() != null ? c.getClientId().toHexString() : "",
                    c.getMontant()
            });
        }
    }

    private void addCommande(ActionEvent e) {
        try {
            Commande c = new Commande(
                    numField.getText(),
                    dateField.getText(),
                    adresseField.getText(),
                    clientIdField.getText().isEmpty() ? null : new ObjectId(clientIdField.getText()),
                    Double.parseDouble(montantField.getText())
            );
            commandeDao.createCommande(c);
            LigneCmd lc = new LigneCmd(
                    c.getId(),
                    articleIdField.getText().isEmpty() ? null : new ObjectId(articleIdField.getText()),
                    Integer.parseInt(quanitieField.getText())
            );

            ligneCmdDao.createLigneCmd(lc);
            loadData();
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantité et Montant doivent etre des nombres", "Données invalides", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "ObjectId Invalid: " + ex.getMessage(), "Données invalides", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyUpdate(int row) {
        ObjectId id = new ObjectId((String) tableModel.getValueAt(row, 0));
        Commande c = new Commande(
                (String) tableModel.getValueAt(row, 1),
                (String) tableModel.getValueAt(row, 2),
                (String) tableModel.getValueAt(row, 3),
                new ObjectId((String) tableModel.getValueAt(row, 4)),
                (Double) tableModel.getValueAt(row, 5)
        );
        c.setId(id);
        commandeDao.updateCommande(c);

        // Update the LigneCmd associated with this Commande
        LigneCmd lc = new LigneCmd(
                c.getId(),
                articleIdField.getText().isEmpty() ? null : new ObjectId(articleIdField.getText()),
                Integer.parseInt(quanitieField.getText())
        );
        lc.setId(id);
        ligneCmdDao.updateLigneCmd(lc);

        loadData();
    }

    public void applyDelete(int row) {
        ObjectId id = new ObjectId((String) tableModel.getValueAt(row, 0));
        commandeDao.deleteCommande(id);
        ligneCmdDao.deleteLigneCmdByCommandeId(id);
        loadData();
    }



}