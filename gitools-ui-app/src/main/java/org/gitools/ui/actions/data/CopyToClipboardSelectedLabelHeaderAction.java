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
package org.gitools.ui.actions.data;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.drawer.HeatmapPosition;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.core.label.LabelProvider;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.ui.heatmap.popupmenus.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.actions.BaseAction;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;


public class CopyToClipboardSelectedLabelHeaderAction extends BaseAction implements IHeatmapHeaderAction {

    private HeatmapHeader header;

    public CopyToClipboardSelectedLabelHeaderAction() {
        super("Copy selected labels to clipboard");
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (header == null) {
            return;
        }

        LabelProvider labelProvider = header.getLabelProvider();
        StringBuilder content = new StringBuilder();
        for (int i : header.getHeatmapDimension().getSelected()) {
            String label = labelProvider.getLabel(i);

            if (label != null && !label.isEmpty()) {
                content.append(label).append('\n');
            }
        }

        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard ();
        clipBoard.setContents(new StringSelection(content.toString()), null);


    }

    @Override
    public void onConfigure(HeatmapHeader header, HeatmapPosition position) {
        setEnabled(header instanceof HeatmapTextLabelsHeader);
        this.header = header;
    }
}