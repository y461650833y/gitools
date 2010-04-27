/*
 *  Copyright 2010 xrafael.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */


package org.gitools.ui.analysis.clustering.dialog;

public class KmeansParamsPanel extends javax.swing.JPanel {

    /** Creates new form cobwebParamsPanel */
    public KmeansParamsPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        kField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        iterField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        seedField = new javax.swing.JTextField();
        numKCombo = new javax.swing.JComboBox();
        iterCombo = new javax.swing.JComboBox();
        seedCombo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        distAlgCombo = new javax.swing.JComboBox();

        jLabel1.setText("Num. Clusters : ");

        kField.setText("2");
        kField.setEnabled(false);

        jLabel2.setText("Max Iterations : ");

        iterField.setText("500");
        iterField.setEnabled(false);

        jLabel3.setText("Seed : ");

        seedField.setText("10");
        seedField.setEnabled(false);

        numKCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "default", "specify" }));
        numKCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numKComboActionPerformed(evt);
            }
        });

        iterCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "default", "specify" }));
        iterCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iterComboActionPerformed(evt);
            }
        });

        seedCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "default", "specify" }));
        seedCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seedComboActionPerformed(evt);
            }
        });

        jLabel4.setText("value :");

        jLabel5.setText("value :");

        jLabel6.setText("value :");

        jLabel7.setText("Distance algorithm : ");

        distAlgCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Euclidean", "Manhattan" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(numKCombo, 0, 167, Short.MAX_VALUE)
                            .addComponent(iterCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(seedCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(seedField, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                            .addComponent(iterField, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                            .addComponent(kField, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)))
                    .addComponent(distAlgCombo, 0, 270, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(distAlgCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(kField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numKCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(iterCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(seedCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(iterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(seedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void numKComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numKComboActionPerformed

		if (numKCombo.getSelectedItem().toString().toLowerCase().equals("specify"))
			kField.setEnabled(true);
		else
			kField.setEnabled(false);
	}//GEN-LAST:event_numKComboActionPerformed

	private void iterComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iterComboActionPerformed


		if (iterCombo.getSelectedItem().toString().toLowerCase().equals("specify"))
			iterField.setEnabled(true);
		else
			iterField.setEnabled(false);


	}//GEN-LAST:event_iterComboActionPerformed

private void seedComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seedComboActionPerformed

		if (seedCombo.getSelectedItem().toString().toLowerCase().equals("specify"))
			seedField.setEnabled(true);
		else
			seedField.setEnabled(false);
}//GEN-LAST:event_seedComboActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox distAlgCombo;
    private javax.swing.JComboBox iterCombo;
    private javax.swing.JTextField iterField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField kField;
    private javax.swing.JComboBox numKCombo;
    private javax.swing.JComboBox seedCombo;
    private javax.swing.JTextField seedField;
    // End of variables declaration//GEN-END:variables

	//FIXME: specify high,mid,low values for cutOff
	public String getIterations(){
		if (iterCombo.getSelectedItem().toString().equals("default"))
			return "500";
		else
			if (iterCombo.getSelectedItem().toString().equals("specify"))
				return iterField.getText();
			else
				return "500";
	}

	//FIXME: specify high,mid,low values for Acuity
	public String getKclusters(){
		if (numKCombo.getSelectedItem().toString().equals("default"))
			return "2";
		else
			if (numKCombo.getSelectedItem().toString().equals("specify"))
				return kField.getText();
			else
				return "2";
	}

	//FIXME: specify high,mid,low values for Seed
	public String getSeed(){
		if (seedCombo.getSelectedItem().toString().equals("default"))
			return "10";
		else
			if (seedCombo.getSelectedItem().toString().equals("specify"))
				return seedField.getText();
			else
				return "10";
	}
	public String getDistanceMethod(){
		return distAlgCombo.getSelectedItem().toString();
	}
}
