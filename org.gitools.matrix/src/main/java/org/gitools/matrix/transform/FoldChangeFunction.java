package org.gitools.matrix.transform;

import org.gitools.api.ApplicationContext;
import org.gitools.api.analysis.IAggregator;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.*;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.matrix.model.MatrixLayers;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.matrix.model.iterable.PositionMapping;
import org.gitools.matrix.sort.AggregationFunction;
import org.gitools.matrix.transform.parameters.AggregatorParameter;
import org.gitools.matrix.transform.parameters.DimensionParameter;
import org.gitools.utils.aggregation.MeanAggregator;
import org.gitools.utils.aggregation.MedianAggregator;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;


public class FoldChangeFunction extends ConfigurableTransformFunction {

    public static final String AGGREGATION_PARAM = "Aggregation";
    AggregatorParameter aggregatorParameter;

    public static final String DIMENSION_PARAM = "Dimension";
    DimensionParameter dimensionParameter;

    private IMatrixLayer aggLayer;
    private final static Key<HashMatrix> CACHEKEY = new Key<HashMatrix>() {};
    private IMatrix matrix;
    private IMatrixLayer newLayer;
    private IProgressMonitor monitor;
    private HashMatrix data;


    public FoldChangeFunction(IMatrix matrix, IMatrixLayer newLayer) {
        super("Fold-Change");
        this.matrix = matrix;
        this.newLayer = newLayer;
    }


    @Override
    public Double apply(Double value, IMatrixPosition position) {
        if(value != null) {
            Double mean = getMedian(position.get(MatrixDimensionKey.ROWS));
            if (mean == null) {
                return null;
            }
            return value - mean;
        }
        return null;
    }


    @Override
    public void onBeforeIterate(IMatrixIterable<Double> parentIterable) {
        super.onBeforeIterate(parentIterable);
        monitor = ApplicationContext.getProgressMonitor().subtask();

        IMatrixDimension transformDimension = matrix.getDimension(dimensionParameter.getParameterValue());
        IMatrixDimension aggDimension = matrix.getDimension(COLUMNS == dimensionParameter.getParameterValue() ? ROWS : COLUMNS);
        IAggregator aggregator = aggregatorParameter.getParameterValue();

        AggregationFunction aggregationFunction = new AggregationFunction(newLayer, aggregator, aggDimension);

        IMatrixLayer aggLayer = new MatrixLayer("agg", Double.class);
        HashMatrix aggregationMatrix = new HashMatrix(
                new MatrixLayers(aggLayer),
                new HashMatrixDimension(ROWS, matrix.getRows())
        );

        matrix.newPosition()
                .iterate(transformDimension)
                .monitor(monitor.subtask(), "Preparing for '" + this.name + "' transformation")
                .transform(aggregationFunction)
                .store(
                        aggregationMatrix,
                        new PositionMapping().map(matrix.getRows(), ROWS),
                        aggLayer
                );
        monitor.end();
        newLayer.setCache(CACHEKEY, aggregationMatrix);
    }

    private Double getMedian(String identifier) {

        if (data == null) {
            data = (HashMatrix) newLayer.getCache(CACHEKEY);
            aggLayer = data.getLayers().get(0);
        }

        return (Double) data.get(aggLayer, identifier);
    }


    @Override
    public FoldChangeFunction createNew() {
        return new FoldChangeFunction(matrix, newLayer);
    }

    @Override
    protected void createDefaultParameters() {
        dimensionParameter = new DimensionParameter();
        dimensionParameter.setDescription("Select if the fold change should be relative to the rows or columns dimension");
        dimensionParameter.setChoices(MatrixDimensionKey.values());
        addParameter(DIMENSION_PARAM, dimensionParameter);


        aggregatorParameter = new AggregatorParameter();
        aggregatorParameter.setDescription("Select if the fold change should be calculated with the row/column median or mean");
        IAggregator[] iAggregators = {
                MedianAggregator.INSTANCE,
                MeanAggregator.INSTANCE
        };
        aggregatorParameter.setChoices(iAggregators);
        addParameter(AGGREGATION_PARAM, aggregatorParameter);
    }

    public String getName() {
        return aggregatorParameter.getParameterValue().toString() + " " + dimensionParameter.getParameterValue().getLabel() + " fold-change";
    }

    public String getDescription() {
        return "Fold change calculated relative to the  " + aggregatorParameter.getParameterValue().toString() + " of each " + dimensionParameter.getParameterValue().getLabel();
    }

}
