package org.gitools.matrix.transform;

import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayer;

import java.util.ArrayList;
import java.util.List;

public class TransformFunctionFactory {

    public static List<ConfigurableTransformFunction> get(IMatrix matrix, IMatrixLayer<Double> resultLayer) {
        List<ConfigurableTransformFunction> funcs = new ArrayList<>();
        funcs.add(new FillEmptyFunction());
        funcs.add(new FoldChangeFunction(matrix, resultLayer));
        funcs.add(new LogNFunction());
        funcs.add(new Log10Function());
        funcs.add(new ReplaceValueFunction());
        funcs.add(new SumConstantFunction());
        return funcs;
    }

    public static List<ConfigurableTransformFunction> get() {
        List<ConfigurableTransformFunction> funcs = new ArrayList<>();
        funcs.add(new FillEmptyFunction());
        funcs.add(new LogNFunction());
        funcs.add(new Log10Function());
        funcs.add(new ReplaceValueFunction());
        funcs.add(new SumConstantFunction());
        return funcs;
    }

    public static <T extends ConfigurableTransformFunction> T createFromTemplate(T template) {
        T newInstance = template.createNew();
        copyParameters(template, newInstance);
        return newInstance;
    }

    protected static <T extends ConfigurableTransformFunction> void copyParameters(T from, T to) {
        for (String key : from.getParameters().keySet()) {
            to.getParameter(key).setParameterValue(from.getParameter(key).getParameterValue());
        }
    }
}