package org.gitools.ui.views;

import org.gitools.ui.views.table.TableView;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;
import org.gitools.model.Analysis;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.table.MatrixView;
import org.gitools.model.table.ObjectMatrix;
import org.gitools.model.table.adapter.ResultsMatrixTableContentsAdapter;
import org.gitools.model.table.decorator.ElementDecorator;
import org.gitools.model.table.decorator.ElementDecoratorFactory;
import org.gitools.model.table.decorator.ElementDecoratorNames;
import org.gitools.model.table.element.array.ArrayElementAdapter;
import org.gitools.model.table.element.basic.StringElementAdapter;

public class DemoView extends TableView {

	private static final long serialVersionUID = 2467164492764056062L;
	
	public DemoView(int rows, int cols) {
		super(createModel(rows, cols));
		
		setName("Demo");
	}
	
	private static MatrixFigure createModel(int rows, int cols) {
		MatrixView matrixView = createTable(rows, cols);
		ElementDecorator decorator = ElementDecoratorFactory.create(
				ElementDecoratorNames.PVALUE, matrixView.getCellAdapter());
		
		return new MatrixFigure(matrixView, decorator);
	}

	private static MatrixView createTable(int rows, int cols) {		
		int k = 0;
		DoubleMatrix1D values = DoubleFactory1D.dense.random(2 * rows * cols);
		
		ObjectMatrix2D data = ObjectFactory2D.dense.make(rows, cols);
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				double[] v = new double[] {
					values.getQuick(k++),
					values.getQuick(k++)
				};
				data.setQuick(row, col, v);
			}
		}
		
		final ObjectMatrix1D rowNames = ObjectFactory1D.dense.make(data.rows());
		for (int i = 0; i < rowNames.size(); i++)
			rowNames.setQuick(i, "row " + (i + 1));
		
		final ObjectMatrix1D colNames = ObjectFactory1D.dense.make(data.columns());
		for (int i = 0; i < colNames.size(); i++)
			colNames.setQuick(i, "col " + (i + 1));
		
		ObjectMatrix resultsMatrix = new ObjectMatrix(
				rowNames,
				colNames,   
				data,
				new StringElementAdapter(), 
				new StringElementAdapter(),
				new ArrayElementAdapter(new String[] {"p-value", "corrected-p-value"}));
		
		Analysis analysis = new Analysis();
		analysis.setResults(resultsMatrix);
		
		return new MatrixView(
					new ResultsMatrixTableContentsAdapter(
								resultsMatrix));
	}
}
