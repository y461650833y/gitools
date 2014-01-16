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
package org.gitools.ui.heatmap.panel.settings.headers;

import com.jgoodies.binding.PresentationModel;
import static com.jgoodies.binding.adapter.Bindings.bind;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.ui.platform.settings.ISettingsSection;

import javax.swing.*;

public class DetailsSection implements ISettingsSection {

    private JPanel rootPanel;
    private JTextArea headerDescription;
    private JTextField headerDescriptionLink;
    private JTextField headerValueLink;
    private JTextField headerTitle;

    public DetailsSection(HeatmapHeader heatmapHeader) {
        PresentationModel<HeatmapHeader> header = new PresentationModel<>(heatmapHeader);

        // Fix the TextArea border
        headerDescription.setBorder(BorderFactory.createEtchedBorder());

        // Bind value controls
        bind(headerTitle, header.getModel(HeatmapHeader.PROPERTY_TITLE));
        bind(headerDescription, header.getModel(HeatmapHeader.PROPERTY_DESCRIPTION));
        bind(headerDescriptionLink, header.getModel(HeatmapHeader.PROPERTY_DESCRIPTION_URL));
        bind(headerValueLink, header.getModel(HeatmapHeader.PROPERTY_VALUE_URL));

    }

    @Override
    public String getName() {
        return "Details";
    }

    @Override
    public JPanel getPanel() {
        return rootPanel;
    }
}
