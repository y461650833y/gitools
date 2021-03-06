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
package org.gitools.ui.app.actions.file;

import org.apache.commons.io.FilenameUtils;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.persistence.FileFormat;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.FileFormats;
import org.gitools.ui.app.heatmap.drawer.HeatmapDrawer;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.components.wizard.SaveFileWizard;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.settings.Settings;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExportHeatmapImageAction extends HeatmapAction {

    private static final long serialVersionUID = -7288045475037410310L;

    public ExportHeatmapImageAction() {
        super("Heatmap to image...");

        setDesc("Export the heatmap to image file");
        setMnemonic(KeyEvent.VK_H);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        SaveFileWizard saveWiz = SaveFileWizard.createSimple(
                "Export heatmap to image ...",
                FilenameUtils.getName(getSelectedEditor().getName()),
                Settings.get().getLastExportPath(),
                new FileFormat[]{FileFormats.PNG}
        );

        WizardDialog dlg = new WizardDialog(Application.get(), saveWiz);
        dlg.open();
        if (dlg.isCancelled()) {
            return;
        }

        Settings.get().setLastExportPath(saveWiz.getFolder());

        final File file = saveWiz.getPathAsFile();

        final String formatExtension = saveWiz.getFormat().getExtension();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    monitor.begin("Exporting heatmap to image ...", 1);
                    monitor.info("File: " + file.getName());

                    Heatmap hm = getHeatmap();

                    HeatmapDrawer drawer = new HeatmapDrawer(hm);
                    drawer.setPictureMode(true);

                    Dimension heatmapSize = drawer.getSize();

                    final BufferedImage bi = new BufferedImage(heatmapSize.width, heatmapSize.height, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = bi.createGraphics();
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, heatmapSize.width, heatmapSize.height);
                    drawer.draw(g, new Rectangle(new Point(), heatmapSize), new Rectangle(new Point(), heatmapSize));

                    ImageIO.write(bi, formatExtension, file);

                } catch (Exception ex) {
                    monitor.exception(ex);
                }
            }
        });

        Application.get().showNotification("Image created.", 2000);
    }


}
