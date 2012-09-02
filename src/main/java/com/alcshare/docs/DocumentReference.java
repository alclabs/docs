package com.alcshare.docs;

import com.alcshare.docs.util.Logging;
import com.controlj.green.addonsupport.AddOnInfo;
import com.controlj.green.addonsupport.access.Location;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
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
    private String pathType;
    private Location location;
    private Map<String,String> extraColumns;
    private static String ADDON_NAME;


    // todo - field for specifying order of documents
    // todo - type and user specified columns


    public DocumentReference(String gqlPath, String title, String docPath, String pathType, Location location, Map<String,String> extraColumns) {
        this.gqlPath = gqlPath;
        this.title = title;
        this.docPath = docPath;
        this.pathType = pathType;
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


    public String getURL() {
        if (isPathTypeDoc()) {
            if (ADDON_NAME == null) {
                try {
                    ADDON_NAME = AddOnInfo.getAddOnInfo().getName();
                } catch (Throwable th) { ADDON_NAME="TEST"; } // AddOnInfo not available during unit tests
            }
            URI uri;
            try {
                uri = new URI(null, null, "/"+ADDON_NAME+"/content"+getNormalizedDocPath(), null);
                return uri.toString();
            } catch (URISyntaxException e) {
                Logging.println("Error formatting document URI", e);
            }
        }
        return docPath;
    }

    public Location getLocation() {
        return location;
    }

    public String getPathType() {
        if (isPathTypeDoc()) { return "DOC"; }
        else if (isPathTypeURL()) { return "URL"; }
        else return pathType;
    }

    private boolean isPathTypeDoc() {
        return (pathType == null || pathType.length()==0 || pathType.equalsIgnoreCase("doc"));
    }

    private boolean isPathTypeURL() {
        return (pathType != null && pathType.equalsIgnoreCase("url"));
    }

    private String getNormalizedDocPath() {
        if (docPath.startsWith("/")) {
            return docPath;
        } else {
            return "/" + docPath;
        }
    }
    public String get(String columnName) {
        String columnValue = extraColumns.get(columnName);
        if (columnValue == null) {
            if (columnName.equalsIgnoreCase("title")) {
                return getTitle();
            } else if (columnName.equalsIgnoreCase("url")) {
                return getURL();
            } else return "";  //todo - figure out a better way to handle errors - how does velocity handle exceptions?
        }
        return columnValue;
    }

}
