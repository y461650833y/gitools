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
package org.gitools.ui.core.commands.tools;

import org.gitools.api.matrix.MatrixDimensionKey;
import org.kohsuke.args4j.Option;

public abstract class HeaderTool extends HeatmapTool {

    enum Side {ROWS, COLUMNS}

    @Option(name = "-d", aliases = "--dimension", metaVar = "<dimension>", required = true,
            usage = "Indicate where to add the header: 'rows' or 'columns'.")
    protected MatrixDimensionKey dimensionKey;

    @Option(name = "-p", aliases = "--pattern", metaVar = "<pattern>", required = true,
            usage = "The pattern of annotations as e.g. ${annotation-id}")
    protected String pattern;

    public HeaderTool() {
        super();
    }


    @Override
    public String getName() {
        return "add-header";
    }

}
