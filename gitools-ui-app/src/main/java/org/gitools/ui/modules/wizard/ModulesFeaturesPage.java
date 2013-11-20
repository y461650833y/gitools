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
package org.gitools.ui.modules.wizard;

import org.gitools.datasources.biomart.idmapper.EnsemblIds;
import org.gitools.core.modules.importer.FeatureCategory;
import org.gitools.core.modules.importer.ModulesImporter;
import org.gitools.core.modules.importer.Organism;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.wizard.common.FilteredListPage;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ModulesFeaturesPage extends FilteredListPage {

    private final ModulesImporter importer;

    private Organism organism;
    private boolean loaded;

    public ModulesFeaturesPage(ModulesImporter importer) {
        this.importer = importer;

        setTitle("Select Identifiers");
    }

    @Override
    public void updateControls() {
        if (!loaded || organism != importer.getOrganism()) {
            JobThread.execute(AppFrame.get(), new JobRunnable() {
                @Override
                public void run(@NotNull IProgressMonitor monitor) {
                    try {
                        monitor.begin("Getting available identifiers ...", 1);

                        organism = importer.getOrganism();
                        final FeatureCategory[] features = importer.getFeatureCategories();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                setListData(features);
                                for (FeatureCategory f : features)
                                    if (f.getRef().equals(EnsemblIds.ENSEMBL_GENES)) {
                                        setSelectedValue(f);
                                    }
                                loaded = true;
                            }
                        });

                        monitor.end();
                    } catch (Exception ex) {
                        monitor.exception(ex);
                    }
                }
            });
        }
    }

    @Override
    public void updateModel() {
        importer.setFeatCategory(getFeatureCategory());
    }

    @NotNull
    private FeatureCategory getFeatureCategory() {
        return (FeatureCategory) getSelectedValue();
    }
}
