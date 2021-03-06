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
package org.gitools.ui.app.actions.data;

import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.ui.app.actions.HeatmapDimensionAction;
import org.gitools.ui.core.Application;
import org.gitools.ui.platform.icons.IconNames;

import java.awt.event.ActionEvent;

public class ShowAllAction extends HeatmapDimensionAction {

    private static final long serialVersionUID = 7110623490709997414L;

    public ShowAllAction(MatrixDimensionKey key) {
        super(key, "<html><i>Show</i> all</html>");

        setSmallIconFromResource(IconNames.get(key).getShowAll16());
        setLargeIconFromResource(IconNames.get(key).getShowAll24());
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        HeatmapDimension dimension = getDimension();

        dimension.showAll();

        Application.get().showNotification("All " + getDimensionLabel() + "s shown in original order");

    }
}
