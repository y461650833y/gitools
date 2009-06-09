package org.gitools.ui.editor.matrix;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.velocity.VelocityContext;
import org.gitools.model.IModel;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.matrix.element.IElementProperty;
import org.gitools.stats.test.results.BinomialResult;
import org.gitools.stats.test.results.CombinationResult;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.stats.test.results.FisherResult;
import org.gitools.stats.test.results.ZScoreResult;
import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.FileActionSet;
import org.gitools.ui.actions.MenuActionSet;
import org.gitools.ui.actions.TableActionSet;
import org.gitools.ui.editor.AbstractEditor;
import org.gitools.ui.panels.TemplatePane;
import org.gitools.ui.panels.matrix.MatrixPanel;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

import edu.upf.bg.GenericFormatter;
import edu.upf.bg.colorscale.PValueColorScale;
import edu.upf.bg.colorscale.ZScoreColorScale;

public class MatrixEditor extends AbstractEditor {

	private static final long serialVersionUID = -540561086703759209L;

	private static final String defaultTemplateName = "/vm/details/noselection.vm";
	
	private MatrixFigure model;
	
	private CellConfigPage cellsConfigPage;
	
	private MatrixPanel matrixPanel;
	
	private JTabbedPane tabbedPane;
	
	protected boolean blockSelectionUpdate;

	private PropertyChangeListener modelListener;
	private PropertyChangeListener cellDecoratorListener;

	private HeaderConfigPage rowsConfigPage;

	private HeaderConfigPage columnsConfigPage;

