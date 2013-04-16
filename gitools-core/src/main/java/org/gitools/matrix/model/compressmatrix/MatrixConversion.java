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
package org.gitools.matrix.model.compressmatrix;

import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrix;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.util.HashMap;
import java.util.Map;

public class MatrixConversion extends AbstractCompressor
{

    public CompressMatrix convert(IMatrix inputMatrix, IProgressMonitor progressMonitor) throws Exception
    {

        if (inputMatrix instanceof CompressMatrix)
        {
            return (CompressMatrix) inputMatrix;
        }

        progressMonitor.end();
        initialize(new MatrixReader(inputMatrix, progressMonitor));

        CompressDimension rows = getRows();
        CompressDimension columns = getColumns();
       // IElementAdapter cellAdapter = inputMatrix.getCellAdapter();

        Map<Integer, CompressRow> values = new HashMap<Integer, CompressRow>(inputMatrix.getRows().size());

        progressMonitor.begin("Compressing rows...", rows.size());
        int totalProperties = inputMatrix.getLayers().size();
        for (int r=0; r < rows.size(); r++)
        {
            NotCompressRow notCompressRow = new NotCompressRow( columns );

            int row = inputMatrix.getRows().getIndex( rows.getLabel(r) );
            for (int c=0; c < columns.size(); c++)
            {
                int column = inputMatrix.getColumns().getIndex( columns.getLabel(c) );

                if (!inputMatrix.isEmpty(row, column))
                {
                    StringBuilder line = new StringBuilder(totalProperties*8);
                    line.append( inputMatrix.getColumns().getLabel(column) ).append(SEPARATOR);
                    line.append( inputMatrix.getRows().getLabel(row) ).append(SEPARATOR);
                    for (int p=0; p < totalProperties; p++)
                    {
                         line.append( MatrixUtils.doubleValue( inputMatrix.getCellValue(row, column, p) ) ).append(SEPARATOR);
                    }
                    notCompressRow.append(line.toString());
                }

            }

            values.put( r,  compressRow( notCompressRow ));
            progressMonitor.worked(1);
            if (progressMonitor.isCancelled())
            {
                throw new RuntimeException("Cancelled by the user");
            }
        }

        return new CompressMatrix(
                getRows(),
                getColumns(),
                new CompressElementAdapter(
                        getDictionary(),
                        getHeader(),
                        values,
                        getColumns()
                )
        );
    }

    private static class MatrixReader implements IMatrixReader {

        private int numProperties;
        private int row;
        private int col;

        private IMatrix matrix;
        private IProgressMonitor progressMonitor;

        private MatrixReader(IMatrix matrix, IProgressMonitor progressMonitor)
        {
            progressMonitor.begin("Preparing compression...", matrix.getRows().size());
            this.matrix = matrix;
            this.row = -1;
            this.col = -1;
            this.numProperties = matrix.getLayers().size();
            this.progressMonitor = progressMonitor;
        }

        @Override
        public String[] readNext()
        {
            String[] values = new String[numProperties + 2];

            if (row == matrix.getRows().size() )
            {
                return null;
            }

            if (row == -1)
            {
                for (int i=0; i < numProperties; i++)
                {
                    values[0] = "column";
                    values[1] = "row";
                    values[i + 2] = matrix.getLayers().get(i).getId();
                }
                row++; col++;
            }
            else
            {
                for (int i=0; i < numProperties; i++)
                {
                    values[0] = matrix.getColumns().getLabel(col);
                    values[1] = matrix.getRows().getLabel(row);
                    values[i + 2] = Double.toString( MatrixUtils.doubleValue( matrix.getCellValue(row, col, i)));
                }
                col++;
            }

            if (col == matrix.getColumns().size())
            {
                row++;
                col = 0;
                progressMonitor.worked(1);
                if (progressMonitor.isCancelled())
                {
                    throw new RuntimeException("Cancelled by the user");
                }
            }

            return values;
        }

        @Override
        public void close()
        {
        }
    }
}
