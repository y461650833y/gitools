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
package org.gitools.persistence.locators.filters.zip;

import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.locators.filters.AbstractResourceFilter;
import org.jetbrains.annotations.NotNull;

public class ZipResourceFilter extends AbstractResourceFilter
{
    public static final String SUFFIX = "zip";

    public ZipResourceFilter()
    {
        super(SUFFIX);
    }

    @NotNull
    @Override
    public IResourceLocator apply(IResourceLocator resourceLocator)
    {
        if (isFiltered(resourceLocator.getExtension()))
        {

            String entryName = removeExtension(resourceLocator.getName());

            return new ZipResourceLocatorAdaptor(entryName, this, resourceLocator);
        }

        return resourceLocator;
    }
}
