package org.example.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.function.Consumer;

public class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
    private final JButton upd = new JButton("Update");
    private final JButton del = new JButton("Delete");
    private final Consumer<Integer> onUpdate;
    private final Consumer<Integer> onDelete;
    private int currentRow;

    public ActionCellEditor(
            Consumer<Integer> onUpdate,
            Consumer<Integer> onDelete
    ) {
        this.onUpdate = onUpdate;
        this.onDelete = onDelete;
        panel.add(upd);
        panel.add(del);

        upd.addActionListener(e -> {
            fireEditingStopped();
            onUpdate.accept(currentRow);
        });
        del.addActionListener(e -> {
            fireEditingStopped();
            onDelete.accept(currentRow);
        });
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int col
    ) {
        currentRow = row;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}