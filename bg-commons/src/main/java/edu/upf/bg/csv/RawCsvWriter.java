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

package edu.upf.bg.csv;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

public class RawCsvWriter {

	private PrintWriter out;
	private char separator;
	private char quote;
	
	public RawCsvWriter(Writer writer, char separator, char quote) {
		this.out = new PrintWriter(writer);
		this.separator = separator;
		this.quote = quote;
	}
	
	public void writeNewLine() {
		out.println();
	}
	
	public void writeValue(String value) {
		out.print(value);
	}
	
	public void writeQuotedValue(String value) {
		out.print(quote);
		out.print(value);
		out.print(quote);
	}
	
	public void writeProperty(String name, String value) {
		out.print(name);
		out.print(separator);
		writeQuotedValue(value);
		out.println();
	}

	public void writePropertyList(String name, String[] values) {
		out.print(name);
		if (values.length > 0) {
			for (String value : values) {
				out.print(separator);
				writeQuotedValue(value);
			}
		}
		out.println();
	}
	
	public void writePropertyList(String name, List<String> values) {
		out.print(name);
		if (values.size() > 0) {
			for (String value : values) {
				out.print(separator);
				writeQuotedValue(value);
			}
		}
		out.println();
	}

	public void writeSeparator() {
		out.print(separator);
	}

	public void write(String raw) {
		out.print(raw);
	}
	
	public void close() {
		out.close();
	}
}