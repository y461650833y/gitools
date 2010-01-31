package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.analysis.htest.wizard.EnrichmentAnalysisWizard;

public class NewEnrichmentAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8592231961109105958L;

	public NewEnrichmentAnalysisAction() {
		super("Enrichment analysis ...");

		setDesc("Run an enrichment analysis");
		setMnemonic(KeyEvent.VK_E);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		EnrichmentAnalysisWizard wizard = new EnrichmentAnalysisWizard();
		
		WizardDialog wizDlg = new WizardDialog(
				AppFrame.instance(), wizard);
		
		wizDlg.open();

		//FIXME
		/*ZCalcCommand cmd = new ZCalcCommand();
		//analysisName, testName, samplingNumSamples, 
		//dataFile, valueFilter, groupsFile, minGroupSize, maxModuleSize, 
		//includeNonMappedItems, workdir, outputFormat, resultsByCond
		
		cmd.setTitle(wizard.getAnalysisTitle());
		cmd.setNotes(wizard.getAnalysisNotes());
		cmd.setTestConfig(wizard.getTestConfig());
		cmd.setDataFile(wizard.getDataFile().getAbsolutePath());
		cmd.setDataBinaryCutoffEnabled(wizard.getDataBinaryCutoffEnabled());
		cmd.setDataBinaryCutoff(wizard.getDataBinaryCutoff());
		*/
	}
}
