package org.gitools.model.table;

import org.gitools.model.matrix.TableFormatException;
import org.gitools.model.matrix.element.IElementAdapter;

public class TableView implements ITable {
	// Clase ad-hoc para adpatarse a una TableFigue

	ITable contents;

	public TableView() {

	}

	public TableView(ITable table) {
		this.contents = table;
	}

	@Override
	public void addColumn(ITableColumn column) throws TableFormatException {
		contents.addColumn(column);
	}

	@Override
	public void removeColumn(int index) {
		contents.removeColumn(index);
	}

	@Override
	public IElementAdapter getCellColumnAdapter(int column) {
		return contents.getCellColumnAdapter(column);
	}

	@Override
	public ITableColumn getColumn(int index) {
		return contents.getColumn(index);

	}

	@Override
	public int getColumnCount() {
		return contents.getColumnCount();
	}

	@Override
	public String getHeader(int column) {
		return contents.getHeader(column);
	}

	@Override
	public int getRowCount() {
		return contents.getRowCount();
	}

	@Override
	public Object getValue(int row, int column) {
		return contents.getValue(row, column);
	}
}
