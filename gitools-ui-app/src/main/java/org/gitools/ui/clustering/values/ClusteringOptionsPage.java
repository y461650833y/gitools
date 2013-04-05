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
package org.gitools.ui.clustering.values;

import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

/**
 * @noinspection ALL
 */
class ClusteringOptionsPage extends AbstractWizardPage
{

    // model wrapper
    private static class MatrixAttributeWrapper
    {

        private IElementAttribute attribute;

        public MatrixAttributeWrapper(IElementAttribute a)
        {
            this.attribute = a;
        }

        public IElementAttribute getMatrixAttribute()
        {
            return attribute;
        }

        public void setMatrixAttribute(IElementAttribute a)
        {
            this.attribute = a;
        }

        @Override
        public String toString()
        {
            return attribute.getName();
        }
    }

    public ClusteringOptionsPage(@NotNull List<IElementAttribute> cellAttributes, int index)
    {

        initComponents();

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        MatrixAttributeWrapper attrWrapper = null;
        for (IElementAttribute attr : cellAttributes)
        {
            attrWrapper = new MatrixAttributeWrapper(attr);
            model.addElement(attrWrapper);
        }

        attributeCb.setModel(model);
        attributeCb.setSelectedIndex(index);

        newickChk.setVisible(false);

        setTitle("Clustering options");
        setComplete(true);
    }

    public int getDataAttribute()
    {
        return attributeCb.getSelectedIndex();
    }

    public boolean isApplyToRows()
    {
        return rowsRadio.isSelected();
    }

    public boolean isPreprocessing()
    {
        return preprocessingChk.isSelected();
    }

    public boolean isSort()
    {
        return sortChk.isSelected();
    }

    public boolean isHeaderSelected()
    {
        return headerChk.isSelected();
    }

    public void setHeaderEnabled(boolean res)
    {
        headerChk.setEnabled(res);
    }

    public boolean isNewickExportVisible()
    {
        return newickChk.isVisible();
    }

    public void setNewickExportVisible(boolean enabled)
    {
        newickChk.setVisible(enabled);
    }

    public boolean isNewickExportSelected()
    {
        return newickChk.isVisible() && newickChk.isSelected();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        optGroup = new javax.swing.ButtonGroup();
        jLabel5 = new javax.swing.JLabel();
        columnsRadio = new javax.swing.JRadioButton();
        rowsRadio = new javax.swing.JRadioButton();
        preprocessingChk = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        attributeCb = new javax.swing.JComboBox();
        headerChk = new javax.swing.JCheckBox();
        sortChk = new javax.swing.JCheckBox();
        newickChk = new javax.swing.JCheckBox();

        jLabel5.setText("Apply to:");

        optGroup.add(columnsRadio);
        columnsRadio.setSelected(true);
        columnsRadio.setText("columns");

        optGroup.add(rowsRadio);
        rowsRadio.setText("rows");

        preprocessingChk.setSelected(true);
        preprocessingChk.setText("Apply dimensionality reduction");

        jLabel6.setText("Take values from");

        headerChk.setText("Add a new header with colors representing clusters");

        sortChk.setSelected(true);
        sortChk.setText("Sort heatmap by clustering results");

        newickChk.setText("Save hierarchical clustering tree in Newick format");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(columnsRadio).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(rowsRadio).addGap(307, 307, 307)).addGroup(layout.createSequentialGroup().addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(attributeCb, 0, 418, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(preprocessingChk).addComponent(sortChk)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 288, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(headerChk).addComponent(newickChk)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(columnsRadio).addComponent(rowsRadio)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(attributeCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(preprocessingChk).addGap(18, 18, 18).addComponent(sortChk).addGap(18, 18, 18).addComponent(headerChk).addGap(18, 18, 18).addComponent(newickChk).addContainerGap(74, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox attributeCb;
    private javax.swing.JRadioButton columnsRadio;
    private javax.swing.JCheckBox headerChk;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JCheckBox newickChk;
    private javax.swing.ButtonGroup optGroup;
    private javax.swing.JCheckBox preprocessingChk;
    private javax.swing.JRadioButton rowsRadio;
    private javax.swing.JCheckBox sortChk;
    // End of variables declaration//GEN-END:variables

}
