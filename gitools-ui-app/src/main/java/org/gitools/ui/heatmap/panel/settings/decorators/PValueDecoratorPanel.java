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
package org.gitools.ui.heatmap.panel.settings.decorators;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.matrix.model.IMatrixLayer;
import org.gitools.model.decorator.impl.PValueDecorator;
import org.gitools.ui.utils.landf.MyWebColorChooserField;

import javax.swing.*;

public class PValueDecoratorPanel extends DecoratorPanel {
    private JPanel rootPanel;
    private JTextField minColor;
    private JTextField maxColor;
    private JTextField nonColor;
    private JTextField emptyColor;
    private JComboBox correctedValue;
    private JCheckBox useCorrection;
    private JSpinner significance;

    public PValueDecoratorPanel() {
        super("P-Value scale", new PValueDecorator());
    }

    @Override
    public void bind() {
        Bindings.bind(minColor, "color", model(PValueDecorator.PROPERTY_MIN_COLOR));
        Bindings.bind(maxColor, "color", model(PValueDecorator.PROPERTY_MAX_COLOR));
        Bindings.bind(emptyColor, "color", model(PValueDecorator.PROPERTY_EMPTY_COLOR));
        Bindings.bind(nonColor, "color", model(PValueDecorator.PROPERTY_NON_SIGNIFICANT_COLOR));

        Bindings.bind(useCorrection, model(PValueDecorator.PROPERTY_USE_CORRECTION));

        Bindings.bind(correctedValue, new SelectionInList<HeatmapLayer>(
                getLayers(),
                new ValueHolder(),
                model(PValueDecorator.PROPERTY_CORRECTED_VALUE)
        ));

        significance.setModel(
                SpinnerAdapterFactory.createNumberAdapter(
                        model(PValueDecorator.PROPERTY_SIGNIFICANCE),
                        Double.valueOf(0.05),
                        Double.valueOf(0),
                        Double.valueOf(1),
                        Double.valueOf(0.01)
                )
        );
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        minColor = new MyWebColorChooserField();
        maxColor = new MyWebColorChooserField();
        emptyColor = new MyWebColorChooserField();
        nonColor = new MyWebColorChooserField();
    }
}
