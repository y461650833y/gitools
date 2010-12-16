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

package org.gitools.persistence.text;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import org.gitools.datafilters.DoubleTranslator;
import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.persistence.PersistenceException;

public class DoubleBinaryMatrixTextPersistence
		extends MatrixTextPersistence<DoubleBinaryMatrix> {

	@Override
	protected DoubleBinaryMatrix createEntity() {
		return new DoubleBinaryMatrix();
	}

	@Override
	public DoubleBinaryMatrix read(File file, IProgressMonitor monitor)
			throws PersistenceException {

		return read(file, new DoubleTranslator(), monitor);
	}

	@Override
	public void write(File file, DoubleBinaryMatrix entity,
			IProgressMonitor monitor) throws PersistenceException {
		
		write(file, entity, new DoubleTranslator(), monitor);
	}

}