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
 * ColoredClustersAnnotationsPage.java
 *
 * Created on 02-mar-2011, 8:08:28
 */

package org.gitools.ui.clustering.values;

import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.gitools.clustering.ClusteringMethodDescriptor;
import org.gitools.clustering.ClusteringMethodFactory;
import org.gitools.clustering.method.value.AbstractClusteringValueMethod;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class ClusteringMethodsPage extends AbstractWizardPage {

	public ClusteringMethodsPage() {
		
		initComponents();

		List<ClusteringMethodDescriptor> descriptors = ClusteringMethodFactory.getDefault().getDescriptors();
		DefaultListModel model = new DefaultListModel();
			for (ClusteringMethodDescriptor desc : descriptors)
				if (desc.getMethodClass().getSuperclass() != null &&
						desc.getMethodClass()
							.getSuperclass()
							.equals(AbstractClusteringValueMethod.class))
					model.addElement(desc);

		annList.setModel(model);
		annList.setSelectedIndex(0);
		
		annList.addListSelectionListener(new ListSelectionListener() {
			@Override public void valueChanged(ListSelectionEvent e) {
				updateCompleted(); }
		});

		setTitle("Clustering method selection");
		setComplete(true);
	}

	private void updateCompleted() {
		boolean completed = annList.getSelectedIndices().length > 0;
		
		setComplete(completed);
	}


	@Override
	public void updateModel() {
		super.updateModel();

	}

	public ClusteringMethodDescriptor getMethodDescriptor() {
		int indice = annList.getSelectedIndex();
		
		if (indice >= 0)
			return (ClusteringMethodDescriptor) annList.getModel().getElementAt(indice);
		else
			return null;
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
        annList = new javax.swing.JList();

        annList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(annList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList annList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup optGroup;
    // End of variables declaration//GEN-END:variables

}
