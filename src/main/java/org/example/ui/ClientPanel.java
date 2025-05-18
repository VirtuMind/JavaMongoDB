package org.example.ui;

import org.example.dao.ClientDao;
import org.example.model.Client;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ClientPanel extends JPanel {
    private ClientDao dao;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField nomField, prenomField, adresseField, telephoneField, codePostalField;

    public ClientPanel() {
        dao = new ClientDao("mongodb://localhost:27017", "javadb", "client");
        setLayout(new BorderLayout());
        buildUI();
        loadData();
    }

    private void buildUI() {
        JPanel inputPanel = new JPanel(new FlowLayout());
        nomField = new JTextField(8);
        prenomField = new JTextField(8);
        adresseField = new JTextField(10);
        telephoneField = new JTextField(8);
        codePostalField = new JTextField(5);
        inputPanel.add(new JLabel("Nom:")); inputPanel.add(nomField);
        inputPanel.add(new JLabel("Prénom:")); inputPanel.add(prenomField);
        inputPanel.add(new JLabel("Adresse:")); inputPanel.add(adresseField);
        inputPanel.add(new JLabel("Téléphone:")); inputPanel.add(telephoneField);
        inputPanel.add(new JLabel("Code Postal:")); inputPanel.add(codePostalField);

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(this::addClient);
        inputPanel.add(addBtn);

        tableModel = new DefaultTableModel(new Object[]{"ID","Nom","Prénom","Adresse","Téléphone","Code Postal", "Actions"},0) {
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(table);
        add(inputPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

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
        List<Client> list = dao.getAllClients();
        for (Client c : list) {
            tableModel.addRow(new Object[]{c.getId().toHexString(), c.getNom(), c.getPrenom(), c.getAdresse(), c.getTelephone(), c.getCodePostal()});
        }
    }

    private void addClient(ActionEvent e) {
        try{
            Client c = new Client(nomField.getText(), prenomField.getText(), adresseField.getText(), telephoneField.getText(), codePostalField.getText());
            dao.createClient(c);
            loadData();
        }
        catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Téléphone et code postal doivent etre des nombres", "Données invalides", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    // read the row’s values and call DAO.updateClient, then reload
    private void applyUpdate(int row) {
        ObjectId id = new ObjectId((String) tableModel.getValueAt(row, 0));
        Client c = new Client(
                (String) tableModel.getValueAt(row, 1),
                (String) tableModel.getValueAt(row, 2),
                (String) tableModel.getValueAt(row, 3),
                (String) tableModel.getValueAt(row, 4),
                (String) tableModel.getValueAt(row, 5)
        );
        c.setId(id);
        dao.updateClient(c);
        loadData();
    }

    // call DAO.deleteClient, then reload
    private void applyDelete(int row) {
        ObjectId id = new ObjectId((String) tableModel.getValueAt(row, 0));
        dao.deleteClient(id);
        loadData();
    }

}
