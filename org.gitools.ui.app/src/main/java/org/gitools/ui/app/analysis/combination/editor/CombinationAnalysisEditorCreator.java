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
package org.gitools.ui.app.analysis.combination.editor;


import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.api.components.IEditor;
import org.gitools.api.components.IEditorCreator;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CombinationAnalysisEditorCreator implements IEditorCreator{

    @Override
    public boolean canCreate(Object object) {
        return object instanceof CombinationAnalysis;
    }

    @Override
    public IEditor create(Object object) {
        if (canCreate(object)) {
            return new CombinationAnalysisEditor((CombinationAnalysis) object);
        }
        return null;
    }
}