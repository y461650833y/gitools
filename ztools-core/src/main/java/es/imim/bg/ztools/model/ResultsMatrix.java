package es.imim.bg.ztools.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;
import es.imim.bg.ztools.model.elements.ArrayElementAdapter;
import es.imim.bg.ztools.model.elements.BeanElementAdapter;
import es.imim.bg.ztools.model.elements.ElementAdapter;
import es.imim.bg.ztools.model.elements.StringElementAdapter;

@XmlType(
		propOrder = {
				"rowAdapter",
				"columnAdapter",
				"cellAdapter"})

@XmlSeeAlso(value = {
		BeanElementAdapter.class, 
		StringElementAdapter.class,
		ArrayElementAdapter.class})
		
public class ResultsMatrix {
	
	protected ObjectMatrix1D rows;
	protected ObjectMatrix1D columns;
	protected ObjectMatrix2D cells;

	protected ElementAdapter rowAdapter;
	protected ElementAdapter columnAdapter;
	protected ElementAdapter cellAdapter;
	
	public ResultsMatrix() {
	}
	
	public ResultsMatrix(
			ObjectMatrix1D rows,
			ObjectMatrix1D columns,
			ObjectMatrix2D cells,
			ElementAdapter rowAdapter,
			ElementAdapter columnAdapter,
			ElementAdapter cellAdapter) {
		
		this.rows = rows;
		this.columns = columns;
		this.cells = cells;
		
		this.rowAdapter = rowAdapter;
		this.columnAdapter = columnAdapter;
		this.cellAdapter = cellAdapter;
	}

	public void makeData() {
		cells = ObjectFactory2D.dense.make(
				rows.size(),
				columns.size());
	}
	
	@XmlTransient
	public ObjectMatrix1D getRows() {
		return rows;
	}
	
	public void setRows(ObjectMatrix1D rows) {
		this.rows = rows;
	}
	
	@XmlTransient
	public ObjectMatrix1D getColumns() {
		return columns;
	}
	
	public void setColumns(ObjectMatrix1D columns) {
		this.columns = columns;
	}
	
	@XmlTransient
	public final ObjectMatrix2D getCells() {
		return cells;
	}

	public void setCells(ObjectMatrix2D cells) {
		this.cells = cells;
	}
	
	@XmlTransient
	public final int getRowCount() {
		return cells.rows();
	}

	public Object getRow(int index) {
		return rows.get(index);
	}
	
	public void setRow(int index, Object row) {
		rows.set(index, row);
	}
	
	@XmlTransient
	public final int getColumnCount() {
		return cells.columns();
	}
	
	public Object getColumn(int index) {
		return columns.get(index);
	}
	
	public void setColumn(int index, Object column) {
		columns.set(index, column);
	}
	
	public final Object getCell(int row, int column) {
		return cells.get(row, column);
	}
	
	public void setCell(int row, int column, Object cell) {
		cells.set(row, column, cell);
	}
	
	public Object getCellValue(int row, int column, int property) {
		return cellAdapter.getValue(
				cells.get(row, column), property);
	}
	
	public Object getCellValue(int row, int column, String id) {
		return cellAdapter.getValue(
				cells.get(row, column), id);
	}
	
	public void setCellValue(int row, int column, int property, Object value) {
		cellAdapter.setValue(
				cells.get(row, column), property, value);
	}
	
	public void setCellValue(int row, int column, String id, Object value) {
		cellAdapter.setValue(
				cells.get(row, column), id, value);
	}
	
	//@XmlAnyElement
	@XmlElement
	public ElementAdapter getRowAdapter() {
		return rowAdapter;
	}
	
	public void setRowAdapter(ElementAdapter rowAdapter) {
		this.rowAdapter = rowAdapter;
	}
	
	//@XmlAnyElement
	@XmlElement
	public ElementAdapter getColumnAdapter() {
		return columnAdapter;
	}
	
	public void setColumnAdapter(ElementAdapter columnAdapter) {
		this.columnAdapter = columnAdapter;
	}
	
	//@XmlAnyElement
	@XmlElement
	public ElementAdapter getCellAdapter() {
		return cellAdapter;
	}
	
	public void setCellAdapter(ElementAdapter cellAdapter) {
		this.cellAdapter = cellAdapter;
	}
}
