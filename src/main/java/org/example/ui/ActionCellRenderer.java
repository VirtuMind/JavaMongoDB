package org.example.ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ActionCellRenderer extends JPanel implements TableCellRenderer {
    private final JButton upd = new JButton("Update");
    private final JButton del = new JButton("Delete");

    public ActionCellRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        add(upd);
        add(del);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int col
    ) {
        return this;
    }
}