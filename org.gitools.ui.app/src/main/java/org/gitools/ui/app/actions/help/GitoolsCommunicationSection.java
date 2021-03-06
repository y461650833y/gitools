/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.app.actions.help;

import com.jgoodies.binding.PresentationModel;
import org.gitools.ui.platform.settings.ISettingsSection;
import org.gitools.ui.platform.settings.Settings;
import org.gitools.utils.formatter.IntegerFormat;

import javax.swing.*;

import static com.jgoodies.binding.adapter.Bindings.bind;
import static com.jgoodies.binding.value.ConverterFactory.createStringConverter;

public class GitoolsCommunicationSection implements ISettingsSection {
    private JPanel panel;
    private JCheckBox IGVBox;
    private JTextField port;
    private JCheckBox portBox;
    private JTextField IGVUrl;
    private JCheckBox proxyEnabled;
    private JFormattedTextField proxyPort;
    private JFormattedTextField proxyHost;

    public GitoolsCommunicationSection(Settings settings) {

        PresentationModel<Settings> model = new PresentationModel<>(settings);

        bind(proxyEnabled, model.getModel(Settings.PROPERTY_PROXY_ENABLED));
        bind(proxyPort, createStringConverter(model.getModel(Settings.PROPERTY_PROXY_PORT), IntegerFormat.get()));
        bind(proxyHost, model.getModel(Settings.PROPERTY_PROXY_HOST));

        bind(portBox, model.getModel(Settings.PROPERTY_PORT_ENABLED));
        bind(port, createStringConverter(model.getModel(Settings.PROPERTY_PORT), IntegerFormat.get()));
        bind(IGVBox, model.getModel(Settings.PROPERTY_IGV_ENABLED));
        bind(IGVUrl, model.getModel(Settings.PROPERTY_IGV_URL));

    }

    @Override
    public String getName() {
        return "Communication";
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

}
