/*
 * #%L
 * gitools-core
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
package org.gitools.matrix.model.matrix.element;


import org.gitools.matrix.model.SimpleMatrixLayer;
import org.gitools.matrix.model.SimpleMatrixLayers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @noinspection ALL
 */
@XmlRootElement
public class ArrayElementAdapter extends AbstractElementAdapter
{

    private static final long serialVersionUID = 5864596809781257355L;

    private static class ArrayElementProperty extends SimpleMatrixLayer
    {

        private static final long serialVersionUID = 7803752573190009823L;

        public ArrayElementProperty(String id, String name, String description)
        {
            super(id, double.class, name, description);
        }
    }

    private String[] ids;

    protected ArrayElementAdapter()
    {
    }

    public ArrayElementAdapter(@NotNull String[] ids)
    {
        super(double[].class);

        setProperties(new SimpleMatrixLayers(double.class, ids));
    }

    @Nullable
    @Override
    public Object getValue(@Nullable Object element, int index)
    {
        return element != null ? ((double[]) element)[index] : null;
    }

    @Override
    public void setValue(@Nullable Object element, int index, Object value)
    {
        if (element != null)
        {
            ((double[]) element)[index] = (Double) value;
        }
    }
}