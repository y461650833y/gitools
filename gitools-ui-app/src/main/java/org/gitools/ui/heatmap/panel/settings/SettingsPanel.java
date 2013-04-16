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
package org.gitools.ui.heatmap.panel.settings;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.AbstractValueModel;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.HeatmapLayers;
import org.gitools.model.Resource;
import org.gitools.model.decorator.Decorator;
import org.gitools.ui.heatmap.panel.settings.decorators.DecoratorPanel;
import org.gitools.ui.heatmap.panel.settings.decorators.DecoratorPanelContainer;
import org.gitools.ui.heatmap.panel.settings.decorators.DecoratorPanels;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.decorators.DecoratorArchive;
import org.gitools.ui.settings.decorators.DecoratorArchivePersistance;
import org.gitools.ui.settings.decorators.LoadDecoratorDialog;
import org.gitools.ui.settings.decorators.SaveDecoratorDialog;
import org.gitools.ui.utils.landf.MyWebColorChooserField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SettingsPanel
{

    // Components
    private JPanel rootPanel;
    private JComboBox decoratorPanelSelector;
    private JPanel decoratorPanels;
    private JTextField gridRowsColor;
    private JTextField gridColumnsColor;
    private JCheckBox cellSizeKeepRatio;
    private JSpinner cellSizeRows;
    private JSpinner cellSizeColumns;
    private JTextArea documentTitle;
    private JTextArea documentDescription;
    private JSpinner gridRowsSize;
    private JSpinner gridColumnsSize;
    private JLabel editRowHeaders;
    private JLabel editColumnsHeader;
    private JLabel newRowsHeader;
    private JLabel newColumnsHeader;
    private JLabel colorScaleSave;
    private JLabel colorScaleOpen;
    private JComboBox layerselector;

    public SettingsPanel(final Heatmap heatmap)
    {
        PresentationModel<Heatmap> model = new PresentationModel<Heatmap>(heatmap);

        // Data models
        PresentationModel<HeatmapDimension> rows = new PresentationModel<HeatmapDimension>(model.getModel(Heatmap.PROPERTY_ROWS));
        PresentationModel<HeatmapDimension> columns = new PresentationModel<HeatmapDimension>(model.getModel(Heatmap.PROPERTY_COLUMNS));
        PresentationModel<HeatmapLayers> layers = new PresentationModel<HeatmapLayers>(model.getModel(Heatmap.PROPERTY_LAYERS));

        // Layer selector
        Bindings.bind(layerselector, new SelectionInList<HeatmapLayer>(
                heatmap.getLayers().toList(),
                layers.getModel(HeatmapLayers.PROPERTY_TOP_LAYER)
        ));

        // Bind color scale controls
        DecoratorPanels decorators = new DecoratorPanels();
        DecoratorPanelContainer decoratorsPanels = (DecoratorPanelContainer) this.decoratorPanels;
        decoratorsPanels.init(decorators, heatmap);
        Bindings.bind(decoratorPanelSelector, new SelectionInList<DecoratorPanel>(
                decorators,
                decoratorsPanels.getCurrentPanelModel()
        ));

        colorScaleSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        colorScaleSave.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                saveScale(heatmap.getLayers().getTopLayer());
            }
        });

        colorScaleOpen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        colorScaleOpen.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                loadScale(heatmap.getLayers().getTopLayer());
            }
        });

        // Bind grid controls
        Bindings.bind(gridRowsColor, "color", rows.getModel(HeatmapDimension.PROPERTY_GRID_COLOR));
        Bindings.bind(gridColumnsColor, "color", columns.getModel(HeatmapDimension.PROPERTY_GRID_COLOR));
        gridRowsSize.setModel(SpinnerAdapterFactory.createNumberAdapter(rows.getModel(HeatmapDimension.PROPERTY_GRID_SIZE), 1, 0, 10, 1));
        gridColumnsSize.setModel(SpinnerAdapterFactory.createNumberAdapter(columns.getModel(HeatmapDimension.PROPERTY_GRID_SIZE), 1, 0, 10, 1));

        // Bind document controls
        Bindings.bind(documentTitle, model.getModel(Resource.PROPERTY_TITLE));
        Bindings.bind(documentDescription, model.getModel(Resource.PROPERTY_DESCRIPTION));

        // Bind cell size controls
        AbstractValueModel cellSizeRowsModel = rows.getModel(HeatmapDimension.PROPERTY_CELL_SIZE);
        cellSizeRows.setModel(SpinnerAdapterFactory.createNumberAdapter(cellSizeRowsModel, 1, 0, 300, 1));
        AbstractValueModel cellSizeColumnsModel = columns.getModel(HeatmapDimension.PROPERTY_CELL_SIZE);
        cellSizeColumns.setModel( SpinnerAdapterFactory.createNumberAdapter(cellSizeColumnsModel , 1, 0, 300, 1) );
        cellSizeKeepRatio.setModel(new KeepRatioModel(cellSizeRowsModel, cellSizeColumnsModel));


    }

    private void saveScale(HeatmapLayer topLayer) {

        Decorator d = topLayer.getDecorator();
        DecoratorArchivePersistance archivePersistance = new DecoratorArchivePersistance();
        DecoratorArchive archive = archivePersistance.load();
        SaveDecoratorDialog dialog = new SaveDecoratorDialog(AppFrame.get());
        dialog.setExistingScaleNames(archive.getDecorators().keySet());
        dialog.setName(d.getName());
        dialog.setVisible(true);
        if (dialog.isCancelled()) {
            return;
        }

        d.setName(dialog.getScaleName());
        archive.add(d);
        archivePersistance.save(archive);
    }

    private void loadScale(HeatmapLayer topLayer) {

        DecoratorArchivePersistance archivePersistance =
                new DecoratorArchivePersistance();
        DecoratorArchive archive = archivePersistance.load();

        Decorator d = topLayer.getDecorator();

        LoadDecoratorDialog dialog = new LoadDecoratorDialog(
                AppFrame.get(),
                archive.getDecorators().values().toArray(),
                d.getClass());
        dialog.setVisible(true);
        if (dialog.isCancelled()) {
            return;
        }
        Decorator loadedDecorator = dialog.getSelectedDecorator();

        topLayer.setDecorator(loadedDecorator);

    }

    public JPanel getRootPanel()
    {
        return rootPanel;
    }


    private void createUIComponents()
    {
        this.gridRowsColor = new MyWebColorChooserField();
        this.gridColumnsColor = new MyWebColorChooserField();
        this.decoratorPanels = new DecoratorPanelContainer();
    }
}