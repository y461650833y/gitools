package org.gitools.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.DataFormatException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.gitools.model.matrix.ObjectMatrix;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.matrix.element.IElementFactory;
import org.gitools.model.matrix.element.IElementProperty;
import org.gitools.model.matrix.element.array.ArrayElementAdapter;
import org.gitools.model.matrix.element.array.ArrayElementFactory;
import org.gitools.model.matrix.element.basic.StringElementAdapter;
import org.gitools.model.matrix.element.bean.BeanElementAdapter;
import org.gitools.model.matrix.element.bean.BeanElementFactory;
import org.gitools.stats.test.results.BinomialResult;
import org.gitools.stats.test.results.CombinationResult;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.stats.test.results.FisherResult;
import org.gitools.stats.test.results.ZScoreResult;

import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;

import edu.upf.bg.csv.RawCsvWriter;
import edu.upf.bg.progressmonitor.ProgressMonitor;

public class ResultsResource extends Resource {

	private static final long serialVersionUID = 3487889255829878181L;

	private static final CSVStrategy csvStrategy = defaultCsvStrategy;

	/* This information will be used to infer the element class
	 * to use when loading an old tabulated file 
	 * using only its headers */
	private static Map<String, Class<?>> elementClasses = new HashMap<String, Class<?>>();
	static {
		Class<?>[] classes = new Class<?>[] {
			ZScoreResult.class,
			BinomialResult.class,
			FisherResult.class,
			CombinationResult.class,
			CommonResult.class
		};
		
		for (Class<?> elementClass : classes) {
			IElementAdapter adapter = 
				new BeanElementAdapter(elementClass);

			elementClasses.put(
					getElementClassId(adapter.getProperties()), 
					elementClass);
		}
	}
	
	private static String getElementClassId(List<IElementProperty> properties) {
		String[] ids = new String[properties.size()];
		for (int i = 0; i < properties.size(); i++)
			ids[i] = properties.get(i).getId();
		
		return getElementClassId(ids);
	}
	
	private static String getElementClassId(String[] ids) {
		Arrays.sort(ids);
		
		StringBuilder sb = new StringBuilder();
		for (String id : ids)
			sb.append(':').append(id);
		return sb.toString();
	}
	
	public ResultsResource() {
		super((String)null); //FIXME
	}

	public ResultsResource(String fileName) {
		super(fileName);
	}
	
	public ResultsResource(File file) {
		super(file);
	}

