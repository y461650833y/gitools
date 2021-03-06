/*
 * #%L
 * gitools-core
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
package org.gitools.matrix.model.hashmatrix;

import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.matrix.model.AbstractMatrixDimension;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HashMatrixDimension extends AbstractMatrixDimension {

    private Map<String, Integer> labelToIndex;
    private Map<Integer, String> indexToLabel;

    public HashMatrixDimension(MatrixDimensionKey id) {
        super(id);

        this.labelToIndex = new ConcurrentHashMap<>();
        this.indexToLabel = new ConcurrentHashMap<>();
    }

    public HashMatrixDimension(MatrixDimensionKey id, Iterable<String> labels) {
        this(id);

        for (String label : labels) {
            add(label);
        }
    }

    @Override
    public int size() {
        return labelToIndex.size();
    }

    @Override
    public String getLabel(int index) {
        return indexToLabel.get(index);
    }

    @Override
    public int indexOf(String label) {

        if (labelToIndex.containsKey(label)) {
            return labelToIndex.get(label);
        }

        return -1;
    }

    void add(String label) {

        if (labelToIndex.containsKey(label)) {
            return;
        }

        Integer nextIndex = this.labelToIndex.size();
        this.labelToIndex.put(label, nextIndex);
        this.indexToLabel.put(nextIndex, label);
    }

    @Override
    public IMatrixDimension subset(Set<String> identifiers) {
        return new HashMatrixDimension(getId(), identifiers);
    }

    public void optimize(Iterable<String> identifiers) {

        Map<String, Integer> allIds = labelToIndex;
        this.labelToIndex = new HashMap<>();
        this.indexToLabel = new HashMap<>();

        for (String id : identifiers) {
            add(id);
            if (allIds.containsKey(id)) {
                allIds.remove(id);
            }
        }

        for (String id : allIds.keySet()) {
            add(id);
        }
    }
}
