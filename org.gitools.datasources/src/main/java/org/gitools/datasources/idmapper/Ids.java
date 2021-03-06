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
package org.gitools.datasources.idmapper;

public interface Ids {

    public static final String NCBI_REFSEQ = "ncbi:refseq";
    public static final String NCBI_UNIGENE = "ncbi:unigene";
    public static final String NCBI_GENES = "ncbi:gene";
    public static final String PDB = "pdb:protein";
    public static final String UNIPROT = "uniprot:protein";

    public static final String GO_BP = "go:bp";
    public static final String GO_MF = "go:mf";
    public static final String GO_CL = "go:cl";
    public static final String GO_ID = "go:id";
}
