package com.alcshare.docs;

import com.controlj.green.addonsupport.AddOnInfo;
import com.controlj.green.addonsupport.access.Location;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class DocumentReference
{

    private String gqlPath;
    private String title;
    private String docPath;
    private Location location;
    private Map<String,String> extraColumns;
    private static String ADDON_NAME;


    // todo - field for specifying order of documents
    // todo - type and user specified columns


    public DocumentReference(String gqlPath, String title, String docPath, Location location, Map<String,String> extraColumns) {
        this.gqlPath = gqlPath;
        this.title = title;
        this.docPath = getNormalizedDocPath(docPath);
        this.location = location;
        this.extraColumns = extraColumns;
    }

    public String getGqlPath()
    {
        return gqlPath;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDocPath()
    {
        return docPath;
    }


    public String getDocURL() {
        if (ADDON_NAME == null) {
            ADDON_NAME = AddOnInfo.getAddOnInfo().getName();
        }
        return "/"+ADDON_NAME+"/content"+getDocPath();
    }

    public Location getLocation() {
        return location;
    }

    private String getNormalizedDocPath(String path) {
        if (path.startsWith("/")) {
            return path;
        } else {
            return "/" + path;
        }
    }
    public String get(String columnName) {
        String columnValue = extraColumns.get(columnName);
        if (columnValue == null) {
            if (columnName.equalsIgnoreCase("title")) {
                columnValue = getTitle();
            } else if (columnName.equalsIgnoreCase("docurl")) {
                columnValue = getDocURL();
            }
        }
        return columnValue;
    }

}
