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
package org.gitools.ui.app.analysis.htest.wizard;

import org.gitools.analysis.ToolConfig;
import org.gitools.analysis.stats.test.factory.BinomialTestFactory;
import org.gitools.analysis.stats.test.factory.TestFactory;
import org.gitools.analysis.stats.test.factory.ZscoreTestFactory;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * @noinspection ALL
 */
public class StatisticalTestPage extends AbstractWizardPage {

    private static final long serialVersionUID = -2043719552247673856L;

    private static class Test {
        public final String name;
        public final String description;

        public Test(String name, String description) {
            this.name = name;
            this.description = description;
        }

        @Override
        public String toString() {
            return name;
        }
    }


    /**
     * Creates new form StatisticalTestPanel
     */
    public StatisticalTestPage() {
        initComponents();

        setTitle("Select statistical test");

        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_METHOD, 96));

        setComplete(true);

        testCbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Test test = (Test) testCbox.getSelectedItem();
                descLabel.setText(test.description);

                boolean isZ = testCbox.getSelectedIndex() == 2;
                samplingSizeLabel.setVisible(isZ);
                samplingSizeCbox.setVisible(isZ);
                estimatorLabel.setVisible(isZ);
                estimatorCbox.setVisible(isZ);
            }
        });
        testCbox.setModel(new DefaultComboBoxModel(
                new Test[]{new Test("Binomial (Bernoulli)", "Binomial: Use with binary data and big sample sizes"),
                        new Test("Fisher Exact", "Fisher's Exact: Use with binary data and small sample sizes"),
                        new Test("Z Score", "Z-Score: Use with continuous data and obtaining detailed enrichment status. The greater the sampling size the more accurate the result")}));
        testCbox.setSelectedIndex(0);

        samplingSizeCbox.setSelectedItem(String.valueOf(ZscoreTestFactory.DEFAULT_NUM_SAMPLES));

        estimatorCbox.setModel(new DefaultComboBoxModel(new String[]{ZscoreTestFactory.MEAN_ESTIMATOR, ZscoreTestFactory.MEDIAN_ESTIMATOR}));
    }


    @Override
    public JComponent createControls() {
        return this;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        testCbox = new javax.swing.JComboBox();
        descLabel = new javax.swing.JLabel();
        samplingSizeLabel = new javax.swing.JLabel();
        samplingSizeCbox = new javax.swing.JComboBox();
        estimatorLabel = new javax.swing.JLabel();
        estimatorCbox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        mtcCb = new javax.swing.JComboBox();

        testCbox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Binomial (Bernoulli)", "Fisher Exact", "Z-Score"}));

        descLabel.setText("Description");

        samplingSizeLabel.setText("Sampling size");

        samplingSizeCbox.setEditable(true);
        samplingSizeCbox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"100", "1000", "10000"}));
        samplingSizeCbox.setSelectedIndex(2);

        estimatorLabel.setText("Estimator");

        estimatorCbox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"mean", "median"}));

        jLabel1.setText("Multiple test correction");

        mtcCb.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Bonferroni", "Benjamini Hochberg FDR"}));
        mtcCb.setSelectedIndex(1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(descLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE).addComponent(testCbox, 0, 376, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(estimatorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(samplingSizeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(estimatorCbox, 0, 275, Short.MAX_VALUE).addComponent(samplingSizeCbox, 0, 275, Short.MAX_VALUE))).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(mtcCb, 0, 218, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(testCbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(descLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(samplingSizeLabel).addComponent(samplingSizeCbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(estimatorCbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(estimatorLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(mtcCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descLabel;
    private javax.swing.JComboBox estimatorCbox;
    private javax.swing.JLabel estimatorLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JComboBox mtcCb;
    private javax.swing.JComboBox samplingSizeCbox;
    private javax.swing.JLabel samplingSizeLabel;
    private javax.swing.JComboBox testCbox;
    // End of variables declaration//GEN-END:variables


    public ToolConfig getTestConfig() {
        ToolConfig config = new ToolConfig(ToolConfig.ENRICHMENT);

        switch (testCbox.getSelectedIndex()) {
            case 0:
                config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.BINOMIAL_TEST);
                config.put(BinomialTestFactory.APROXIMATION_PROPERTY, BinomialTestFactory.EXACT_APROX);
                break;

            case 1:
                config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.FISHER_EXACT_TEST);
                break;

            case 2:
                config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.ZSCORE_TEST);
                config.put(ZscoreTestFactory.NUM_SAMPLES_PROPERTY, samplingSizeCbox.getSelectedItem().toString());
                config.put(ZscoreTestFactory.ESTIMATOR_PROPERTY, estimatorCbox.getSelectedItem().toString());
                break;
        }

        return config;
    }

    public void setTestConfig(ToolConfig testConfig) {
        Map<String, String> cfg = testConfig.getConfiguration();
        String testName = cfg.get(TestFactory.TEST_NAME_PROPERTY);
        int index = 0;
        if (TestFactory.BINOMIAL_TEST.equals(testName)) {
            index = 0;
        } else if (TestFactory.FISHER_EXACT_TEST.equals(testName)) {
            index = 1;
        } else if (TestFactory.ZSCORE_TEST.equals(testName)) {
            index = 2;
            samplingSizeCbox.setSelectedItem(cfg.get(ZscoreTestFactory.NUM_SAMPLES_PROPERTY));
            estimatorCbox.setSelectedItem(cfg.get(ZscoreTestFactory.ESTIMATOR_PROPERTY));
        }
        testCbox.setSelectedIndex(index);

        boolean vis = index == 2;
        samplingSizeLabel.setVisible(vis);
        samplingSizeCbox.setVisible(vis);
        estimatorLabel.setVisible(vis);
        estimatorCbox.setVisible(vis);
    }

    // FIXME

    public String getMtc() {
        switch (mtcCb.getSelectedIndex()) {
            case 0:
                return "bonferroni";
            case 1:
                return "bh";
        }
        return "bh";
    }

    // FIXME
    public void setMtc(String mtc) {
        if (mtc.equals("bonferroni")) {
            mtcCb.setSelectedIndex(0);
        } else if (mtc.equals("bh")) {
            mtcCb.setSelectedIndex(1);
        }
    }
}
