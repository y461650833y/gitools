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
package org.gitools.ui.app.heatmap.panel.settings;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import org.gitools.heatmap.Heatmap;
import org.gitools.resource.Resource;
import org.gitools.ui.platform.settings.ISettingsSection;

import javax.swing.*;

public class HeatmapDetailsSection implements ISettingsSection {
    private JTextArea documentTitle;
    private JTextArea documentDescription;
    private JPanel root;
    private JTextArea authorName;
    private JTextArea authorEmail;

    public HeatmapDetailsSection(Heatmap heatmap) {

        PresentationModel<Heatmap> model = new PresentationModel<>(heatmap);

        // Bind document controls
        Bindings.bind(documentTitle, model.getModel(Resource.PROPERTY_TITLE));
        Bindings.bind(documentDescription, model.getModel(Resource.PROPERTY_DESCRIPTION));

        Bindings.bind(authorName, model.getModel(Heatmap.PROPERTY_AUTHOR_NAME));
        Bindings.bind(authorEmail, model.getModel(Heatmap.PROPERTY_AUTHOR_EMAIL));
    }

    @Override
    public String getName() {
        return "Details";
    }

    @Override
    public JPanel getPanel() {
        return root;
    }

    @Override
    public boolean isDirty() {
        return false;
    }
}

