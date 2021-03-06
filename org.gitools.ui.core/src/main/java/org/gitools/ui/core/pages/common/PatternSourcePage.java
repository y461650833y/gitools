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
package org.gitools.ui.core.pages.common;

import org.gitools.api.ApplicationContext;
import org.gitools.api.resource.ResourceReference;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.matrix.format.AnnotationMatrixFormat;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.core.utils.DocumentChangeListener;
import org.gitools.ui.core.utils.FileChooserUtils;
import org.gitools.ui.core.utils.LogUtils;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.settings.Settings;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PatternSourcePage extends AbstractWizardPage {

    private HeatmapDimension hdim;

    private boolean idOptVisible;
    private boolean viewOnlyMode;

    private List<AnnotationOption> annotationOptions;

    public PatternSourcePage(boolean idOptVisible) {
        this(null, idOptVisible, false);
    }


    public PatternSourcePage(HeatmapDimension hdim) {
        this(hdim, true, false);
    }


    public PatternSourcePage(HeatmapDimension hdim, boolean idAsOption) {
        this(hdim, idAsOption, false);
    }


    public PatternSourcePage(HeatmapDimension hdim, boolean idAsOption, boolean viewOnlyMode) {
        this.hdim = hdim;
        this.idOptVisible = idAsOption;
        this.viewOnlyMode = viewOnlyMode;
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.annotation512, 96));


        initComponents();

        ChangeListener optListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sourceChanged();
            }
        };

        if (!viewOnlyMode) {
            idOpt.addChangeListener(optListener);
            annOpt.addChangeListener(optListener);
            patOpt.addChangeListener(optListener);
        } else {
            idOpt.setVisible(false);
            idOpt.setEnabled(false);
            patOpt.setVisible(false);
            patOpt.setEnabled(false);
        }

        annSepCb.setModel(new DefaultComboBoxModel(new String[]{",", "-", " | ", " / ", " > ", "::"}));
        annSepCb.setSelectedIndex(0);

        annList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                annListChanged();
            }
        });


        patText.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            protected void update(DocumentEvent e) {
                updateComplete();
            }
        });

        setTitle("Annotations selection");
        setComplete(true);
    }

    class FilterCellRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            AnnotationOption annOpt = (AnnotationOption) value;
            String needle = annotationSearchField.getText().toLowerCase();
            boolean match;
            match = annotationSearchField.getText().isEmpty();

            if (!match) {
                match = annOpt.getKey().toLowerCase().contains(needle) |
                        annOpt.getDescription().toLowerCase().contains(needle);
            }

            if (match) {// <= put your logic here
                c.setFont(c.getFont().deriveFont(Font.ITALIC, 14.0f));
                c.setForeground(Color.BLACK);
            } else {
                c.setFont(c.getFont().deriveFont(Font.ITALIC, 9.0f));
                c.setForeground(Color.LIGHT_GRAY);
            }
            return c;
        }
    }


    @Override
    public void updateControls() {
        super.updateControls();

        if (hdim != null && hdim.getAnnotations() != null && !hdim.getAnnotations().getLabels().isEmpty()) {
            annOpt.setSelected(true);
            annOpt.setEnabled(true);
            DefaultListModel<AnnotationOption> model = new DefaultListModel<>();
            FilterCellRenderer cellRenderer = new FilterCellRenderer();
            if (idOptVisible) {
                model.addElement(new AnnotationOption("id"));
            }


            annotationOptions = new ArrayList<>(hdim.getAnnotations().getLabels().size());
            for (String key : hdim.getAnnotations().getLabels()) {
                String description = hdim.getAnnotations().getAnnotationMetadata("description", key);
                annotationOptions.add(new AnnotationOption(key, description));
            }

            Collections.sort(annotationOptions, new Comparator<AnnotationOption>() {
                @Override
                public int compare(AnnotationOption o1, AnnotationOption o2) {
                    return o1.toString().toUpperCase().compareTo(o2.toString().toUpperCase());
                }
            });

            for (AnnotationOption annotationOption : annotationOptions) {
                model.addElement(annotationOption);
            }

            annList.setModel(model);
            annList.setSelectedIndex(0);
            annList.setCellRenderer(cellRenderer);
        } else {
            if (idOptVisible) {
                idOpt.setSelected(true);
            } else {
                patOpt.setSelected(true);
            }
            annOpt.setEnabled(false);
        }
    }

    private void removeSelecedAnnotations() {
        String inUse = "id";
        for (HeatmapHeader hh : hdim.getHeaders()) {
            inUse = inUse + ", " + hh.getAnnotationPattern();
        }

        for (Object option : annList.getSelectedValuesList()) {
            String label = ((AnnotationOption) option).getKey();
            if (!inUse.contains(label)) {
                hdim.getAnnotations().removeAnnotations(label);
            }
        }
        updateControls();
        annList.setSelectedIndices(new int[0]);
    }

    public void setAnnSeparationModel(String[] strings) {
        annSepCb.setModel(new DefaultComboBoxModel(strings));
        annSepCb.setSelectedIndex(0);
    }

    private void updateComplete() {
        setComplete(annOpt.isSelected() && annList.getSelectedIndices().length > 0 || patOpt.isSelected() && patText.getDocument().getLength() > 0);
    }

    private void sourceChanged() {
        boolean annSel = annOpt.isSelected();
        annList.setEnabled(annSel);
        annSepLabel.setEnabled(annSel);
        annSepCb.setEnabled(annSel);
        patText.setEnabled(patOpt.isSelected());
    }

    private void annListChanged() {
        patText.setText(getPattern());
        updateComplete();
    }

    public String getPattern() {
        if (idOpt.isSelected()) {
            return "${id}";
        } else if (patOpt.isSelected()) {
            return patText.getText();
        }

        StringBuilder sb = new StringBuilder();
        String[] values = getSelectedValues();
        if (values.length == 0) {
            return "";
        }

        sb.append("${");
        sb.append(values[0]);
        sb.append("}");
        for (int i = 1; i < values.length; i++) {
            sb.append(annSepCb.getSelectedItem());
            sb.append("${");
            sb.append(values[i]);
            sb.append("}");
        }

        return sb.toString();
    }

    public String getPatternTitle() {
        if (idOpt.isSelected()) {
            return "id";
        } else if (patOpt.isSelected()) {
            return patText.getText();
        }

        StringBuilder sb = new StringBuilder();
        String[] values = getSelectedValues();

        sb.append(values[0]);
        for (int i = 1; i < values.length; i++) {
            sb.append(annSepCb.getSelectedItem());
            sb.append(values[i]);
        }

        return sb.toString();
    }

    public String[] getSelectedValues() {
        int[] indices = annList.getSelectedIndices();
        String[] values = new String[indices.length];

        for (int i = 0; i < indices.length; i++) {
            int index = indices[i];
            if (index == 0) {
                values[i] = "id";
            } else {
                values[i] = annotationOptions.get(index - 1).getKey();
            }
        }

        return values;
    }

    public void setHeatmapDimension(HeatmapDimension heatmapDimension) {
        this.hdim = heatmapDimension;
        updateControls();
    }

    public boolean isIdOptVisible() {
        return idOptVisible;
    }

    public void setIdOptVisible(boolean idOptVisible) {
        this.idOptVisible = idOptVisible;
        idOpt.setVisible(idOptVisible);
        invalidate();
    }

    private void filterAnnotationsBox(KeyEvent evt) {
        annList.repaint();
    }

    public HeatmapTextLabelsHeader.LabelSource getLabelSource() {
        if (idOpt.isSelected()) {
            return HeatmapTextLabelsHeader.LabelSource.ID;
        } else if (annOpt.isSelected()) {
            if (annList.getSelectedIndex() == 0) {
                return HeatmapTextLabelsHeader.LabelSource.ID;
            } else {
                return HeatmapTextLabelsHeader.LabelSource.PATTERN;
            }
        } else if (patOpt.isSelected()) {
            return HeatmapTextLabelsHeader.LabelSource.PATTERN;
        }
        return HeatmapTextLabelsHeader.LabelSource.ID;
    }

    public String getAnnotationName() {
        if (annList.getSelectedIndex() > 0) {
            return annotationOptions.get(annList.getSelectedIndex() - 1).getKey();
        } else {
            return "";
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

        optGroup = new javax.swing.ButtonGroup();
        annOpt = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        annList = new javax.swing.JList();
        patOpt = new javax.swing.JRadioButton();
        patText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        annSepLabel = new javax.swing.JLabel();
        annSepCb = new javax.swing.JComboBox();
        idOpt = new javax.swing.JRadioButton();
        loadAnnotations = new javax.swing.JButton();
        annotationSearchField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        removeSelected = new javax.swing.JButton();

        optGroup.add(annOpt);
        annOpt.setText("Annotations");

        jScrollPane1.setViewportView(annList);

        optGroup.add(patOpt);
        patOpt.setText("Pattern");

        patText.setText("${id}");

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getSize() - 2f));
        jLabel1.setText("Press Ctrl key to select for multiple annotations");

        annSepLabel.setText("Separator");

        annSepCb.setEditable(true);

        optGroup.add(idOpt);
        idOpt.setText("Identifiers");

        loadAnnotations.setText("Load from file");
        loadAnnotations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadAnnotationsActionPerformed(evt);
            }
        });

        annotationSearchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                annotationSearchFieldKeyReleased(evt);
            }
        });

        jLabel2.setText("Filter:");

        removeSelected.setText("remove Selected");
        removeSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSelectedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(annotationSearchField))
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                                                        .addComponent(patText, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(idOpt)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(annOpt)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(loadAnnotations)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(removeSelected))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(annSepLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(annSepCb, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(patOpt)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(idOpt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(annOpt)
                                        .addComponent(loadAnnotations)
                                        .addComponent(removeSelected))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(annotationSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(annSepLabel)
                                        .addComponent(annSepCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(patOpt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(patText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void loadAnnotationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadAnnotationsActionPerformed
        try {
            File file = FileChooserUtils.selectFile("Open annotations file", Settings.get().getLastAnnotationPath(), FileChooserUtils.MODE_OPEN).getFile();

            if (file != null) {
                hdim.addAnnotations(new ResourceReference<>(new UrlResourceLocator(file), ApplicationContext.getPersistenceManager().getFormat(AnnotationMatrixFormat.EXTENSION, AnnotationMatrix.class)).get());
                Settings.get().setLastAnnotationPath(file.getParent());
                updateControls();
                //annFile.setText(file.getName());
            }
        } catch (Exception ex) {
            LogUtils.logException(ex, LoggerFactory.getLogger(getClass()));
        }
    }//GEN-LAST:event_loadAnnotationsActionPerformed

    private void annotationSearchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_annotationSearchFieldKeyReleased
        filterAnnotationsBox(evt);
    }//GEN-LAST:event_annotationSearchFieldKeyReleased

    private void removeSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSelectedActionPerformed
        removeSelecedAnnotations();
    }//GEN-LAST:event_removeSelectedActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList annList;
    private javax.swing.JRadioButton annOpt;
    private javax.swing.JComboBox annSepCb;
    private javax.swing.JLabel annSepLabel;
    private javax.swing.JTextField annotationSearchField;
    private javax.swing.JRadioButton idOpt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loadAnnotations;
    private javax.swing.ButtonGroup optGroup;
    private javax.swing.JRadioButton patOpt;
    private javax.swing.JTextField patText;
    private javax.swing.JButton removeSelected;
    // End of variables declaration//GEN-END:variables

}
