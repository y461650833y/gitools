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
package org.gitools.ui.wizard.add.data;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixLayer;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.utils.DocumentChangeListener;

import javax.swing.event.DocumentEvent;

public class DataDetailsPage extends AbstractWizardPage {

    private final Heatmap hm;
    private String dimensionName;
    private final String message;

    public DataDetailsPage(Heatmap hm) {
        this.hm = hm;

        initComponents();
        setComplete(false);

        setTitle("Data DimensionEnum Integration");
        this.message = "Choose a name for the new data value dimension";
        setMessage(message);

        nameField.getDocument().addDocumentListener(new DocumentChangeListener() {

            @Override
            protected void update(DocumentEvent e) {
                nameChanged();
            }
        });
    }


    private void nameChanged() {
        if (nameField.getText().length() > 0) {

            boolean everythingIsOk = true;
            String existingName;
            String originalWantedName = nameField.getText();
            String wantedName = originalWantedName.toLowerCase().trim();
            for (IMatrixLayer iElementAttribute : hm.getLayers()) {
                existingName = iElementAttribute.getName().toLowerCase();
                if (existingName.equals(wantedName)) {
                    setMessage(MessageStatus.ERROR, "Data dimension with name '" + originalWantedName + "' already exists");
                    everythingIsOk = false;
                    break;
                }
            }

            if (everythingIsOk) {
                setComplete(true);
                setMessage(MessageStatus.INFO, message);
                dimensionName = nameField.getText();
            }
        } else {
            setComplete(false);
            dimensionName = "";
        }
    }

    public String getDimensionName() {
        return dimensionName;
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

        applyGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();

        jLabel1.setText("Name");

        nameField.setText("Integrated values");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addContainerGap(384, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(408, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup applyGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField nameField;
    // End of variables declaration//GEN-END:variables


}
