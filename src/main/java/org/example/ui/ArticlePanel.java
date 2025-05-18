package org.example.ui;

import org.example.dao.ArticleDao;
import org.bson.types.ObjectId;
import org.example.model.Article;
import org.example.model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ArticlePanel extends JPanel {
    private ArticleDao dao;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField codeField, designationField, prixField, rayonField, sousRayonField;

    public ArticlePanel() {
        dao = new ArticleDao("mongodb://localhost:27017", "javadb", "article");
        setLayout(new BorderLayout());
        buildUI();
        loadData();
    }

    private void buildUI() {
        JPanel inputPanel = new JPanel(new FlowLayout());
        codeField = new JTextField(5);
        designationField = new JTextField(10);
        prixField = new JTextField(5);
        rayonField = new JTextField(8);
        sousRayonField = new JTextField(8);
        inputPanel.add(new JLabel("Code:")); inputPanel.add(codeField);
        inputPanel.add(new JLabel("Désignation:")); inputPanel.add(designationField);
        inputPanel.add(new JLabel("Prix U:")); inputPanel.add(prixField);
        inputPanel.add(new JLabel("Rayon:")); inputPanel.add(rayonField);
        inputPanel.add(new JLabel("Sous-rayon:")); inputPanel.add(sousRayonField);

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(this::addArticle);
        inputPanel.add(addBtn);

        tableModel = new DefaultTableModel(new Object[]{"ID","Code","Désignation","Prix U","Rayon","Sous-rayon", "Actions"},0) {
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
        List<Article> list = dao.getAllArticles();
        for (Article a : list) {
            tableModel.addRow(new Object[]{a.getId().toHexString(), a.getCode(), a.getDesignation(), a.getPrix_u(), a.getRayon(), a.getSs_rayon()});
        }
    }

    private void addArticle(ActionEvent e) {
        try {
            Article a = new Article(
                codeField.getText(),
                designationField.getText(),
                Double.parseDouble(prixField.getText()),
                rayonField.getText(),
                sousRayonField.getText()
            );
            dao.createArticle(a);
            loadData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Prix U doit etre un nombre", "Données invalides", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // read the row’s values and call DAO.updateClient, then reload
    private void applyUpdate(int row) {
        ObjectId id = new ObjectId((String) tableModel.getValueAt(row, 0));
        Article a = new Article(
                (String) tableModel.getValueAt(row, 1),
                (String) tableModel.getValueAt(row, 2),
                (Double) tableModel.getValueAt(row, 3),
                (String) tableModel.getValueAt(row, 4),
                (String) tableModel.getValueAt(row, 5)
        );
        a.setId(id);
        dao.updateArticle(a);
        loadData();
    }

    // call DAO.deleteClient, then reload
    private void applyDelete(int row) {
        ObjectId id = new ObjectId((String) tableModel.getValueAt(row, 0));
        dao.deleteArticle(id);
        loadData();
    }

}
