/*
 *  Copyright 2010 chris.
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

package org.gitools.ui.modules.wizard;

import java.io.File;
import org.gitools.modules.importer.ModulesImporter;
import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileFormats;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.common.wizard.SaveFilePage;

public class ModulesImportWizard extends AbstractWizard {

	public static final String FORMAT_PLAIN = "TSV";
	public static final String FORMAT_COMPRESSED_GZ = "GZ";

	private FileFormat[] supportedFormats = new FileFormat[] {
		new FileFormat(FileFormats.MODULES_2C_MAP.getTitle(), FileFormats.MODULES_2C_MAP.getExtension(), FORMAT_PLAIN, true, false),
		new FileFormat(FileFormats.MODULES_2C_MAP.getTitle() + " compressed", FileFormats.MODULES_2C_MAP.getExtension() + ".gz", FORMAT_COMPRESSED_GZ, true, false)
	};

	private ModulesImporter importer;

	private ModulesSourcePage moduleCategoryPage;
	private ModulesOrganismPage organismPage;
	private ModulesFeaturesPage featuresPage;
	private SaveFilePage saveFilePage;

	public ModulesImportWizard(ModulesImporter importer) {
		this.importer = importer;

		setTitle("Import modules...");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_MODULES, 96));
	}

	@Override
	public void addPages() {

		moduleCategoryPage = new ModulesSourcePage(importer);
		addPage(moduleCategoryPage);

		organismPage = new ModulesOrganismPage(importer);
		addPage(organismPage);

		featuresPage = new ModulesFeaturesPage(importer);
		addPage(featuresPage);

		// Destination
		saveFilePage = new SaveFilePage() {
			@Override public void updateModel() {
				Settings.getDefault().setLastMapPath(getFolder()); } };

		saveFilePage.setTitle("Select destination file");
		saveFilePage.setFolder(Settings.getDefault().getLastMapPath());
		saveFilePage.setFormats(supportedFormats);
		addPage(saveFilePage);
	}

	public File getFile() {
		return saveFilePage.getFile();
	}
}