	public MatrixEditor(MatrixFigure model) {
		
		this.model = model;
		
		final IMatrixView matrixView = model.getMatrixView();
	
		this.blockSelectionUpdate = false;
		
		createComponents();
		
		modelListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				modelPropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			}
		};
		
		cellDecoratorListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				decoratorPropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			}
		};
		
		model.addPropertyChangeListener(modelListener);
		
		model.getCellDecorator().addPropertyChangeListener(cellDecoratorListener);
		
		matrixView.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				matrixPropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			}
		});
	}

	protected void modelPropertyChange(
			String propertyName, Object oldValue, Object newValue) {
		
		if (MatrixFigure.CELL_DECORATOR_CHANGED.equals(propertyName)) {
			final ElementDecorator prevDecorator = (ElementDecorator) oldValue;
			prevDecorator.removePropertyChangeListener(cellDecoratorListener);
			final ElementDecorator nextDecorator = (ElementDecorator) newValue;
			nextDecorator.addPropertyChangeListener(cellDecoratorListener);
			matrixPanel.setCellDecorator(model.getCellDecorator());
		}
		else if (MatrixFigure.PROPERTY_CHANGED.equals(propertyName)) {
			matrixPanel.setShowGrid(model.isShowGrid());
			matrixPanel.setGridColor(model.getGridColor());
			matrixPanel.setCellSize(model.getCellSize());
		}
		
		matrixPanel.refresh();
	}
	
	protected void decoratorPropertyChange(
			String propertyName, Object oldValue, Object newValue) {
		
		matrixPanel.refresh();
	}
	
	protected void matrixPropertyChange(
			String propertyName, Object oldValue, Object newValue) {

		/*if (ITable.CELL_DECORATION_PROPERTY.equals(propertyName)) {
			cellDecorator.setConfig(
					table.getCellDecoration(
							table.getCurrentProperty()));
			
			refreshColorMatrixWidth();
			colorMatrixPanel.refresh();
		}*/
		/*else if (ITable.SELECTION_MODE_PROPERTY.equals(propertyName)) {
			SelectionMode mode = (SelectionMode) newValue;
			colorMatrixPanel.setSelectionMode(mode);
			colorMatrixPanel.refresh();
			refreshActions();
		}*/
		if (IMatrixView.SELECTION_CHANGED.equals(propertyName)
			|| IMatrixView.VISIBLE_COLUMNS_CHANGED.equals(propertyName)) {
			
			if (!blockSelectionUpdate) {
				blockSelectionUpdate = true;
				if (IMatrixView.VISIBLE_COLUMNS_CHANGED.equals(propertyName))
					matrixPanel.refreshColumns();
				
				//System.out.println("Start selection change:");
				matrixPanel.setSelectedCells(
						getTable().getSelectedColumns(),
						getTable().getSelectedRows());
				matrixPanel.refresh();
				//System.out.println("End selection change.");
				
				blockSelectionUpdate = false;
			}
		}
		else if (IMatrixView.SELECTED_LEAD_CHANGED.equals(propertyName)) {
			refreshCellDetails();
		}
		else if (IMatrixView.VISIBLE_COLUMNS_CHANGED.equals(propertyName)) {
			matrixPanel.refresh();
		}
		else if (IMatrixView.VISIBLE_ROWS_CHANGED.equalsIgnoreCase(propertyName)) {
			matrixPanel.refresh();
		}
		else if (IMatrixView.CELL_VALUE_CHANGED.equals(propertyName)) {
			matrixPanel.refresh();
		}
		else if (IMatrixView.CELL_DECORATION_CONTEXT_CHANGED.equals(propertyName)) {
			if (oldValue != null)
				((IModel) oldValue).removePropertyChangeListener(modelListener);
			
			((IModel) newValue).addPropertyChangeListener(modelListener);
		}
	}

	private void refreshCellDetails() {		
		int row = getTable().getSelectionLeadRow();
		int rowCount = getTable().getRowCount();
		int column = getTable().getSelectionLeadColumn();
		int columnCount = getTable().getColumnCount();
		
		VelocityContext context = new VelocityContext();
		String templateName = defaultTemplateName;
		
		if (column >= 0 && column < columnCount && row >= 0 && row < rowCount) {
			final IElementAdapter columnAdapter = getTable().getColumnAdapter();
			final Object columnElement = getTable().getColumn(column);
			
			final IElementAdapter rowAdapter = getTable().getRowAdapter();
			final Object rowElement = getTable().getRow(row);
			
			final IElementAdapter cellAdapter = getTable().getCellAdapter();
			final Object cellElement = getTable().getCell(row, column);
			
			templateName = getTemplateNameFromObject(cellElement);

			if (templateName != null) {				
				context.put("fmt", new GenericFormatter());
				
				context.put("zscoreScale", new ZScoreColorScale()); //FIXME
				context.put("pvalueScale", new PValueColorScale()); //FIXME
				
				context.put("columnAdapter", columnAdapter);
				context.put("columnElement", columnElement);
				
				context.put("rowAdapter", rowAdapter);
				context.put("rowElement", rowElement);
				
				context.put("cellAdapter", cellAdapter);
				context.put("cellElement", cellElement);
				
				final List<IElementProperty> properties = 
					cellAdapter.getProperties();
				
				final Map<String, Object> cellMap = 
					new HashMap<String, Object>();
				
				for (int index = 0; index < properties.size(); index++) {
					final IElementProperty prop = properties.get(index);
					cellMap.put(prop.getId(), 
							cellAdapter.getValue(cellElement, index));
				}
				
				context.put("cell", cellMap);
			}
		}
		/*else if (column < 0) {
			System.out.println("row:" + row);
		}
		else if (row < 0) {
			System.out.println("col:" + column);
		}*/
		
		//System.out.println("refreshCellDetails(" + row + ", " + column + ")");
		
		try {
			final TemplatePane templatePane = AppFrame.instance().getDetailsPane();
			
			templatePane.setTemplate(templateName);
			templatePane.setContext(context);
			templatePane.render();
		}
		catch (Exception e) {
			e.printStackTrace(); //FIXME
		}
	}
	
	private String getTemplateNameFromObject(Object object) {
		String templateName = "default.vm";
		if (object instanceof BinomialResult)
			templateName = "binomial.vm";
		else if (object instanceof FisherResult)
			templateName = "fisher.vm";
		else if (object instanceof ZScoreResult)
			templateName = "zscore.vm";
		else if (object instanceof CombinationResult)
			templateName = "combination.vm";
		else if (object instanceof CommonResult)
			templateName = "common.vm";
		
		return "/vm/details/" + templateName;
	}

	private void createComponents() {
		
		/* Color matrix */
		
		matrixPanel = new MatrixPanel();
		matrixPanel.setMinimumSize(new Dimension(200, 200));
		matrixPanel.setModel(getTable());
		matrixPanel.setShowGrid(model.isShowGrid());
		matrixPanel.setGridColor(model.getGridColor());
		matrixPanel.setCellSize(model.getCellSize());
		
		matrixPanel.setCellDecorator(model.getCellDecorator());
		
		ListSelectionListener selListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel src = (ListSelectionModel) e.getSource();
				src.hashCode();
				
				if (!e.getValueIsAdjusting() && !blockSelectionUpdate) {
					blockSelectionUpdate = true;
					
					//System.out.println("Selection listener.");
					
					getTable().setSelectedRows(
							matrixPanel.getSelectedRows());
					getTable().setSelectedColumns(
							matrixPanel.getSelectedColumns());
					
					int colIndex = matrixPanel.getSelectedLeadColumn();
					int rowIndex = matrixPanel.getSelectedLeadRow();
					
					getTable().setLeadSelection(rowIndex, colIndex);
					
					blockSelectionUpdate = false;
				}
			}
		};
		
		matrixPanel.getTableSelectionModel().addListSelectionListener(selListener);
		matrixPanel.getColumnSelectionModel().addListSelectionListener(selListener);

		refreshColorMatrixWidth();

		/* Configuration panels */

		cellsConfigPage = new CellConfigPage(model);		
		cellsConfigPage.refresh();
		
		rowsConfigPage = new HeaderConfigPage(model, model.getRowDecorator());
		
		columnsConfigPage = new HeaderConfigPage(model, model.getColumnDecorator());
		
		tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setMinimumSize(new Dimension(0, 180));
		tabbedPane.setPreferredSize(new Dimension(0, 200));
		tabbedPane.addTab("Cells", cellsConfigPage);
		tabbedPane.addTab("Rows", rowsConfigPage);
		tabbedPane.addTab("Columns", columnsConfigPage);
		
		/* Split */
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setResizeWeight(1.0);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		splitPane.add(matrixPanel);
		splitPane.add(tabbedPane);
		
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
	}

	private void refreshColorMatrixWidth() {
		/*CellDecorationConfig config = 
			getTable().getCellDecoration(
					getTable().getCurrentProperty());
		
		colorMatrixPanel.setColumnsWidth(
				config.showColors ? 
						defaultColorColumnsWidth 
						: defaultValueColumnsWidth);*/
		
		/*tablePanel.setColumnsWidth(
				configPanel.getCellDecorator()
					.getPreferredWidth());*/
	}

	protected IMatrixView getTable() {
		return model.getMatrixView();
	}
	
	@Deprecated //When getModel return TableViewModel
	public void setTable(IMatrixView tableModel) {
		this.model.setMatrixView(tableModel);
		refresh();
	}

	@Override
	public Object getModel() {
		return model.getMatrixView(); //TODO: return MatrixFigure
		//return model;
	}

	@Override
	public void refresh() {
		matrixPanel.refresh();
	}
	
	@Override
	public void refreshActions() {
		MenuActionSet.editActionSet.setTreeEnabled(true);
		MenuActionSet.tableActionSet.setTreeEnabled(true);
		MenuActionSet.mtcActionSet.setTreeEnabled(true);
		TableActionSet.fastSortRowsAction.setEnabled(true);
		
		FileActionSet.closeAction.setEnabled(true);
		FileActionSet.exportActionSet.setTreeEnabled(true);
		
		MenuActionSet.mtcActionSet.setTreeEnabled(true);
	}
}