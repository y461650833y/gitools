/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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
 * AddHeaderDialog.java
 *
 * Created on 25-feb-2011, 19:33:13
 */

package org.gitools.ui.heatmap.header;

import javax.swing.DefaultListModel;
import org.gitools.heatmap.model.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.model.HeatmapHeader;
import org.gitools.heatmap.model.HeatmapLabelsHeader;
import org.gitools.ui.platform.wizard.AbstractWizardPage;


public class AddHeaderPage extends AbstractWizardPage {

	private static class HeaderType {
		private String title;
		private Class<? extends HeatmapHeader> cls;

		public HeaderType(String title, Class<? extends HeatmapHeader> cls) {
			this.title = title;
			this.cls = cls;
		}

		public String getTitle() {
			return title;
		}

		public Class<? extends HeatmapHeader> getHeaderClass() {
			return cls;
		}

		@Override
		public String toString() {
			return title;
		}
	}

	private DefaultListModel model;

    /** Creates new form AddHeaderDialog */
    public AddHeaderPage() {
        initComponents();

		model = new DefaultListModel();
		model.addElement(new HeaderType("Text labels", HeatmapLabelsHeader.class));
		model.addElement(new HeaderType("Colored labels from annotations", HeatmapColoredLabelsHeader.class));
		// TODO Colored clusters from a hierarchical clustering
		// TODO Values plot
		// TODO Calculated value

		headerTypeList.setModel(model);
		headerTypeList.setSelectedIndex(0);

		setTitle("Which type of header do you want to add ?");

		setComplete(true);
    }

	public Class<? extends HeatmapHeader> getHeaderClass() {
		return ((HeaderType) headerTypeList.getSelectedValue()).getHeaderClass();
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        optGroup = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        headerTypeList = new javax.swing.JList();

        jScrollPane1.setViewportView(headerTypeList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList headerTypeList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup optGroup;
    // End of variables declaration//GEN-END:variables

}
