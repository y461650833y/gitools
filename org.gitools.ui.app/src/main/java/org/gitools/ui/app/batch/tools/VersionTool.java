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
package org.gitools.ui.app.batch.tools;

import org.gitools.ui.app.Main;
import org.gitools.ui.core.commands.tools.ITool;

import java.io.PrintWriter;

public class VersionTool implements ITool {

    public VersionTool() {
        super();
    }


    @Override
    public String getName() {
        return "version";
    }

    @Override
    public boolean run(String[] args, PrintWriter out) {
        out.println(getVersion());
        return true;
    }

    @Override
    public String getExitMessage() {
        return null;
    }

    @Override
    public int getExitStatus() {
        return 0;
    }

    public static String getVersion() {
        return "Gitools " + Main.class.getPackage().getImplementationVersion();
    }

    @Override
    public boolean check(String[] args, PrintWriter out) {
        return true;
    }

}
