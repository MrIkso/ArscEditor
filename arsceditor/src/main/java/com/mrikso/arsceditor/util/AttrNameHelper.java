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

import com.mrikso.arsceditor.model.ResId;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class AttrNameHelper {

    private final HashMap<Integer, ResId> attrAndroidMap = new HashMap<>();

    private HashMap<Integer, ResId> attrPackageMap = new HashMap<>();

    public static final String PUBLIC_XML = "public-final.xml";
    private static AttrNameHelper instance;

    public static AttrNameHelper getInstance() {
        if (instance == null) {
            instance = new AttrNameHelper();
            return instance;
        }
        return instance;
    }

    public AttrNameHelper() {
        try {
            initPublicNameToResId();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void initPublicNameToResId() throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResourceAsStream(PUBLIC_XML);
        Document document = db.parse(in);
        document.normalize();
        NodeList nodeList = document.getElementsByTagName("public");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String type = element.getAttribute("type");
                String name = element.getAttribute("name");
                String id = element.getAttribute("id");
                if (!id.isEmpty())
                    attrAndroidMap.put(Integer.decode(id), new ResId(Integer.decode(id), name, type));
            }
        }
    }

    public void setAttrPackageMap(HashMap<Integer, ResId> attrPackageMap) {
        this.attrPackageMap = attrPackageMap;
    }

    public String getName(int id, String pkg) {
        if (attrAndroidMap.containsKey(id)) {
            ResId resId = attrAndroidMap.get(id);
            if (resId.getType().equals("attr")) {
                return "android:" + resId.getName();
            } else {
                return "android:" + resId.getType() + "/" + resId.getName();
            }
        } else if (attrPackageMap.containsKey(id)) {
            ResId resId = attrPackageMap.get(id);
            if (resId.getType().equals("attr")) {
                return resId.getName();
            } else {
                return resId.getType() + "/" + resId.getName();
            }
        }
        return null;
    }

    /*public int getId(String name, String pkg) {
        if (!"android".equals(pkg)) {
            if (attrAndroidMap.containsValue(name)) {
                return attrAndroidMap.inverse().get(name);
            }
        }
        return 0;
    }*/

}
