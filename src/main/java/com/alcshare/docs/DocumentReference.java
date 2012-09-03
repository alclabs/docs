package com.alcshare.docs;

import com.alcshare.docs.util.AddOnFiles;
import com.alcshare.docs.util.AddOnInfoHelper;
import com.alcshare.docs.util.Logging;
import com.controlj.green.addonsupport.access.Location;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 *
 */
public class DocumentReference
{
    private String referencePath;
    private String title;
    private String docPath;
    private String pathType;
    private String category;
    private Location location;
    private Map<String,String> extraColumns;


    // todo - field for specifying order of documents
    // todo - type and user specified columns


    public DocumentReference(String referencePath, String title, String docPath, String pathType, String category, Location location, Map<String,String> extraColumns) {
        this.referencePath = referencePath;
        this.title = title;
        this.docPath = docPath;
        this.pathType = pathType;
        this.category = category;
        this.location = location;
        this.extraColumns = extraColumns;
    }

    public String getReferencePath() {
        return referencePath;
    }

    public String getTitle() {
        return title;
    }

    public String getDocPath() {
        return docPath;
    }

    public File getDocFile() {
        return canonicalize(new File(AddOnFiles.getDocDirectory(), getDocPath()));
    }

    private File canonicalize(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            return file;
        }
    }

    // returns true if path type is not DOC, or if it is DOC and getDocFile().exists()
    public boolean checkDocExists() {
        return !isPathTypeDoc() || getDocFile().exists();
    }

    public String getURL() {
        if (isPathTypeDoc()) {
            try {
                URI uri = new URI(null, null, "/" + AddOnInfoHelper.getAddonName() + "/content" + getNormalizedDocPath(), null);
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

    public String getCategory() {
        if (category!=null && category.length()>0) {
            return category;
        } else {
            return "Uncategorized";
        }
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
    public Object get(String columnName) {
        String columnValue = extraColumns.get(columnName);
        if (columnValue == null) {
            if (columnName.equalsIgnoreCase("title")) {
                return getTitle();
            } else if (columnName.equalsIgnoreCase("url")) {
                return getURL();
            } else if (columnName.equalsIgnoreCase("category")) {
                return getCategory();
            } else if (columnName.equalsIgnoreCase("exists")) {
                return checkDocExists();
            }
            else return "";  //todo - figure out a better way to handle errors - how does velocity handle exceptions?
        }
        return columnValue;
    }

}
