/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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

package org.gitools.ui.heatmap.header.datalabels;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import edu.upf.bg.aggregation.IAggregator;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapDataHeatmapHeader;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;

public class DataLabelsHeaderWizard extends AbstractWizard {

    private Heatmap heatmap;
    private Heatmap aggregatedValueHeatmap;
    private boolean applyToRows;
    private boolean editionMode;
    private HeatmapDataHeatmapHeader header;


    private LabelDataSourcePage dataSourcePage;
    private ColorScalePage colorScalePage;

    public DataLabelsHeaderWizard(Heatmap heatmap, HeatmapDataHeatmapHeader header, boolean applyToRows) {
        super();

        this.heatmap = heatmap;
        this.applyToRows = applyToRows;

        this.header = header;

    }

    @Override
    public void addPages() {
        if (!editionMode) {
            dataSourcePage = new LabelDataSourcePage(heatmap, applyToRows);
            addPage(dataSourcePage);
        }

        colorScalePage = new ColorScalePage();
        addPage(colorScalePage);

    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        IWizardPage nextPage;
        if (page == this.dataSourcePage) {

            aggregatedValueHeatmap = aggregateToHeatmap();
            colorScalePage.setHeatmap(aggregatedValueHeatmap);
            nextPage = colorScalePage;

        } else {
            nextPage = super.getNextPage(page);
        }
        return nextPage;
    }

    private Heatmap aggregateToHeatmap() {


        int elementsToAggregate;
        String[] columnNames;
        String[] rowNames;
        IAggregator aggregator = dataSourcePage.getDataAggregator();
        boolean useAll = dataSourcePage.useAllColumnsOrRows();
        int valueIndex = dataSourcePage.getSelectedDataValueIndex();
        DoubleMatrix2D valueMatrix;


        if (applyToRows) {

            int[] columns = useAll ? heatmap.getMatrixView().getVisibleColumns() :
                    heatmap.getMatrixView().getSelectedColumns();
            int[] rows = heatmap.getMatrixView().getVisibleRows();


            elementsToAggregate = columns.length;
            valueMatrix = DoubleFactory2D.dense.make(rows.length, 1, 0.0);
            final double[] valueBuffer = new double[elementsToAggregate];

            for (int i = 0; i < rows.length; i++) {
                double aggregatedValue = aggregateValue(heatmap.getMatrixView(),columns,i,valueIndex,aggregator,valueBuffer);
                valueMatrix.set(i,0,aggregatedValue);
            }

            rowNames = new String[rows.length];
            for (int i = 0; i < rows.length; i++)
                rowNames[i] = heatmap.getMatrixView().getRowLabel(i);


            columnNames = new String[1];
            columnNames[0] = dataSourcePage.getDataAggregator().toString();


        } else {

            int[] rows = useAll ? heatmap.getMatrixView().getVisibleRows() :
                    heatmap.getMatrixView().getSelectedRows();
            int[] columns = heatmap.getMatrixView().getVisibleColumns();


            elementsToAggregate = rows.length;
            valueMatrix = DoubleFactory2D.dense.make(1, columns.length, 0.0);
            final double[] valueBuffer = new double[elementsToAggregate];

            for (int i = 0; i < columns.length; i++) {
                double aggregatedValue = aggregateValue(heatmap.getMatrixView(),rows,i,valueIndex,aggregator,valueBuffer);
                valueMatrix.set(0,i,aggregatedValue);
            }

            columnNames = new String[columns.length];
            for (int i = 0; i < columnNames.length; i++)
                columnNames[i] = heatmap.getMatrixView().getColumnLabel(i);

            rowNames = new String[1];
            rowNames[0] = dataSourcePage.getDataAggregator().toString();
        }

        return new Heatmap(
                new MatrixView(
                        new DoubleMatrix(
                                "Data Annotation",
                                columnNames,
                                rowNames,
                                valueMatrix)
                ));
    }

    @Override
    public boolean canFinish() {
        return currentPage != dataSourcePage;
    }

    @Override
    public void pageLeft(IWizardPage currentPage) {
        super.pageLeft(currentPage);

        if (currentPage != dataSourcePage || editionMode)
            return;

    }

   @Override
    public void performFinish() {
       StringBuilder sb = new StringBuilder("");
       sb.append(dataSourcePage.getDataAggregator().toString());
       sb.append(" of ");
       sb.append(dataSourcePage.getSelectedDataValueName());

       aggregatedValueHeatmap.setTitle(sb.toString());
       header.setHeaderHeatmap(aggregatedValueHeatmap);
    }


    public HeatmapDataHeatmapHeader getHeader() {
        return header;
    }

    public void setEditionMode(boolean editionMode) {
        this.editionMode = editionMode;
    }


    private double aggregateValue(
            IMatrixView matrixView,
            int[] selectedIndices,
            int idx,
            int valueIndex,
            IAggregator aggregator,
            double[] valueBuffer) {

        for (int i = 0; i < selectedIndices.length; i++) {
            int selected = selectedIndices[i];

            Object valueObject;
            if (applyToRows)
                valueObject = matrixView.getCellValue(idx, selected, valueIndex);
            else
                valueObject = matrixView.getCellValue(selected, idx, valueIndex);
            valueBuffer[i] = MatrixUtils.doubleValue(valueObject);
        }

        return aggregator.aggregate(valueBuffer);
    }


}
