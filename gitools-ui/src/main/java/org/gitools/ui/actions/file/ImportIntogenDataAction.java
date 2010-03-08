package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import org.gitools.ui.intogen.dialog.IntogenImportDialog;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

public class ImportIntogenDataAction extends BaseAction {

	private static final long serialVersionUID = 668140963768246841L;

	public ImportIntogenDataAction() {
		super("Data ...");
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		/*WizardDialog wizDlg = new WizardDialog(
				AppFrame.instance(),
				new IntogenDataWizard());
		
		wizDlg.open();*/

		IntogenImportDialog dlg = new IntogenImportDialog(AppFrame.instance(),
				IntogenImportDialog.ImportType.ONCODATA);
		
		dlg.setVisible(true);
	}

}