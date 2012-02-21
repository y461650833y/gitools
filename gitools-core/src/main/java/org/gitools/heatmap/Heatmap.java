/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.heatmap;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.heatmap.xml.HeatmapMatrixViewXmlAdapter;

import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.Figure;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;

/*TODO: Heatmap should implement IMatrixView
 * and handle movement and visibility synchronized
 * between annotations, clusters and so on.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement()
public class Heatmap
		extends Figure
		implements Serializable {

	private static final long serialVersionUID = 325437934312047512L;

	public static final String CELL_DECORATOR_CHANGED = "cellDecorator";
	public static final String MATRIX_VIEW_CHANGED = "matrixView";
	public static final String CELL_SIZE_CHANGED = "cellSize";
	public static final String ROW_DIMENSION_CHANGED = "rowDim";
	public static final String COLUMN_DIMENSION_CHANGED = "columnDim";
	
	@Deprecated public static final String ROW_LABELS_HEADER_CHANGED = "rowDecorator";
	@Deprecated public static final String COLUMN_LABELS_HEADER_CHANGED = "columnDecorator";
	@Deprecated public static final String GRID_PROPERTY_CHANGED = "gridProperty";

	@XmlJavaTypeAdapter(HeatmapMatrixViewXmlAdapter.class)
	private IMatrixView matrixView;

	// Cells

	private ElementDecorator activeDecorator;
	private ElementDecorator[] cellDecorators;
	private int cellWidth;
	private int cellHeight;

	private HeatmapDim rowDim;

	private HeatmapDim columnDim;

	// Other

	@XmlTransient
	private boolean showBorders;

	PropertyChangeListener propertyListener;
	
	public Heatmap() {
		this(
				null, null, //FIXME should it be null ?
				new HeatmapTextLabelsHeader(),
				new HeatmapTextLabelsHeader());
	}

	public Heatmap(IMatrixView matrixView) {
		this(
				matrixView,
				cellDecoratorFromMatrix(matrixView),
				new HeatmapTextLabelsHeader(),
				new HeatmapTextLabelsHeader());
	}
	
	public Heatmap(
			IMatrixView matrixView,
			ElementDecorator cellDecorator,
			HeatmapTextLabelsHeader rowsLabelsHeader,
			HeatmapTextLabelsHeader columnLabelsHeader) {

		propertyListener = new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChange(evt); } };

		this.matrixView = matrixView;
		this.matrixView.addPropertyChangeListener(propertyListener);
		
		this.activeDecorator = cellDecorator;
		this.activeDecorator.addPropertyChangeListener(propertyListener);

		this.cellWidth = 14;
		this.cellHeight = 14;
		this.showBorders = false;
				
		this.rowDim = new HeatmapDim();
		//rowsLabelsHeader.setSize(220);
		this.rowDim.addHeader(rowsLabelsHeader);
		this.rowDim.addPropertyChangeListener(propertyListener);

		this.columnDim = new HeatmapDim();
		//columnLabelsHeader.setSize(130);
		this.columnDim.addHeader(columnLabelsHeader);
		this.columnDim.addPropertyChangeListener(propertyListener);
	}

	private static ElementDecorator cellDecoratorFromMatrix(
			IMatrixView matrixView) {
		
		ElementDecorator decorator = null;
		
		IElementAdapter adapter = matrixView.getCellAdapter();
		List<IElementAttribute> attributes = matrixView.getCellAttributes();
		
		int attrIndex = matrixView.getSelectedPropertyIndex();
		if (attrIndex >= 0 && attrIndex < attributes.size()) {
			Class<?> elementClass = attributes.get(attrIndex).getValueClass();
			
			//FIXME No funciona
			if (Double.class.isInstance(elementClass)
					|| double.class.isInstance(elementClass))
				decorator = ElementDecoratorFactory.create(
						ElementDecoratorNames.LINEAR_TWO_SIDED, adapter);
		}
		
		if (decorator == null)
			decorator = ElementDecoratorFactory.create(
					ElementDecoratorNames.LINEAR_TWO_SIDED, adapter);
		
		return decorator;
	}

	// Matrix View

	public final IMatrixView getMatrixView() {
		return matrixView;
	}

	public final void setMatrixView(IMatrixView matrixView) {
		this.matrixView.removePropertyChangeListener(propertyListener);
		matrixView.addPropertyChangeListener(propertyListener);
		final IMatrixView old = this.matrixView;
		this.matrixView = matrixView;
		firePropertyChange(MATRIX_VIEW_CHANGED, old, matrixView);
	}

	// Cells

	public final ElementDecorator getActiveCellDecorator() {
		int propIndex = getMatrixView().getSelectedPropertyIndex();
		return cellDecorators[propIndex];
	}

	public final void changeActiveCellDecorator(int newindex) {
		final int oldindex = getMatrixView().getSelectedPropertyIndex();
		getMatrixView().setSelectedPropertyIndex(newindex);
		this.cellDecorators[oldindex].removePropertyChangeListener(propertyListener);
		this.cellDecorators[newindex].addPropertyChangeListener(propertyListener);
	}
    
    public void setActiveDecorator(ElementDecorator newDecorator) throws Exception {
        int propIndex = getMatrixView().getSelectedPropertyIndex();
        this.cellDecorators[propIndex].removePropertyChangeListener(propertyListener);
		newDecorator.addPropertyChangeListener(propertyListener);
        ElementDecorator old = this.cellDecorators[propIndex];
        if (old.getAdapter().getElementClass().equals(
                newDecorator.getAdapter().getElementClass())) {
            this.cellDecorators[propIndex] = newDecorator;
            newDecorator.setValueIndex(propIndex);
            firePropertyChange(CELL_DECORATOR_CHANGED, old, newDecorator);
        } else {
            throw new Exception("Substituting decorator not of same class");
        }
    }

	public final void setCellDecorators(ElementDecorator[] decorators) {
		int propIndex = getMatrixView().getSelectedPropertyIndex();
		ElementDecorator old = null;
		if (this.cellDecorators != null) {
			this.cellDecorators[propIndex].removePropertyChangeListener(propertyListener);
			old = this.cellDecorators[propIndex];
		}
		decorators[propIndex].addPropertyChangeListener(propertyListener);

		this.cellDecorators = decorators;
		firePropertyChange(CELL_DECORATOR_CHANGED, old, decorators[propIndex]);
	}

	public final ElementDecorator[] getCellDecorators() {
		return this.cellDecorators;
	}

	public int getCellWidth() {
		return cellWidth;
	}

	public void setCellWidth(int cellWidth) {
		int old = this.cellWidth;
		this.cellWidth = cellWidth;
		firePropertyChange(CELL_SIZE_CHANGED, old, cellWidth);
	}

	public int getCellHeight() {
		return cellHeight;
	}

	public void setCellHeight(int cellHeight) {
		int old = this.cellHeight;
		this.cellHeight = cellHeight;
		firePropertyChange(CELL_SIZE_CHANGED, old, cellHeight);
	}

	// Dimensions

	public HeatmapDim getRowDim() {
		return rowDim;
	}

	public void setRowDim(HeatmapDim rowDim) {
		this.rowDim.removePropertyChangeListener(propertyListener);
		rowDim.addPropertyChangeListener(propertyListener);
		HeatmapDim old = this.rowDim;
		this.rowDim = rowDim;
		firePropertyChange(ROW_DIMENSION_CHANGED, old, rowDim);
	}

	public HeatmapDim getColumnDim() {
		return columnDim;
	}

	public void setColumnDim(HeatmapDim columnDim) {
		this.columnDim.removePropertyChangeListener(propertyListener);
		columnDim.addPropertyChangeListener(propertyListener);
		HeatmapDim old = this.columnDim;
		this.columnDim = columnDim;
		firePropertyChange(COLUMN_DIMENSION_CHANGED, old, columnDim);
	}

	// Other

	public boolean isShowBorders() {
		return showBorders;
	}

	public void setShowBorders(boolean showBorders) {
		this.showBorders = showBorders;
	}

	// Generated values

	@Deprecated //FIXME
	public String getColumnLabel(int index) {
		String label = matrixView.getColumnLabel(index);
		return label;
		/*HeatmapLabelsDecoration decoration = new HeatmapLabelsDecoration();
		columnDim.getLabelsHeader().decorate(decoration, label);
		return decoration.getText();*/
	}

	@Deprecated //FIXME
	public String getRowLabel(int index) {
		String label = matrixView.getRowLabel(index);
		return label;
		/*HeatmapLabelsDecoration decoration = new HeatmapLabelsDecoration();
		rowDim.getLabelsHeader().decorate(decoration, label);
		return decoration.getText();*/
	}

	@Deprecated //FIXME
	public String getColumnLinkUrl(int index) {
		return "";
		/*String header = matrixView.getColumnLabel(index);
		HeatmapLabelsDecoration decoration = new HeatmapLabelsDecoration();
		columnDim.getLabelsHeader().decorate(decoration, header);
		return decoration.getUrl();*/
	}

	@Deprecated //FIXME
	public String getRowLinkUrl(int index) {
		return "";
		/*String header = matrixView.getRowLabel(index);
		HeatmapLabelsDecoration decoration = new HeatmapLabelsDecoration();
		rowDim.getLabelsHeader().decorate(decoration, header);
		return decoration.getUrl();*/
	}
}
