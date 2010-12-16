/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

package org.gitools.ui.platform.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public abstract class AbstractDialog extends JDialog {

	private static final long serialVersionUID = 5886096207448862426L;

	private DialogHeaderPanel hdrPanel;
	
	protected JComponent container;
	
	public AbstractDialog(
			Window owner,
			String title, String header, 
			String message, MessageStatus status,
			Icon logo) {
	
		super(owner, title);
		setModal(true);

		/*if (logo != null)
			setIconImage(IconUtils.iconToImage(logo));*/
		
		createComponents(header, message, status, logo);
		
		setLocationRelativeTo(owner);
		setMinimumSize(new Dimension(300, 260));
	}
	
	public AbstractDialog(Window owner, String title, Icon icon) {
		this(owner, title, "", "", MessageStatus.INFO, icon);
	}

	public void open() {
		setVisible(true);
	}
	
	protected JComponent getContainer() {
		return container;
	}
	
	protected void setContainer(JComponent container) {
		this.container = container;
	}
	
	protected void createComponents(
			String header, 
			String message, 
			MessageStatus status,
			Icon logo) {
		
		hdrPanel = new DialogHeaderPanel();
		hdrPanel.setTitle(header);
		hdrPanel.setMessage(message);
		hdrPanel.setMessageStatus(status);
		hdrPanel.setRightLogo(logo);
		
		JPanel hp = new JPanel();
		hp.setLayout(new BorderLayout());
		hp.add(hdrPanel, BorderLayout.CENTER);
		hp.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);
		
		container = createContainer();
		
		final DialogButtonsPanel buttonsPanel = 
			new DialogButtonsPanel(
					createButtons());
		
		JPanel bp = new JPanel();
		bp.setLayout(new BorderLayout());
		bp.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.NORTH);
		bp.add(buttonsPanel, BorderLayout.CENTER);
		
		setLayout(new BorderLayout());
		add(hp, BorderLayout.NORTH);
		if (container != null)
			add(container, BorderLayout.CENTER);
		add(bp, BorderLayout.SOUTH);
	}

	public DialogHeaderPanel getHeaderPanel() {
		return hdrPanel;
	}
	
	protected abstract JComponent createContainer();
	
	protected abstract List<JButton> createButtons();
}