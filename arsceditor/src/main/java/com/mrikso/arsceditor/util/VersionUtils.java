/*
 * Copyright (c) 2021 MrIkso
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.mrikso.arsceditor.util;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

public class VersionUtils {
    public static String getVersion() {
        try {
            ClassLoader classLoader = VersionUtils.class.getClassLoader();
            if (classLoader != null) {
                Enumeration<URL> resources = classLoader.getResources("META-INF/MANIFEST.MF");
                while (resources.hasMoreElements()) {
                    try (InputStream is = resources.nextElement().openStream()) {
                        Manifest manifest = new Manifest(is);
                        String ver = manifest.getMainAttributes().getValue("arseditor-version");
                        if (ver != null) {
                            return ver;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }
}
