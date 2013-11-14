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
package org.gitools.core.heatmap;

import org.gitools.core.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.core.matrix.MirrorDimension;
import org.gitools.core.matrix.model.*;
import org.gitools.core.persistence.ResourceReference;
import org.gitools.core.persistence.formats.analysis.adapter.ResourceReferenceXmlAdapter;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import static org.gitools.core.matrix.model.MatrixDimension.COLUMNS;
import static org.gitools.core.matrix.model.MatrixDimension.ROWS;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlType(propOrder = {"diagonal", "rows", "columns", "data", "layers"})
public class Heatmap extends AbstractMatrix<HeatmapLayers, HeatmapDimension> implements IMatrixView {

    public static final String PROPERTY_ROWS = "rows";
    public static final String PROPERTY_COLUMNS = "columns";
    public static final String PROPERTY_LAYERS = "layers";

    @XmlTransient
    private PropertyChangeListener propertyListener;

    private HeatmapDimension rows;
    private HeatmapDimension columns;

    private transient HeatmapDimension diagonalRows;

    private boolean diagonal;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IMatrix> data;

    public Heatmap() {
        super(new HeatmapLayers());
        this.rows = new HeatmapDimension();
        this.columns = new HeatmapDimension();
        this.diagonal = false;
    }

    public Heatmap(IMatrix data) {
        this(data, false);
    }

    public Heatmap(IMatrix data, boolean diagonal) {
        super(new HeatmapLayers(data));
        this.rows = new HeatmapDimension(this, data.getIdentifiers(ROWS));
        this.columns = new HeatmapDimension(this, data.getIdentifiers(COLUMNS));
        this.data = new ResourceReference<>("data", data);
        this.diagonal = diagonal;
    }

    public HeatmapDimension getRows() {

        if (diagonal) {
            return diagonalRows;
        }

        return rows;
    }

    public void setRows(@NotNull HeatmapDimension rows) {
        this.rows.removePropertyChangeListener(propertyListener);
        rows.addPropertyChangeListener(propertyListener);
        HeatmapDimension old = this.rows;
        this.rows = rows;
        firePropertyChange(PROPERTY_ROWS, old, rows);
    }

    public HeatmapDimension getColumns() {
        return columns;
    }

    public void setColumns(@NotNull HeatmapDimension columns) {
        this.columns.removePropertyChangeListener(propertyListener);
        columns.addPropertyChangeListener(propertyListener);
        HeatmapDimension old = this.columns;
        this.columns = columns;
        firePropertyChange(PROPERTY_COLUMNS, old, columns);
    }

    public void detach() {
        if (data != null && data.isLoaded()) {
            data.get().detach();
        }
    }

    public void init() {
        propertyListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                firePropertyChange(evt);
            }
        };
        this.rows.addPropertyChangeListener(propertyListener);
        this.columns.addPropertyChangeListener(propertyListener);
        getLayers().addPropertyChangeListener(propertyListener);

        IMatrix matrix = getData().get();
        this.rows.init(this, matrix.getIdentifiers(ROWS));
        this.columns.init(this, matrix.getIdentifiers(COLUMNS));

        if (this.rows.getHeaderSize() == 0) {
            this.rows.addHeader(new HeatmapTextLabelsHeader());
        }
        if (this.columns.getHeaderSize() == 0) {
            this.columns.addHeader(new HeatmapTextLabelsHeader());
        }

        getLayers().init(matrix);

        if (isDiagonal()) {
            diagonalRows = new MirrorDimension(columns, rows);
        }

        particularInitialization(matrix);

    }

    public boolean isDiagonal() {
        return diagonal;
    }

    public void setDiagonal(boolean diagonal) {
        this.diagonal = diagonal;
    }

    @Override
    public IMatrix getContents() {
        return getData().get();
    }

    public ResourceReference<IMatrix> getData() {
        return data;
    }

    public void setData(ResourceReference<IMatrix> data) {
        this.data = data;
    }

    @Override
    public HeatmapDimension getIdentifiers(MatrixDimension dimension) {

        if (dimension == ROWS) {
            return getRows();
        }

        if (dimension == COLUMNS) {
            return getColumns();
        }

        return null;
    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, String... identifiers) {
        return getContents().get(layer, identifiers);
    }

    @Override
    public <T> void set(IMatrixLayer<T> layer, T value, String... identifiers) {
        getContents().set(layer, value, identifiers);
    }

    private static List<MatrixDimension> dimensions = Arrays.asList(ROWS, COLUMNS);
    @Override
    public List<MatrixDimension> getDimensions() {
        return dimensions;
    }

    @Deprecated
    private void particularInitialization(IMatrix matrix) {
        /*if (matrix instanceof DoubleBinaryMatrix) {
            for (HeatmapLayer layer : getLayers()) {
                BinaryDecorator decorator = new BinaryDecorator();
                decorator.setCutoff(1.0);
                decorator.setComparator(CutoffCmp.EQ);
                layer.setDecorator(decorator);
            }

            getRows().setGridSize(0);
            getColumns().setGridSize(0);
        } else if (matrix instanceof DoubleMatrix) {
            getRows().setGridSize(0);
            getColumns().setGridSize(0);
        } */
    }

}
