package es.imim.bg.ztools.zcalc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.command.AnalysisCommand;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Data;
import es.imim.bg.ztools.model.Modules;
import es.imim.bg.ztools.resources.DataFile;
import es.imim.bg.ztools.resources.ModulesFile;
import es.imim.bg.ztools.resources.analysis.REXmlAnalysisResource;
import es.imim.bg.ztools.resources.analysis.TabAnalysisResource;
import es.imim.bg.ztools.test.factory.TestFactory;

public class ZCalcCommand extends AnalysisCommand {
	
	public ZCalcCommand(String analysisName, String testName,
			int samplingNumSamples, String dataFile, String groupsFile,
			int minGroupSize, int maxModuleSize, String workdir,
			String outputFormat, boolean resultsByCond) {
		
		super(analysisName, testName, samplingNumSamples, dataFile, groupsFile,
				minGroupSize, maxModuleSize, workdir, outputFormat, resultsByCond);
	}
	
	public void run(ProgressMonitor monitor) 
			throws IOException, DataFormatException, InterruptedException {
		
		// Prepare output
		
		//AnalysisResource output = createOutput(outputFormat);
		
		// Prepare test factory
		
		TestFactory testFactory = createTestFactory(testName);
		
		// Load data and modules
		
		monitor.begin("Loading ...", 1);
		monitor.info("Data: " + dataFile);
		monitor.info("Modules: " + modulesFile);
		
		Data data = new Data();
		Modules modules = new Modules();
		loadDataAndModules(
				data, modules, 
				dataFile, modulesFile, 
				minModuleSize, maxModuleSize,
				monitor.subtask());
		
		monitor.end();
		
		// Create and process analysis
		
		Analysis analysis = new Analysis();
		analysis.setName(analysisName);
		analysis.setTestFactory(testFactory);
		analysis.setData(data);
		analysis.setModules(modules);
		
		ZCalcProcessor processor = 
			new ZCalcProcessor(analysis);
		
		processor.run(monitor);
		
		// Save analysis
		
		monitor.begin("Saving analysis ...", 1);
		monitor.info("Location: " + workdir + File.separator + analysisName);
	
		new TabAnalysisResource(workdir, resultsByCond, defaultSep, defaultQuote)
			.save(analysis);
		
		if (outputFormat.equalsIgnoreCase("rexml"))
			new REXmlAnalysisResource(workdir, minModuleSize, maxModuleSize)
				.save(analysis);
		
		monitor.end();
	}

	private void loadDataAndModules(
			Data data, Modules modules,
			String dataFileName, String modulesFileName, 
			int minModuleSize, int maxModuleSize, 
			ProgressMonitor monitor) throws FileNotFoundException, IOException, DataFormatException {
		
		// Load metadata
		
		DataFile dataFile = new DataFile(dataFileName);
		dataFile.loadMetadata(data, monitor);
		
		// Load modules
		
		ModulesFile modulesFile = new ModulesFile(modulesFileName);
		modulesFile.load(
			modules,
			minModuleSize,
			maxModuleSize,
			data.getRowNames(),
			monitor);
		
		// Load data
		
		dataFile.loadData(
				data,
				null, 
				modules.getItemsOrder(), 
				monitor);
		
	}

	/*private AnalysisResource createOutput(String outputFormat) {
		AnalysisResource output = null;
		
		if (outputFormat.equalsIgnoreCase("csv"))
			output = new TabAnalysisResource(workdir, resultsByCond, defaultSep, defaultQuote);
		else if (outputFormat.equalsIgnoreCase("rexml"))
			output = new REXmlAnalysisResource(workdir, minModuleSize, maxModuleSize);
		else
			throw new IllegalArgumentException("Unknown output format '" + outputFormat + "'");
		
		return output;
	}*/
}
