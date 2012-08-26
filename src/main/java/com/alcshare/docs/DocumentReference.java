package com.alcshare.docs;

import com.controlj.green.addonsupport.AddOnInfo;
import com.controlj.green.addonsupport.access.Location;

import java.io.File;

/**
 *
 */
public class DocumentReference
{
    // todo - use lookupString after we can generate the default file
    //private String lookupString;

    private String gqlPath;
    private String title;
    private String docPath;
    private Location location;
    private static String ADDON_NAME;

    // todo - field for specifying order of documents
    // todo - type and user specified columns


    public DocumentReference(String gqlPath, String title, String docPath, Location location) {
        this.gqlPath = gqlPath;
        this.title = title;
        this.docPath = getNormalizedDocPath(docPath);
        this.location = location;
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

}
