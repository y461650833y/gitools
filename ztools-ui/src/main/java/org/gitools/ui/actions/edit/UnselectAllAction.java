package org.gitools.ui.actions.edit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.AppFrame;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.BaseAction;

import org.gitools.model.matrix.IMatrixView;

public class UnselectAllAction extends BaseAction {

	private static final long serialVersionUID = 1581417292789818975L;

	public UnselectAllAction() {
		super("Unselect all");
		
		setDesc("Unselect all");
		setSmallIconFromResource(IconNames.unselectAll16);
		setLargeIconFromResource(IconNames.unselectAll24);
		setMnemonic(KeyEvent.VK_U);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		IMatrixView matrixView = getTable();
		
		if (matrixView != null)
			matrixView.clearSelection();
		
		AppFrame.instance()
			.setStatusText("Unselected all.");
	}

}