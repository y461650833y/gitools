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
package org.gitools.ui.biomart.filter;

import org.gitools.biomart.restful.model.Filter;
import org.gitools.biomart.restful.model.FilterDescription;
import org.gitools.biomart.restful.model.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class FilterRadioComponent extends FilterComponent
{

    private final Integer RADIO_HEIGHT = 45;

    public FilterRadioComponent(FilterDescription d, FilterDescriptionPanel descriptionParent)
    {

        super(d, descriptionParent);
        initComponents();

        buildComponent();
    }

    public FilterRadioComponent(Option o)
    {

        super(o);
        initComponents();

        buildComponent();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        radioComponents = new javax.swing.ButtonGroup();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 45, Short.MAX_VALUE));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup radioComponents;
    // End of variables declaration//GEN-END:variables

    //FIXME : Test initialisation String list of options
    private void buildComponent()
    {

        String[] options;

        //Retrieve list of text from options
        options = getListTextOptions();

        if (options != null)
        {
            JRadioButton radio = null;
            this.setLayout(new GridLayout(options.length, 1));

            for (int i = 0; i < options.length; i++)
            {
                radio = new JRadioButton(options[i], (i == 0) ? true : false);
                radioComponents.add(radio);
                this.add(radio);
            }
            currentHeight = RADIO_HEIGHT * options.length;
        }


    }

    @NotNull
    @Override
    // FIXME : Check if get filter from radio value/s is correct
    public List<Filter> getFilters()
    {

        List<Filter> filters = new ArrayList<Filter>();

        Filter f = new Filter();

        JRadioButton r = null;

        // Could happen filterDescription null, if this component is a child (belongs to a container component)
        if (filterDescription != null && filterDescription.getInternalName() != null)
        {
            f.setName(filterDescription.getInternalName());
        }

        for (Enumeration<AbstractButton> enumRadio = radioComponents.getElements(); enumRadio.hasMoreElements(); )
        {

            r = (JRadioButton) enumRadio.nextElement();
            if (r.isSelected())
            {

                if (r.getText().toLowerCase().equals("excluded"))
                {
                    f.setRadio(true);
                    f.setValue("1");
                }
                else
                {
                    if (r.getText().toLowerCase().equals("only"))
                    {
                        f.setValue("0");
                        f.setRadio(true);
                    }
                    else
                    {
                        f.setValue(r.getText());
                    }
                }
            }
        }

        filters.add(f);

        return filters;

    }

    @NotNull
    @Override
    //Always render filter from select component filter
    public Boolean hasChanged()
    {
        return true;

    }

    /**
     * If the component is child the text of each radio is obtained from filterOptions
     * component
     *
     * @param child
     * @return
     */
    @Nullable
    private String[] getListTextOptions()
    {
        String res[] = null;
        if (filterOptions != null)
        {
            res = new String[filterOptions.getOptions().size()];
            for (int i = 0; i < filterOptions.getOptions().size(); i++)
            {
                res[i] = filterOptions.getOptions().get(i).getValue();
            }

        }
        else
        {
            if (filterDescription == null)
            {
                return res;
            }

            res = new String[filterDescription.getOptions().size()];

            for (int i = 0; i < filterDescription.getOptions().size(); i++)
            {
                res[i] = filterDescription.getOptions().get(i).getValue();
            }
        }
        return res;
    }

    @Override
    public void setListOptions(List<Option> optionList)
    {
        return;
    }

}
