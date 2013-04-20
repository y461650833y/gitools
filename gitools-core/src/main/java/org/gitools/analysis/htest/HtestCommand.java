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
package org.gitools.analysis.htest;

import org.gitools.analysis.AnalysisCommand;
import org.gitools.model.ToolConfig;
import org.gitools.persistence.IResourceFormat;
import org.gitools.stats.test.factory.TestFactory;
import org.jetbrains.annotations.Nullable;

public abstract class HtestCommand extends AnalysisCommand {

    protected HtestAnalysis analysis;

    protected IResourceFormat dataFormat;
    protected String dataPath;
    protected int valueIndex;

    protected String populationPath;
    protected final Double populationDefaultValue;

    protected HtestCommand(HtestAnalysis analysis, IResourceFormat dataFormat, String dataPath, int valueIndex, String populationPath, Double populationDefaultValue, String workdir, String fileName) {

        super(workdir, fileName);

        this.analysis = analysis;
        this.dataFormat = dataFormat;
        this.dataPath = dataPath;
        this.valueIndex = valueIndex;
        this.populationPath = populationPath;
        this.populationDefaultValue = populationDefaultValue;
    }

    public HtestAnalysis getaAnalysis() {
        return analysis;
    }

    public void setAnalysis(HtestAnalysis analysis) {
        this.analysis = analysis;
    }

    public IResourceFormat getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(IResourceFormat dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public int getValueIndex() {
        return valueIndex;
    }

    public void setValueIndex(int valueIndex) {
        this.valueIndex = valueIndex;
    }

    public String getPopulationPath() {
        return populationPath;
    }

    public void setPopulationPath(String populationPath) {
        this.populationPath = populationPath;
    }

    @Nullable
    protected TestFactory createTestFactory(String toolName, String configName) {
        ToolConfig toolConfig = TestFactory.createToolConfig(toolName, configName);

        TestFactory testFactory = TestFactory.createFactory(toolConfig);

        return testFactory;
    }
}
