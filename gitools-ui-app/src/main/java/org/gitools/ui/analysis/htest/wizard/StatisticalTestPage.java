/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

/*
 * StatisticalTestPanel.java
 *
 * Created on September 3, 2009, 6:50 PM
 */

package org.gitools.ui.analysis.htest.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import org.gitools.model.ToolConfig;
import org.gitools.stats.test.factory.BinomialTestFactory;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.stats.test.factory.ZscoreTestFactory;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class StatisticalTestPage extends AbstractWizardPage {

	private static final long serialVersionUID = -2043719552247673856L;

	private static class Test {
		public String name;
		public String description;
		public Test(String name, String description) {
			this.name = name;
			this.description = description;
		}
		@Override
		public String toString() {
			return name;
		}
	};
	
	/** Creates new form StatisticalTestPanel */
    public StatisticalTestPage() {
        initComponents();

		setTitle("Select statistical test");

		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_METHOD, 96));

		setComplete(true);

		testCbox.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				Test test = (Test) testCbox.getSelectedItem();
				descLabel.setText(test.description);

				boolean isZ = testCbox.getSelectedIndex() == 2;
				samplingSizeLabel.setVisible(isZ);
				samplingSizeCbox.setVisible(isZ);
				estimatorLabel.setVisible(isZ);
				estimatorCbox.setVisible(isZ);
			}
		});
		testCbox.setModel(new DefaultComboBoxModel(new Test[] {
				new Test("Binomial (Bernoulli)", ""),
				new Test("Fisher Exact", ""),
				new Test("Z Score", "")
		}));
		testCbox.setSelectedIndex(0);

		samplingSizeCbox.setSelectedItem(
				String.valueOf(ZscoreTestFactory.DEFAULT_NUM_SAMPLES));

		estimatorCbox.setModel(new DefaultComboBoxModel(new String[] {
				ZscoreTestFactory.MEAN_ESTIMATOR,
				ZscoreTestFactory.MEDIAN_ESTIMATOR
		}));
    }

	@Override
	public JComponent createControls() {
		return this;
	}

    /** This method is called from within the constructor to
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

        testCbox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Binomial (Bernoulli)", "Fisher Exact", "Z-Score" }));

        descLabel.setText("Description");

        samplingSizeLabel.setText("Sampling size");

        samplingSizeCbox.setEditable(true);
        samplingSizeCbox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "100", "1000", "10000" }));
        samplingSizeCbox.setSelectedIndex(2);

        estimatorLabel.setText("Estimator");

        estimatorCbox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "mean", "median" }));

        jLabel1.setText("Multiple test correction");

        mtcCb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bonferroni", "Benjamini Hochberg FDR" }));
        mtcCb.setSelectedIndex(1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(testCbox, 0, 376, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(estimatorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(samplingSizeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(estimatorCbox, 0, 275, Short.MAX_VALUE)
                            .addComponent(samplingSizeCbox, 0, 275, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mtcCb, 0, 218, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(testCbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(descLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(samplingSizeLabel)
                    .addComponent(samplingSizeCbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(estimatorCbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(estimatorLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(mtcCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel descLabel;
    public javax.swing.JComboBox estimatorCbox;
    public javax.swing.JLabel estimatorLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JComboBox mtcCb;
    public javax.swing.JComboBox samplingSizeCbox;
    public javax.swing.JLabel samplingSizeLabel;
    public javax.swing.JComboBox testCbox;
    // End of variables declaration//GEN-END:variables

	public ToolConfig getTestConfig() {
		ToolConfig config = new ToolConfig(ToolConfig.ENRICHMENT);

		switch (testCbox.getSelectedIndex()) {
		case 0:
			config.put(
					TestFactory.TEST_NAME_PROPERTY,
					TestFactory.BINOMIAL_TEST);
			config.put(
					BinomialTestFactory.APROXIMATION_PROPERTY,
					BinomialTestFactory.EXACT_APROX);
			break;

		case 1:
			config.put(
					TestFactory.TEST_NAME_PROPERTY,
					TestFactory.FISHER_EXACT_TEST);
			break;

		case 2:
			config.put(
					TestFactory.TEST_NAME_PROPERTY,
					TestFactory.ZSCORE_TEST);
			config.put(
					ZscoreTestFactory.NUM_SAMPLES_PROPERTY,
					samplingSizeCbox.getSelectedItem().toString());
			config.put(
					ZscoreTestFactory.ESTIMATOR_PROPERTY,
					estimatorCbox.getSelectedItem().toString());
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
		}
		else if (TestFactory.FISHER_EXACT_TEST.equals(testName)) {
			index = 1;
		}
		else if (TestFactory.ZSCORE_TEST.equals(testName)) {
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
			case 0: return "bonferroni";
			case 1: return "bh";
		}
		return "bh";
	}

	// FIXME
	public void setMtc(String mtc) {
		if (mtc.equals("bonferroni"))
			mtcCb.setSelectedIndex(0);
		else if (mtc.equals("bh"))
			mtcCb.setSelectedIndex(1);
	}
}