	public ObjectMatrix read(ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, DataFormatException {
		
		ObjectMatrix resultsMatrix = new ObjectMatrix();
		read(resultsMatrix, monitor);
		return resultsMatrix;
	}
	
	public void read(ObjectMatrix resultsMatrix, ProgressMonitor monitor) 
			throws FileNotFoundException, IOException, DataFormatException {
		
		read(openReader(), resultsMatrix, monitor);
	}
	
	protected void read(Reader reader, ObjectMatrix resultsMatrix, ProgressMonitor monitor) 
			throws IOException, DataFormatException {
		
		monitor.begin("Reading results ...", 1);
		
		CSVParser parser = new CSVParser(reader, csvStrategy);
		
		String[] line = parser.getLine();
		
		// read header
		if (line.length < 3)
			throw new DataFormatException("Almost 3 columns expected.");
		
		int numParams = line.length - 2;
		String[] paramNames = new String[numParams];
		System.arraycopy(line, 2, paramNames, 0, line.length - 2);
		
		String[] ids = new String[numParams];
		System.arraycopy(line, 2, ids, 0, line.length - 2);
		
		// infer element class and create corresponding adapter and factory
		Class<?> elementClass = elementClasses.get(
				getElementClassId(ids));
		
		IElementAdapter elementAdapter = null;
		IElementFactory elementFactory = null;
		if (elementClass == null) {
			elementAdapter = new ArrayElementAdapter(paramNames);
			elementFactory = new ArrayElementFactory(paramNames.length);
		}
		else {
			elementAdapter = new BeanElementAdapter(elementClass);
			elementFactory = new BeanElementFactory(elementClass);
		}
		
		// read body
		Map<String, Integer> columnMap = new HashMap<String, Integer>();
		Map<String, Integer> rowMap = new HashMap<String, Integer>();
		List<Object[]> list = new ArrayList<Object[]>();
		
		while ((line = parser.getLine()) != null) {
			final String columnName = line[0];
			final String rowName = line[1];
			
			Integer columnIndex = columnMap.get(columnName);
			if (columnIndex == null) {
				columnIndex = columnMap.size();
				columnMap.put(columnName, columnIndex);
			}
			
			Integer rowIndex = rowMap.get(rowName);
			if (rowIndex == null) {
				rowIndex = rowMap.size();
				rowMap.put(rowName, rowIndex);
			}
			
			Object element = elementFactory.create();

			for (int i = 2; i < line.length; i++) {
				final int pix = elementAdapter
					.getPropertyIndex(paramNames[i - 2]);
				
				Object value = parsePropertyValue(
						elementAdapter.getProperty(pix), line[i]);

				elementAdapter.setValue(element, pix, value);
			}
			
			list.add(new Object[] {
					new int[] { columnIndex, rowIndex }, element });
		}
		
		int numColumns = columnMap.size();
		int numRows = rowMap.size();
		
		ObjectMatrix1D columns = ObjectFactory1D.dense.make(numColumns);
		for (Entry<String, Integer> entry : columnMap.entrySet())
			columns.setQuick(entry.getValue(), entry.getKey());
		
		ObjectMatrix1D rows = ObjectFactory1D.dense.make(numRows);
		for (Entry<String, Integer> entry : rowMap.entrySet())
			rows.setQuick(entry.getValue(), entry.getKey());
		
		resultsMatrix.setColumns(columns);
		resultsMatrix.setRows(rows);
		resultsMatrix.makeData();
		
		resultsMatrix.setColumnAdapter(new StringElementAdapter());
		resultsMatrix.setRowAdapter(new StringElementAdapter());
		resultsMatrix.setCellAdapter(elementAdapter);
		
		for (Object[] result : list) {
			int[] coord = (int[]) result[0];
			final int columnIndex = coord[0];
			final int rowIndex = coord[1];
			
			Object element = result[1];			
			resultsMatrix.setCell(rowIndex, columnIndex, element);
		}
		
		monitor.end();
	}
	
	//FIXME: We need a ValueParserFactory
	private Object parsePropertyValue(IElementProperty property, String string) {
		
		final Class<?> propertyClass = property.getValueClass();
		
		Object value = null;
		try {
			if (propertyClass.equals(double.class)
					|| propertyClass.equals(Double.class))
				value = Double.parseDouble(string);
			else if (propertyClass.equals(float.class)
					|| propertyClass.equals(Float.class))
				value = Double.parseDouble(string);
			else if (propertyClass.equals(int.class)
					|| propertyClass.equals(Integer.class))
				value = Integer.parseInt(string);
			else if (propertyClass.equals(long.class)
					|| propertyClass.equals(Long.class))
				value = Long.parseLong(string);
			else if (propertyClass.isEnum()) {
				Object[] cts = propertyClass.getEnumConstants();
				for (Object o : cts)
					if (o.toString().equals(string))
						value = o;
			}
			else
				value = string;
		}
		catch (Exception e) {
			if (propertyClass.equals(double.class)
					|| propertyClass.equals(Double.class))
				value = Double.NaN;
			else if (propertyClass.equals(float.class)
					|| propertyClass.equals(Float.class))
				value = Float.NaN;
			else if (propertyClass.equals(int.class)
					|| propertyClass.equals(Integer.class))
				value = new Integer(0);
			else if (propertyClass.equals(long.class)
					|| propertyClass.equals(Long.class))
				value = new Long(0);
			else if (propertyClass.isEnum())
				value = string;
		}
		return value;
	}

	public void write(ObjectMatrix results, String prefix, ProgressMonitor monitor) 
			throws FileNotFoundException, IOException {
		
		write(results, prefix, true, monitor);
	}
	
	public void write(ObjectMatrix results, String prefix, boolean orderByColumn, ProgressMonitor monitor) 
			throws FileNotFoundException, IOException {
		
		final File basePath = getResourceFile();
		
		/*String colsPath = new File(basePath, prefix + ".columns.tsv.gz").getAbsolutePath();
		writeColumns(openWriter(colsPath), results, orderByColumn, monitor);*/
		
		/*String rowsPath = new File(basePath, prefix + ".rows.tsv.gz").getAbsolutePath();
		writeRows(openWriter(rowsPath), results, orderByColumn, monitor);*/
		
		String cellsPath = new File(basePath, prefix + ".cells.tsv.gz").getAbsolutePath();
		writeCells(openWriter(cellsPath), results, orderByColumn, monitor);
	}
	
	public void writeRows(Writer writer, ObjectMatrix resultsMatrix, boolean orderByColumn, ProgressMonitor monitor) {
		
	}
	
	public void writeColumns(Writer writer, ObjectMatrix resultsMatrix, boolean orderByColumn, ProgressMonitor monitor) {
		
	}

	public void writeCells(Writer writer, ObjectMatrix resultsMatrix, boolean orderByColumn, ProgressMonitor monitor) {
		
		RawCsvWriter out = new RawCsvWriter(writer, 
				csvStrategy.getDelimiter(), csvStrategy.getEncapsulator());
		
		out.writeQuotedValue("column");
		out.writeSeparator();
		out.writeQuotedValue("row");
		
		for (IElementProperty prop : resultsMatrix.getCellAdapter().getProperties()) {
			out.writeSeparator();
			out.writeQuotedValue(prop.getId());
		}
		
		out.writeNewLine();

		int numColumns = resultsMatrix.getColumnCount();
		int numRows = resultsMatrix.getRowCount();
		
		if (orderByColumn) {
			for (int colIndex = 0; colIndex < numColumns; colIndex++)
				for (int rowIndex = 0; rowIndex < numRows; rowIndex++)
					writeLine(out, resultsMatrix, colIndex, rowIndex);
		}
		else {
			for (int rowIndex = 0; rowIndex < numRows; rowIndex++)
				for (int colIndex = 0; colIndex < numColumns; colIndex++)
					writeLine(out, resultsMatrix, colIndex, rowIndex);
		}
		
		out.close();
	}

	private void writeLine(
			RawCsvWriter out, 
			ObjectMatrix resultsMatrix,
			int colIndex, int rowIndex) {
		
		final String colName = resultsMatrix.getColumn(colIndex).toString();
		final String rowName = resultsMatrix.getRow(rowIndex).toString();
		
		out.writeQuotedValue(colName);
		out.writeSeparator();
		out.writeQuotedValue(rowName);
		
		Object element = resultsMatrix.getCell(rowIndex, colIndex);
		
		IElementAdapter cellsFacade = resultsMatrix.getCellAdapter();
		
		int numProperties = cellsFacade.getPropertyCount();
		
		for (int propIndex = 0; propIndex < numProperties; propIndex++) {
			out.writeSeparator();
			
			Object value = cellsFacade.getValue(element, propIndex);
			if (value instanceof Double) {
				Double v = (Double) value;
				if (Double.isNaN(v))
					out.writeValue("-");
				else
					out.writeValue(v.toString());
			}
			else
				out.writeQuotedValue(value.toString());
		}
		
		out.writeNewLine();
	}
}