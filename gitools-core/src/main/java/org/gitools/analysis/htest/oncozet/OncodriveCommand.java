package org.gitools.analysis.htest.oncozet;

import java.io.File;

import org.gitools.datafilters.ValueTranslator;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.PersistenceException;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.List;
import java.util.Properties;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.htest.HtestCommand;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.text.MatrixTextPersistence;
import org.gitools.persistence.text.ModuleMapPersistence;
import org.gitools.persistence.xml.OncodriveAnalysisXmlPersistence;

public class OncodriveCommand extends HtestCommand {

	protected String modulesMime;
	protected String modulesPath;

	public OncodriveCommand(
			OncodriveAnalysis analysis,
			String dataMime,
			String dataPath,
			String populationPath,
			String modulesMime,
			String modulesFile,
			String workdir,
			String fileName) {
		
		super(analysis, dataMime, dataPath,
				populationPath, workdir, fileName);

		this.modulesMime = modulesMime;
		this.modulesPath = modulesFile;
	}

	@Override
	public void run(IProgressMonitor monitor) throws AnalysisException {

		try {
			final OncodriveAnalysis oncozAnalysis = (OncodriveAnalysis) analysis;

			// Load data and modules

			monitor.begin("Loading ...", 1);
			monitor.info("Data: " + dataPath);
			monitor.info("Columns: " + modulesPath);

			loadDataAndModules(
					dataMime, dataPath,
					populationPath,
					modulesMime, modulesPath,
					oncozAnalysis,
					monitor.subtask());

			monitor.end();

			OncodriveProcessor processor = new OncodriveProcessor(oncozAnalysis);

			processor.run(monitor);

			// Save analysis

			save(oncozAnalysis, monitor);
		}
		catch (Exception ex) {
			throw new AnalysisException(ex);
		}
	}

	private void loadDataAndModules(
			String dataFileMime,
			String dataFileName,
			String populationFileName,
			String modulesFileMime,
			String modulesFileName,
			OncodriveAnalysis analysis,
			IProgressMonitor monitor) throws PersistenceException {

		// Load background population

		String[] populationLabels = null;

		if (populationFileName != null) {
			File bgFile = new File(populationFileName);

			List<String> popLabels = (List<String>) PersistenceManager.getDefault()
					.load(bgFile, MimeTypes.GENE_SET, monitor);

			populationLabels = popLabels.toArray(new String[popLabels.size()]);
		}

		// Load data

		File dataFile = new File(dataFileName);

		ValueTranslator valueTranslator = createValueTranslator(analysis);

		Properties dataProps = new Properties();
		dataProps.put(MatrixTextPersistence.BINARY_VALUES, analysis.isBinaryCutoffEnabled());
		dataProps.put(MatrixTextPersistence.VALUE_TRANSLATOR, valueTranslator);
		if (populationLabels != null)
			dataProps.put(MatrixTextPersistence.POPULATION_LABELS, populationLabels);

		BaseMatrix dataMatrix = loadDataMatrix(dataFile, dataFileMime, dataProps, monitor);

		PersistenceManager.getDefault().clearEntityCache(dataMatrix);

		analysis.setData(dataMatrix);

		// Load modules

		if (modulesFileName != null) {
			File file = new File(modulesFileName);

			Properties modProps = new Properties();
			modProps.put(ModuleMapPersistence.ITEM_NAMES_FILTER_ENABLED, true);
			modProps.put(ModuleMapPersistence.ITEM_NAMES, dataMatrix.getColumnStrings());
			modProps.put(ModuleMapPersistence.MIN_SIZE, analysis.getMinModuleSize());
			modProps.put(ModuleMapPersistence.MAX_SIZE, analysis.getMaxModuleSize());

			ModuleMap moduleMap = loadModuleMap(file, modulesFileMime, modProps, monitor);

			PersistenceManager.getDefault().clearEntityCache(moduleMap);
			
			analysis.setModuleMap(moduleMap);
		}
	}
	
	private void save(final OncodriveAnalysis analysis, IProgressMonitor monitor) throws PersistenceException {

		File workdirFile = new File(workdir);
		if (!workdirFile.exists())
			workdirFile.mkdirs();

		File file = new File(workdirFile, fileName);
		OncodriveAnalysisXmlPersistence p = new OncodriveAnalysisXmlPersistence();
		p.setRecursivePersistence(true);
		p.write(file, analysis, monitor);
	}
}