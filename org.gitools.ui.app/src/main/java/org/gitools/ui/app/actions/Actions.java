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
package org.gitools.ui.app.actions;

import org.gitools.plugins.mutex.MutualExclusiveAnalysisAction;
import org.gitools.plugins.mutex.actions.MutualExclusiveSortAction;
import org.gitools.ui.app.actions.analysis.*;
import org.gitools.ui.app.actions.data.*;
import org.gitools.ui.app.actions.data.analysis.ViewEnrichmentModuleDataAction;
import org.gitools.ui.app.actions.edit.*;
import org.gitools.ui.app.actions.file.*;
import org.gitools.ui.app.actions.toolbar.HeatmapCreateImageAction;
import org.gitools.ui.app.actions.toolbar.HeatmapSearchAction;
import org.gitools.ui.app.svg.ExportHeatmapSVGAction;
import org.gitools.ui.core.actions.ActionManager;
import org.gitools.ui.core.actions.BaseAction;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class Actions {

    // Open
    public static final BaseAction open = new OpenFromFileSystemActionSet();
    public static final BaseAction openBrowse = new OpenFromFilesystemBrowseAction();
    public static final BaseAction openGenomeSpace = new OpenFromGenomeSpaceAction();
    public static final BaseAction openURL = new OpenFromURLAction();

    // Save
    public static final BaseAction saveAction = new SaveAction();
    public static final BaseAction saveAsAction = new SaveAsAction();

    // Exit
    public static final BaseAction exitAction = new ExitAction();

    //Edit
    public static final BaseAction createBookmarkAction = new CreateBookmarkAction();
    public static final BaseAction bookmarksDropdown = new BookmarksDropdown();

    // Import
    public static final BaseAction importBioMartModulesAction = new ImportBiomartModulesAction();
    public static final BaseAction importBioMartTableAction = new ImportBiomartTableAction();
    public static final BaseAction importKeggModulesAction = new ImportKeggModulesAction();
    public static final BaseAction importGoModulesAction = new ImportGoModulesAction();

    // Export
    public static final BaseAction exportAnnotationAction = new ExportAnnotationAction();
    public static final BaseAction exportMatrixAction = new ExportMatrixAction();
    public static final BaseAction exportTableAction = new ExportTableAction();
    public static final BaseAction exportHeatmapImageAction = new ExportHeatmapImageAction();
    public static final BaseAction exportHeatmapSVGAction = new ExportHeatmapSVGAction();
    public static final BaseAction exportHierarchicalTreeImageAction = new ExportHierarchicalTreeImageAction();
    public static final BaseAction exportScaleImageAction = new ExportScaleImageAction();

    // Other
    public static final BaseAction openIntegrativeGenomicViewerAction = new OpenIntegrativeGenomicViewerAction();
    public static final BaseAction selectAllAction = new SelectAllAction();
    public static final BaseAction selectLabelHeaderAction = new SelectLabelHeaderAction();
    public static final BaseAction unselectLabelHeaderAction = new UnselectLabelHeaderAction();

    public static final BaseAction unselectAllAction = new UnselectAllAction();
    public static final BaseAction addRowHeader = new AddHeaderAction(ROWS);
    public static final BaseAction addColumnHeader = new AddHeaderAction(COLUMNS);

    public static final BaseAction copyToClipboardSelectedLabelHeader = new CopyToClipboardSelectedLabelHeaderAction();

    public static final BaseAction filterRowsByLabel = new FilterByAnnotationsAction(ROWS);

    public static final BaseAction filterColumnsByLabel = new FilterByAnnotationsAction(COLUMNS);

    public static final BaseAction filterRowsByValue = new FilterByValueAction(ROWS);

    public static final BaseAction filterColumnsByValue = new FilterByValueAction(COLUMNS);

    public static final BaseAction showAllRowsAction = new ShowAllAction(ROWS);

    public static final BaseAction showAllColumns = new ShowAllAction(COLUMNS);

    public static final BaseAction hideSelectedColumns = new HideSelectionAction(COLUMNS);

    public static final BaseAction hideThisLabelHeaderAction = new HideThisLabelHeaderAction();

    public static final BaseAction hideGreaterThanHeaderAction = new HideNumericHeaderAction(true, "greater");

    public static final BaseAction hideSmallerThanHeaderAction = new HideNumericHeaderAction(false, "smaller");

    public static final BaseAction sortByRowsAnnotation = new SortByAnnotationAction(ROWS);

    public static final BaseAction sortByColumnsAnnotation = new SortByAnnotationAction(COLUMNS);

    public static final BaseAction sortByPredefinedListRowsAction = new SortByPredefinedListAction(ROWS);

    public static final BaseAction sortByPredefinedListColumnsAction = new SortByPredefinedListAction(COLUMNS);

    public static final BaseAction sortByHeader = new SortByHeaderAction();

    public static final BaseAction invertOrder = new InvertOrderAction();

    public static final BaseAction sortColumnsByValue = new SortByValueAction(COLUMNS);

    public static final BaseAction sortRowsByValue = new SortByValueAction(ROWS);

    public static final BaseAction sortColumnsByMutualExclusion = new MutualExclusiveSortAction(COLUMNS);

    public static final BaseAction sortRowsByMutualExclusion = new MutualExclusiveSortAction(ROWS);

    public static final BaseAction hideSelectedRowsAction = new HideSelectionAction(ROWS);

    public static final BaseAction showOnlyHeaderAction = new ShowOnlyLabelHeaderAction();

    public static final BaseAction clusteringAction = new ClusteringAction();

    public static final BaseAction mutexAnalysisAction = new MutualExclusiveAnalysisAction();

    public static final BaseAction enrichment = new EnrichmentAnalysisAction();

    public static final BaseAction combinations = new CombinationsAction();

    public static final BaseAction correlations = new CorrelationsAction();

    public static final BaseAction overlapping = new OverlappingsAction();

    public static final BaseAction groupComparison = new GroupComparisonAction();

    public static final BaseAction snapshotAction = new HeatmapCreateImageAction();

    public static final BaseAction searchRowsAction = new HeatmapSearchAction(ROWS);

    public static final BaseAction addNewLayersFromFile = new AddNewLayersFromFileAction();

    public static final BaseAction addNewLayersFromTransformation = new AddNewLayerFromDataTransformation();

    public static final BaseAction heatmapSettings = new HeatmapSettingsAction();

    public static final BaseAction viewEnrichmentModuleData = new ViewEnrichmentModuleDataAction();


    private Actions() {
    }

    public static void init() {
        ActionManager am = ActionManager.getDefault();
        am.addRootAction(MenuActionSet.INSTANCE);
        am.addRootAction(ToolBarActionSet.INSTANCE);
    }
}
