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
package org.gitools.ui.app.welcome;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.persistence.FileFormat;
import org.gitools.heatmap.format.HeatmapFormat;
import org.gitools.ui.app.actions.file.OpenFromFilesystemBrowseAction;
import org.gitools.ui.app.actions.file.OpenFromGenomeSpaceAction;
import org.gitools.ui.app.actions.help.ShortcutsAction;
import org.gitools.ui.app.commands.CommandLoadFile;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.components.editor.HtmlEditor;
import org.gitools.ui.core.components.wizard.SaveFileWizard;
import org.gitools.ui.core.utils.FileChoose;
import org.gitools.ui.core.utils.FileChooserUtils;
import org.gitools.ui.platform.dialog.ExceptionGlassPane;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.settings.Settings;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.utils.HttpUtils;
import org.gitools.utils.progressmonitor.ProgressMonitorInputStream;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class WelcomeEditor extends HtmlEditor {

    private static final long serialVersionUID = 6851947500231401412L;

    public WelcomeEditor() {
        super("Welcome", getWelcomeURL());
    }

    @Override
    protected void exception(Exception e) {
        ExceptionGlassPane.show(Application.get(), e);
    }

    @Override
    protected void performAction(String name, Map<String, String> params) {
        switch (name) {
            case "open": {
                switch (params.get("ref")) {
                    case "filesystem":
                        new OpenFromFilesystemBrowseAction().actionPerformed(new ActionEvent(this, 0, name));
                        break;
                    case "genomespace":
                        new OpenFromGenomeSpaceAction().actionPerformed(new ActionEvent(this, 0, name));
                        break;
                    case "shortcuts":
                        new ShortcutsAction().actionPerformed(new ActionEvent(this, 0, name));
                        break;
                }
                break;
            }
            case "reload":
                navigate(getWelcomeURL());
                break;
        }
    }

    @Override
    protected void performSave(final String href) {

        final URL url;
        try {
            url = new URL(href);
        } catch (MalformedURLException e) {
            throw new UnsupportedOperationException("Invalid URL '" + href + "'");
        }

        String fileName = FilenameUtils.getName(url.getFile());
        final File file;

        if (fileName.endsWith(".heatmap.zip")) {

            fileName = fileName.replace(".heatmap.zip", "");
            SaveFileWizard wiz = SaveFileWizard.createSimple(
                    "Save",
                    fileName,
                    Settings.get().getLastPath(),
                    new FileFormat("Heatmap, single file (*.heatmap.zip)", HeatmapFormat.EXTENSION + ".zip", false, false)
            );

            WizardDialog dlg = new WizardDialog(Application.get(), wiz);
            dlg.setVisible(true);
            if (dlg.isCancelled()) {
                return;
            }

            file = wiz.getPathAsFile();

        } else {
            FileChoose choose = FileChooserUtils.selectFile("Save file", Settings.get().getLastPath(), FileChooserUtils.MODE_SAVE);

            if (choose == null) {
                return;
            }

            file = choose.getFile();
        }

        if (file != null) {

            JobThread.execute(Application.get(), new JobRunnable() {
                @Override
                public void run(IProgressMonitor monitor) {
                    try {
                        monitor.begin("Downloading file...", HttpUtils.getContentLength(url));
                        IOUtils.copy(new ProgressMonitorInputStream(monitor, url.openStream()), new FileOutputStream(file));
                    } catch (IOException e) {
                        throw new UnsupportedOperationException("Error saving the file");
                    }
                }
            });

            // Track event
            String baseName = FilenameUtils.getBaseName(href);
            Application.get().trackEvent("datasets", "save", baseName);

        }


    }

    @Override
    protected void performLoad(String href) {

        CommandLoadFile loadFile = new CommandLoadFile(href);

        // Execute
        JobThread.execute(Application.get(), loadFile);

        // Track event
        String baseName = FilenameUtils.getBaseName(href);
        Application.get().trackEvent("datasets", "load", baseName);

    }

    @Override
    public void doVisible() {
    }

    private static URL getWelcomeURL() {
        try {
            Proxy proxy = Settings.get().getProxy();
            URL url = new URL(Settings.get().getWelcomeUrl() + "?appversion=" + Application.getGitoolsVersion().toString() + "&uuid=" + Settings.get().getUuid());
            URLConnection connection = url.openConnection(proxy);
            connection.setConnectTimeout(2000);

            if (connection.getContentLength() != -1) {
                return url;
            }

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }

        return WelcomeEditor.class.getResource("/html/welcome.html");
    }
}
