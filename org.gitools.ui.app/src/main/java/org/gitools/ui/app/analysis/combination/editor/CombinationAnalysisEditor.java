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
package org.gitools.ui.app.analysis.combination.editor;

import org.apache.velocity.VelocityContext;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.analysis.combination.format.CombinationAnalysisFormat;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.decorator.impl.PValueDecorator;
import org.gitools.ui.app.commands.CommandLoadFile;
import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.components.editor.AnalysisEditor;
import org.gitools.ui.core.components.editor.EditorsPanel;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import javax.enterprise.context.ApplicationScoped;
import javax.swing.*;
import java.util.Map;

@ApplicationScoped
public class CombinationAnalysisEditor extends AnalysisEditor<CombinationAnalysis> {

    public CombinationAnalysisEditor(CombinationAnalysis analysis) {
        super(analysis, "/vm/analysis/combination/analysis_details.vm", CombinationAnalysisFormat.EXTENSION);
    }

    @Override
    protected void prepareContext(VelocityContext context) {

        final CombinationAnalysis analysis = getModel();

        String combOf = "columns";
        if (analysis.isTransposeData()) {
            combOf = "rows";
        }
        context.put("combinationOf", combOf);

        IResourceLocator resourceLocator = analysis.getData().getLocator();
        context.put("dataFile", resourceLocator != null ? resourceLocator.getName() : "Not defined");

        resourceLocator = analysis.getGroupsMap().getLocator();
        String groupsFile = resourceLocator != null ? resourceLocator.getName() : "Not specified. All " + combOf + " are combined";
        context.put("groupsFile", groupsFile);

        String sizeAttr = analysis.getSizeLayer();
        if (sizeAttr == null || sizeAttr.isEmpty()) {
            sizeAttr = "Constant value of 1";
        }
        context.put("sizeAttr", sizeAttr);

        resourceLocator = analysis.getResults().getLocator();
        context.put("resultsFile", resourceLocator != null ? resourceLocator.getName() : "Not defined");

        resourceLocator = analysis.getLocator();
        if (resourceLocator != null) {
            context.put("analysisLocation", resourceLocator.getURL());
        } else {
            setSaveAllowed(true);
        }
    }

    @Override
    protected void performUrlAction(String name, Map<String, String> params) {
        if ("NewDataHeatmap".equals(name)) {
            newDataHeatmap();
        } else if ("NewResultsHeatmap".equals(name)) {
            newResultsHeatmap();
        } else if ("ViewModuleMap".equals(name)) {
            newGroupsMap();
        }
    }

    private void newGroupsMap() {

        final CombinationAnalysis analysis = getModel();

        if (analysis.getGroupsMap() == null) {
            Application.get().showNotificationPermanent("Analysis doesn't contain a groups file.");
            return;
        }

        JobThread.execute(Application.get(), new CommandLoadFile(analysis.getGroupsMap()));
    }

    private void newDataHeatmap() {

        final CombinationAnalysis analysis = getModel();

        if (analysis.getData() == null) {
            Application.get().showNotificationPermanent("Analysis doesn't contain data.");
            return;
        }

        final EditorsPanel editorPanel = Application.get().getEditorsPanel();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    monitor.begin("Creating new heatmap from data ...", 1);
                    final HeatmapEditor editor = new HeatmapEditor(createDataHeatmap(analysis));

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            editorPanel.addEditor(editor);
                            Application.get().showNotification("Combination data heatmap created.");
                        }
                    });

                } catch (Exception e) {
                    monitor.exception(e);
                }
            }
        });
    }

    private void newResultsHeatmap() {

        final CombinationAnalysis analysis = getModel();
        if (analysis.getResults() == null) {
            Application.get().showNotificationPermanent("Analysis doesn't contain results.");
            return;
        }

        final EditorsPanel editorPanel = Application.get().getEditorsPanel();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Creating new heatmap from results ...", 1);

                try {
                    final HeatmapEditor editor = new HeatmapEditor(createResultsHeatmap(analysis));
                    editor.setIcon(IconUtils.getIconResource(IconNames.analysisHeatmap16));

                    editor.setName(editorPanel.deriveName(getName(), CombinationAnalysisFormat.EXTENSION, "-results", ""));

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            editorPanel.addEditor(editor);
                            Application.get().showNotification("Heatmap for combination results created.");
                        }
                    });
                } catch (Exception e) {
                    monitor.exception(e);
                }
            }
        });
    }

    @Deprecated
    private Heatmap createDataHeatmap(CombinationAnalysis analysis) {

        IMatrix data = analysis.getData().get();
        if (Heatmap.class.isAssignableFrom(data.getClass())) {
            return (Heatmap) data;
        }

        Heatmap heatmap = new Heatmap(data);
        heatmap.setTitle(analysis.getTitle() + " (data)");
        return heatmap;
    }

    @Deprecated
    private Heatmap createResultsHeatmap(CombinationAnalysis analysis) {
        Heatmap heatmap = new Heatmap(analysis.getResults().get());
        heatmap.getLayers().setTopLayerById("right-p-value");
        heatmap.getLayers().getTopLayer().setDecorator(new PValueDecorator());
        heatmap.setTitle(analysis.getTitle() + " (results)");
        return heatmap;
    }
}
