/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.app.dialog;

import org.apache.velocity.VelocityContext;
import org.gitools.ui.core.Application;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.os.OSProperties;
import org.gitools.ui.platform.panel.TemplatePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ShortcutsDialog extends JDialog {

    private static final long serialVersionUID = -5869809986725283792L;

    private final String appName;
    private final String appVersion;
    final OSProperties osProperties = OSProperties.get();


    public ShortcutsDialog(JFrame owner) {
        super(owner);

        appName = Application.getAppName();
        appVersion = Application.getGitoolsVersion().toString();

        setModal(true);
        setTitle(appName + " shortcuts");

        createComponents();

        pack();
    }

    private void createComponents() {
        JLabel imageLabel = new JLabel(IconUtils.getIconResource(IconNames.aboutLogo));
        imageLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 0));
        imageLabel.setVerticalAlignment(SwingConstants.TOP);

        TemplatePanel creditsPane = new TemplatePanel();
        creditsPane.setFocusable(false);
        Dimension dim = new Dimension(600, 500);
        creditsPane.setPreferredSize(dim);
        creditsPane.setMaximumSize(dim);
        try {
            creditsPane.setTemplateFromResource("/vm/shortcuts.vm");
            VelocityContext context = new VelocityContext();
            context.put("appName", appName);
            context.put("appVersion", appVersion);
            context.put("ctrlKey", osProperties.getCtrlKey());
            context.put("altKey", osProperties.getAltKey());
            context.put("metaKey", osProperties.getMetaKey());
            context.put("shiftKey", osProperties.getShiftKey());
            creditsPane.setContext(context);
            creditsPane.render();
        } catch (Exception ex) {
            System.err.println("Unexpected error creating credits pane.");
        }

        JButton acceptBtn = new JButton("Close");
        acceptBtn.setMargin(new Insets(0, 30, 0, 30));
        acceptBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        btnPanel.setLayout(new BorderLayout());
        btnPanel.add(acceptBtn, BorderLayout.EAST);

        JPanel contPanel = new JPanel();
        contPanel.setLayout(new BorderLayout());
        contPanel.add(creditsPane, BorderLayout.CENTER);
        contPanel.add(btnPanel, BorderLayout.SOUTH);
        contPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        setLayout(new BorderLayout());
        add(imageLabel, BorderLayout.WEST);
        add(contPanel, BorderLayout.CENTER);
    }

    void closeDialog() {
        setVisible(false);
    }
}
