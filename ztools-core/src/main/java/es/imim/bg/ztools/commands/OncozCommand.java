package es.imim.bg.ztools.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.datafilters.ValueFilter;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.DataMatrix;
import es.imim.bg.ztools.model.ModuleMap;
import es.imim.bg.ztools.model.ToolConfig;
import es.imim.bg.ztools.processors.OncozProcessor;
import es.imim.bg.ztools.resources.DataResource;
import es.imim.bg.ztools.resources.ModuleMapResource;
import es.imim.bg.ztools.test.factory.TestFactory;

public class OncozCommand extends AnalysisCommand {
	
	public OncozCommand(String analysisName, String testName,
			int samplingNumSamples, String dataFile, ValueFilter valueFilter, 
			String groupsFile, int minModuleSize, int maxModuleSize, String workdir,
			String outputFormat, boolean resultsByCond) {
		
		super(analysisName, testName, samplingNumSamples, dataFile, valueFilter, 
				groupsFile, minModuleSize, maxModuleSize, workdir, outputFormat, resultsByCond);
	}

	@Override
	public void run(ProgressMonitor monitor) 
			throws IOException, DataFormatException, InterruptedException {
		
		// Prepare test factory
		
		TestFactory testFactory = 
			createTestFactory(ToolConfig.ONCOZET, testName);
		
		// Load data and modules
		
		monitor.begin("Loading ...", 1);
		monitor.info("Data: " + dataFile);
		monitor.info("Modules: " + modulesFile);
		
		DataMatrix dataMatrix = new DataMatrix();
		ModuleMap moduleMap = new ModuleMap();
		loadDataAndModules(
				dataMatrix, moduleMap, 
				dataFile, modulesFile, 
				minModuleSize, maxModuleSize,
				monitor.subtask());
		
		monitor.end();
		
		// Create and process analysis
		
		Analysis analysis = new Analysis();
		analysis.setName(analysisName);
		analysis.setToolConfig(testFactory.getTestConfig());
		analysis.setDataMatrix(dataMatrix);
		analysis.setModuleSet(moduleMap);
		
		OncozProcessor processor = 
			new OncozProcessor(analysis);
		
		processor.run(monitor);
		
		// Save analysis
		
		save(analysis, monitor);
	}

	private void loadDataAndModules(
			DataMatrix dataMatrix, ModuleMap moduleMap,
			String dataFileName, String modulesFileName, 
			int minModuleSize, int maxModuleSize, 
			ProgressMonitor monitor) throws FileNotFoundException, IOException, DataFormatException {
		
		// Load metadata
		
		DataResource dataResource = new DataResource(dataFileName);
		dataResource.loadMetadata(dataMatrix, monitor);
		
		// Load modules
		
		if (modulesFileName != null) {
			File file = new File(modulesFileName);
			moduleMap.setName(file.getName());
			
			ModuleMapResource moduleMapResource = new ModuleMapResource(file);
			moduleMapResource.load(
				moduleMap,
				minModuleSize,
				maxModuleSize,
				dataMatrix.getColNames(),
				monitor);
		}
		else {
			moduleMap.setItemNames(dataMatrix.getColNames());
			moduleMap.setModuleNames(new String[] {"all"});
			int num = dataMatrix.getColNames().length;
			int[][] indices = new int[1][num];
			for (int i = 0; i < num; i++)
				indices[0][i] = i;
			moduleMap.setItemIndices(indices);
		}
		
		// Load data
		
		dataResource.loadData(
				dataMatrix,
				moduleMap.getItemsOrder(),
				null,
				monitor);		
	}
}