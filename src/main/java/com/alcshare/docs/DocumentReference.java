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

    // todo - figure out where we really want this and don't hard code it
    private static String DOC_BASE_URL;

    // todo - field for specifying order of documents
    // todo - type and user specified columns


    public DocumentReference(String gqlPath, String title, String docPath, Location location) {
        this.gqlPath = gqlPath;
        this.title = title;
        this.docPath = docPath;
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

    //todo - this needs to find another home
    public String getDocURL() {
        if (DOC_BASE_URL == null) {
            AddOnInfo aoi = AddOnInfo.getAddOnInfo();
            File publicFile = aoi.getPublicDir();
            String name = aoi.getName();
            File systemFile = publicFile.getParentFile().getParentFile();
            String systemName = systemFile.getName();
            String relPath = systemName+"/webapp_data/"+name+"/private/docs/";
            File docBaseFile = new File(systemFile, relPath);
            docBaseFile.mkdirs();

            DOC_BASE_URL = "/"+relPath;
        }
        return DOC_BASE_URL + docPath;
    }

    public Location getLocation() {
        return location;
    }

}
