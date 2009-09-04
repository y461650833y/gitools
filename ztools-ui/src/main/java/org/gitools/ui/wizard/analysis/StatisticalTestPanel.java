/*
 * StatisticalTestPanel.java
 *
 * Created on September 3, 2009, 6:50 PM
 */

package org.gitools.ui.wizard.analysis;

/**
 *
 * @author  cperez
 */
public class StatisticalTestPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = -2043719552247673856L;
	
	/** Creates new form StatisticalTestPanel */
    public StatisticalTestPanel() {
        initComponents();
        
        updateTestConfControls();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        testCbox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        samplingSizeLabel = new javax.swing.JLabel();
        samplingSizeCbox = new javax.swing.JComboBox();
        estimatorLabel = new javax.swing.JLabel();
        estimatorCbox = new javax.swing.JComboBox();

        testCbox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Binomial (Bernoulli)", "Fisher Exact", "Z-Score" }));
        testCbox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                testCboxItemStateChanged(evt);
            }
        });

        jLabel1.setText("Description");

        samplingSizeLabel.setText("Sampling size");

        samplingSizeCbox.setEditable(true);
        samplingSizeCbox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "100", "1000", "10000" }));
        samplingSizeCbox.setSelectedIndex(2);

        estimatorLabel.setText("Estimator");

        estimatorCbox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "mean", "median" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(testCbox, 0, 376, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(estimatorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(samplingSizeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(estimatorCbox, 0, 238, Short.MAX_VALUE)
                            .addComponent(samplingSizeCbox, 0, 238, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(testCbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(samplingSizeLabel)
                    .addComponent(samplingSizeCbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(estimatorCbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(estimatorLabel))
                .addContainerGap(76, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void testCboxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_testCboxItemStateChanged
    updateTestConfControls();
}//GEN-LAST:event_testCboxItemStateChanged

    private void updateTestConfControls() {
        boolean isZ = testCbox.getSelectedItem().toString().equals("Z-Score");
        samplingSizeLabel.setVisible(isZ);
        samplingSizeCbox.setVisible(isZ);
        estimatorLabel.setVisible(isZ);
        estimatorCbox.setVisible(isZ);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JComboBox estimatorCbox;
    private javax.swing.JLabel estimatorLabel;
    private javax.swing.JLabel jLabel1;
    public javax.swing.JComboBox samplingSizeCbox;
    private javax.swing.JLabel samplingSizeLabel;
    public javax.swing.JComboBox testCbox;
    // End of variables declaration//GEN-END:variables

}