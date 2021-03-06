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

import org.gitools.datasources.kegg.modules.EnsemblKeggModulesImporter;
import org.gitools.ui.app.datasources.kegg.wizard.KeggModulesImportWizard;
import org.gitools.ui.app.wizard.ModulesImportWizard;
import org.gitools.ui.platform.icons.IconNames;

public class ImportKeggModulesAction extends AbstractImportModulesAction {

    public ImportKeggModulesAction() {
        super("KEGG pathways...");
        setLargeIconFromResource(IconNames.KEGG24);
        setSmallIconFromResource(IconNames.KEGG16);
    }

    @Override
    protected EnsemblKeggModulesImporter getImporter() {
        return new EnsemblKeggModulesImporter(true, false);
    }

    @Override
    protected ModulesImportWizard getWizard(EnsemblKeggModulesImporter importer) {
        return new KeggModulesImportWizard(importer);
    }

}
