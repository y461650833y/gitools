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
package org.gitools.model;

import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceLocator;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"title", "description", "properties"})
public class Resource extends AbstractModel implements IResource
{

    private static final long serialVersionUID = 5752318457428475330L;

    private static final String TITLE_CHANGED = "titleChanged";
    private static final String DESC_CHANGED = "descChanged";
    private static final String ATTRIBUTES_CHANGED = "attributesChanged";

    /**
     * short description *
     */
    protected String title;

    /**
     * long description *
     */
    private String description;

    /**
     * Extra attributes *
     */
    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    private List<Property> properties = new ArrayList<Property>(0);

	/* constructors */

    @XmlTransient
    private IResourceLocator locator;


    protected Resource()
    {
    }

    public Resource(@NotNull Resource artifact)
    {
        this.title = artifact.getTitle();
        this.description = artifact.getDescription();
        this.properties = (List<Property>) ((ArrayList<Property>) artifact.getProperties()).clone();
    }

	/* getters and setters */

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        String oldValue = this.title;
        this.title = title;
        firePropertyChange(TITLE_CHANGED, oldValue, title);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        String oldValue = this.description;
        this.description = description;
        firePropertyChange(DESC_CHANGED, oldValue, description);
    }

    public List<Property> getProperties()
    {
        return properties;
    }

    public void setProperties(List<Property> properties)
    {
        List<Property> oldValue = this.properties;
        this.properties = properties;
        firePropertyChange(ATTRIBUTES_CHANGED, oldValue, properties);
    }

    public IResourceLocator getLocator()
    {
        return locator;
    }

    public void setLocator(IResourceLocator locator)
    {
        this.locator = locator;
    }
}