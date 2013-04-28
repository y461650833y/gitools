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
package org.gitools.core.persistence.locators.filters.gz;

import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.locators.filters.FilterResourceLocator;
import org.gitools.core.persistence.locators.filters.IResourceFilter;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzResourceLocatorAdaptor extends FilterResourceLocator {

    public GzResourceLocatorAdaptor(IResourceFilter filter, IResourceLocator resourceLocator) {
        super(filter, resourceLocator);
    }

    @NotNull
    @Override
    public InputStream openInputStream(IProgressMonitor progressMonitor) throws IOException {
        return new GZIPInputStream(getResourceLocator().openInputStream(progressMonitor));
    }

    @NotNull
    @Override
    public OutputStream openOutputStream() throws IOException {
        return new GZIPOutputStream(getResourceLocator().openOutputStream());
    }

}