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
package org.gitools.ui.app.heatmap.panel.details.boxes;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.decorator.Decoration;
import org.gitools.heatmap.decorator.Decorator;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.ui.app.actions.edit.EditLayerAction;
import org.gitools.ui.core.actions.ActionSet;
import org.gitools.ui.core.actions.dynamicactions.DynamicActionsManager;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapLayerAction;
import org.gitools.ui.core.components.boxes.DetailsBox;
import org.gitools.utils.events.EventUtils;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;


public class LayersBox extends DetailsBox {
    /**
     * @param title   Optional title of the details table
     * @param actions
     */
    public LayersBox(String title, ActionSet actions, Heatmap heatmap) {
        super(title, actions, heatmap);
    }

    @Override
    public void registerListeners() {
        Heatmap heatmap = getHeatmap();
        PropertyChangeListener updateLayers = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (EventUtils.isAny(evt, HeatmapDimension.class,
                        HeatmapDimension.PROPERTY_FOCUS,
                        HeatmapDimension.PROPERTY_SELECTED,
                        HeatmapDimension.PROPERTY_VISIBLE)
                        ) {
                    update();
                }
            }
        };

        heatmap.getRows().addPropertyChangeListener(updateLayers);
        heatmap.getColumns().addPropertyChangeListener(updateLayers);

        heatmap.getLayers().addPropertyChangeListener(updateLayers);
        heatmap.getLayers().getTopLayer().addPropertyChangeListener(updateLayers);


    }

    @Override
    public void update() {
        Heatmap heatmap = getHeatmap();
        String col = heatmap.getColumns().getFocus();
        String row = heatmap.getRows().getFocus();

        if (col != null && row != null) {

            Decorator decorator = heatmap.getLayers().getTopLayer().getDecorator();
            Decoration decoration = new Decoration();
            boolean showValue = decorator.isShowValue();
            decorator.setShowValue(true);
            decoration.reset();
            HeatmapLayer layer = heatmap.getLayers().getTopLayer();
            decorator.decorate(decoration, layer.getLongFormatter(), heatmap, layer, row, col);
            decorator.setShowValue(showValue);

            this.setTitle("Values: " + decoration.getFormatedValue());
        } else {
            this.setTitle("Values");
        }

        List<DetailsDecoration> layersDetails = new ArrayList<>();
        heatmap.getLayers().populateDetails(layersDetails, heatmap, heatmap.getRows().getFocus(), heatmap.getColumns().getFocus());
        this.draw(layersDetails);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    protected void onMouseSingleClick(DetailsDecoration detail) {
        getHeatmap().getLayers().setTopLayerIndex(detail.getIndex());
    }

    @Override
    protected void onMouseDoubleClick(DetailsDecoration detail) {
        Object reference = detail.getReference();

        if (reference instanceof HeatmapLayer) {
            new EditLayerAction((HeatmapLayer) reference).actionPerformed(null);
        }
    }

    @Override
    protected void onMouseRightClick(DetailsDecoration propertyItem, MouseEvent e) {

        if (propertyItem.getReference() instanceof HeatmapLayer) {
            DynamicActionsManager.updatePopupMenu(popupMenu, IHeatmapLayerAction.class, (HeatmapLayer) propertyItem.getReference(), null);
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
