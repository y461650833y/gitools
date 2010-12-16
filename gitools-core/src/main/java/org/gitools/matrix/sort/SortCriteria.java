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

package org.gitools.matrix.sort;

import org.gitools.aggregation.IAggregator;

public final class SortCriteria {

	public enum SortDirection {
		ASCENDING("Ascending", 1), 
		DESCENDING("Descending", -1);

		private String title;
		private int factor;

		private SortDirection(String title, int factor) {
			this.title = title;
			this.factor = factor;
		}

		public int getFactor() {
			return factor;
		}
		
		@Override
		public String toString() {
			return title;
		}
	}

	protected String attributeName;
	protected int attributeIndex;
	protected IAggregator aggregator;
	protected SortDirection direction;

	public SortCriteria(
			int attributeIndex,
			IAggregator aggregator,
			SortDirection direction) {
		
		this(null, attributeIndex, aggregator, direction);
	}

	public SortCriteria(
			String attributeName,
			int attributeIndex,
			IAggregator aggregator,
			SortDirection direction) {

		this.attributeName = attributeName;
		this.attributeIndex = attributeIndex;
		this.direction = direction;
		this.aggregator = aggregator;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public final int getAttributeIndex() {
		return attributeIndex;
	}

	public final void setAttributeIndex(int propIndex) {
		this.attributeIndex = propIndex;
	}

	public final SortDirection getDirection() {
		return direction;
	}

	public final void setDirection(SortDirection direction) {
		this.direction = direction;
	}

	public final IAggregator getAggregator() {
		return aggregator;
	}

	public final void setAggregator(IAggregator aggregator) {
		this.aggregator = aggregator;
	}

	@Override
	public String toString() {
		return attributeName + ", "
			+ aggregator.toString() + ", "
			+ direction.toString();
	}
}