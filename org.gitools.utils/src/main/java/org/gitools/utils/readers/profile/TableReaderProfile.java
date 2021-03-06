/*
 * #%L
 * org.gitools.utils
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.utils.readers.profile;

import com.google.common.primitives.Ints;
import org.gitools.utils.readers.FileHeader;

import java.util.ArrayList;
import java.util.List;

public class TableReaderProfile extends ReaderProfile {

    private int[] heatmapColumnsIds;
    private int[] heatmapRowsIds;
    private String fieldGlue;

    public TableReaderProfile() {
        super();
        this.name = "deafultTable";
        this.heatmapColumnsIds = new int[]{0};
        this.heatmapRowsIds = new int[]{1};
        this.fieldGlue = "-";
        this.layout = TABLE;
    }

    public static TableReaderProfile fromProfile(ReaderProfile profile) {
        TableReaderProfile newProfile = new TableReaderProfile();
        newProfile.setCommentChar(profile.getCommentChar());
        newProfile.setIgnoredColumns(profile.getIgnoredColumns());
        newProfile.setColumnAnnotationColumns(profile.getColumnAnnotationColumns());
        newProfile.setRowAnnotationColumns(profile.getRowAnnotationColumns());
        newProfile.setSeparator(profile.getSeparator());
        newProfile.setSkipLines(profile.getSkipLines());
        return newProfile;
    }

    @Override
    public void validate(List<FileHeader> inFileHeaders) throws ReaderProfileValidationException {

        if (heatmapColumnsIds.length == 0 ||
                heatmapRowsIds.length == 0) {
            throw new ReaderProfileValidationException(
                    "At least one element is required for column Ids and row Ids"
            );

        }

        List<Integer> intersect = new ArrayList<>(Ints.asList(heatmapColumnsIds));
        List<Integer> idUnion = new ArrayList<>(Ints.asList(heatmapColumnsIds));
        List<Integer> rows = new ArrayList<>(Ints.asList(heatmapRowsIds));
        idUnion.addAll(rows);

        // Intersect?
        intersect.retainAll(rows);
        if (intersect.size() > 0) {
            throw new ReaderProfileValidationException("An element cannot be specified as row and column Id.");
        }

        if (valueColumns.length == 0) {
            List<Integer> valueIndices = new ArrayList<>();
            List<Integer> ignored = Ints.asList(ignoredColumns);
            for (FileHeader h : inFileHeaders) {
                if ((!idUnion.contains(h.getPos())) && (!ignored.contains(h.getPos()))) {
                    valueIndices.add(h.getPos());
                }
            }
            valueColumns = Ints.toArray(valueIndices);
            if (valueColumns.length == 0) {
                throw new ReaderProfileValidationException(
                        "No data value columns available (too many ignored columns?)"
                );
            }
        }
    }

    @Override
    public int getValueColumnsNumber() {
        return valueColumns.length;
    }


    /**
     * Which columns in the flat text are mapped as heatmap row id
     */
    public int[] getHeatmapRowsIds() {
        return heatmapRowsIds;
    }

    /**
     * @param heatmapRowsIds indices of columns mapped to heatmap row ids
     */
    public void setHeatmapRowsIds(int[] heatmapRowsIds) {
        this.heatmapRowsIds = heatmapRowsIds;
    }

    /**
     * If multiple values are used as Column/Row ids, this character will unite the fields
     */
    public String getFieldGlue() {
        return fieldGlue;
    }

    /**
     * Which columns in the flat text are mapped as heatmap column id
     */
    public int[] getHeatmapColumnsIds() {
        return heatmapColumnsIds;
    }

    /**
     * @param heatmapColumnsIds: indices of columns mapped to heatmap row ids
     */
    public void setHeatmapColumnsIds(int[] heatmapColumnsIds) {
        this.heatmapColumnsIds = heatmapColumnsIds;
    }

    /**
     * @param fieldGlue String to glue id fields together.
     */
    public void setFieldGlue(String fieldGlue) {
        this.fieldGlue = fieldGlue;
    }
}
