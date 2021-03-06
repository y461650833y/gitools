/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.app.wizard.add.data;

import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.filter.DataIntegrationCriteria;
import org.gitools.ui.core.utils.DocumentChangeListener;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.operators.Operator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

/**
 * @noinspection ALL
 */
public class DataIntegrationCriteriaDialog extends javax.swing.JDialog {
    /**
     * A return status code - returned if Cancel button has been pressed
     */
    private static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;
    private boolean noValue;

    private Heatmap heatmap;

    private static class OperatorCellRenderer extends DefaultTableCellRenderer {
        public OperatorCellRenderer() {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Operator op = (Operator) value;
            String opString = op.getAbbreviation();
            if (op.equals(Operator.AND)) {
                opString = "    " + op.getAbbreviation();
            }
            return super.getTableCellRendererComponent(table, opString, isSelected, hasFocus, row, column);
        }
    }

    private static class ComboBoxCellEditor extends DefaultCellEditor {
        public ComboBoxCellEditor(Object[] values) {
            super(new JComboBox(values));
        }
    }

    private final IMatrixLayers layers;
    private final CutoffCmp[] comparators;
    private final String[] operators;

    private final DataIntegrationCriteriaTableModel criteriaModel;

    /**
     * Creates new form FilterDialog
     *
     * @noinspection UnusedDeclaration
     */
    public DataIntegrationCriteriaDialog(Frame parent,
                                         IMatrixLayers layers,
                                         CutoffCmp[] comparators,
                                         String[] operators,
                                         List<DataIntegrationCriteria> initialCriteriaList,
                                         String groupName) {

        super(parent, true);

        this.layers = layers;
        this.comparators = comparators;
        this.operators = operators;

        this.criteriaModel = new DataIntegrationCriteriaTableModel(layers);
        initComponents();

        if (groupName == null) {
            this.noValue = true;
        } else {
            noValue = false;
            this.groupNameTextArea.setText(groupName);
        }
        this.groupNameTextArea.setVisible(!noValue);
        this.groupNameTextArea.setEnabled(!noValue);

        table.setModel(criteriaModel);

        table.setRowHeight(30);

        criteriaModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                tableRemoveBtn.setEnabled(criteriaModel.getList().size() > 0);
            }
        });

        if (initialCriteriaList != null) {
            criteriaModel.addAllCriteria(initialCriteriaList);
        }

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellEditor(new ComboBoxCellEditor(operators));
        columnModel.getColumn(0).setCellRenderer(new OperatorCellRenderer());
        columnModel.getColumn(0).setMaxWidth(70);
        columnModel.getColumn(1).setCellEditor(new ComboBoxCellEditor(layers.getIds()));
        columnModel.getColumn(1).setMinWidth(200);
        columnModel.getColumn(2).setCellEditor(new ComboBoxCellEditor(comparators));
        columnModel.getColumn(3).setCellEditor(new DefaultCellEditor(new JTextField()));
        columnModel.getColumn(4).setCellEditor(new DefaultCellEditor(new JTextField()));

        if (initialCriteriaList == null) {
            addCriteria(true);
        }

        groupNameTextArea.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            protected void update(DocumentEvent e) {
                groupNameChanged(e);
            }
        });


    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public boolean isCancelled() {
        return returnStatus != RET_OK;
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    private void addCriteria(boolean first) {
        if (first) {
            criteriaModel.addCriteria(new DataIntegrationCriteria(layers.get(layers.getIds()[0]), CutoffCmp.EQ, 1.0, null, Operator.EMPTY));
        } else {
            criteriaModel.addCriteria(new DataIntegrationCriteria(layers.get(layers.getIds()[0]), CutoffCmp.EQ, 1.0, null, Operator.AND));
        }

    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        applyToGroup = new javax.swing.ButtonGroup();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        tableAddBtn = new javax.swing.JButton();
        tableRemoveBtn = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        groupNameTextArea = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setTitle("Criteria creation");
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Operator", "Attribute", "Condition", "Value", "Convert empty"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        table.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        tableAddBtn.setText("Add");
        tableAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableAddBtnActionPerformed(evt);
            }
        });

        tableRemoveBtn.setText("Remove");
        tableRemoveBtn.setEnabled(false);
        tableRemoveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableRemoveBtnActionPerformed(evt);
            }
        });

        jLabel2.setText("Group Name:");

        groupNameTextArea.setText("0");

        jLabel1.setText("Select columns or rows according to the specified criteria below. ");

        jLabel3.setText("<html><b>Operator</b> lets you combine critera by AND (both criteria fulfilled) and OR (one critera fulfilled).</html>");

        jLabel4.setText("<html><b>Layer</b>, <b>Condition</b> and <b>Value</b> define the criteria</html>");

        jLabel5.setText("<html>Specify a <b>Convert empty</b> value if empty cells for the specified layer are equivalent to a numeric value.</html>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cancelButton))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jScrollPane1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(tableAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(tableRemoveBtn))
                                                .addGap(6, 6, 6))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(groupNameTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jLabel1)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{cancelButton, okButton});

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(groupNameTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(tableAddBtn)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tableRemoveBtn)
                                                .addGap(165, 165, 165)))
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cancelButton)
                                        .addComponent(okButton))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void tableAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableAddBtnActionPerformed
        boolean first = table.getRowCount() == 0;
        addCriteria(first);
    }//GEN-LAST:event_tableAddBtnActionPerformed

    private void tableRemoveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableRemoveBtnActionPerformed
        criteriaModel.removeCriteria(table.getSelectedRows());
    }//GEN-LAST:event_tableRemoveBtnActionPerformed

    private void groupNameChanged(DocumentEvent evt) {

        if (groupNameTextArea.getText().isEmpty()) {
            okButton.setEnabled(false);
        } else {
            okButton.setEnabled(true);
        }
    }

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        try {
            if (retStatus == RET_OK) {
                TableColumnModel columnModel = table.getColumnModel();
                boolean b;
                b = columnModel.getColumn(0).getCellEditor().stopCellEditing();
                b = columnModel.getColumn(1).getCellEditor().stopCellEditing();
                b = columnModel.getColumn(2).getCellEditor().stopCellEditing();
                b = columnModel.getColumn(3).getCellEditor().stopCellEditing();
                b = columnModel.getColumn(4).getCellEditor().stopCellEditing();
            }

            setVisible(false);
            dispose();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup applyToGroup;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField groupNameTextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton okButton;
    private javax.swing.JTable table;
    private javax.swing.JButton tableAddBtn;
    private javax.swing.JButton tableRemoveBtn;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;

    public List<DataIntegrationCriteria> getCriteriaList() {
        return criteriaModel.getList();
    }

    public String getGroupName() {
        return groupNameTextArea.getText();
    }
}
