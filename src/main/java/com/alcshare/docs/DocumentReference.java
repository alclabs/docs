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
    public enum PathType { DOC, URL, DIR, DISCOVERED, OTHER};

    private String referencePath;
    private String title;
    private String docPath;
    private PathType pathType;
    private String category;
    private Location location;
    private Map<String,String> extraColumns;


    // todo - field for specifying order of documents
    // todo - type and user specified columns

    // todo - DIR link type


    public DocumentReference(String referencePath, String title, String docPath, PathType pathType, String category, Location location, Map<String,String> extraColumns) {
        this.referencePath = referencePath;
        this.title = title;
        this.docPath = docPath;
        this.pathType = pathType;
        this.category = category;
        this.location = location;
        this.extraColumns = extraColumns;
    }

    public static PathType stringToPathType(String stringType) {
        PathType result;
        try {
            result = PathType.valueOf(stringType.toUpperCase());
        } catch (Exception e) {
            result = PathType.OTHER;
        }
        return result;
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

    public PathType getPathType() {
        return pathType;
    }

    public File getDocFile() {
        return canonicalize(new File(AddOnFiles.getDocDirectory(), getNormalizedDocPath()));
    }

    public Map<String, String> getExtraColumns() {
        return extraColumns;
    }

    private File canonicalize(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            return file;
        }
    }

    // returns true for types who's existence can't be verified (like URLs or unknown types)
    public boolean checkDocExists() {
        boolean result = true;
        if (pathType == PathType.DOC || pathType == PathType.DISCOVERED) {
            result = getDocFile().exists();
        }
        return result;
    }

    public String getURL() {
        if (pathType == PathType.DOC || pathType == PathType.DISCOVERED) {
            try {
                URI uri = new URI(null, null, "/" + AddOnInfoHelper.getAddonName() + "/content" + getNormalizedDocPath(), "title="+getTitle(), null);
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

    public String getCategory() {
        if (category!=null && category.length()>0) {
            return category;
        } else {
            return "Uncategorized";
        }
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
            } else if (columnName.equalsIgnoreCase("pathtype")) {
                return getPathType();
            }
            else return "";  //todo - figure out a better way to handle errors - how does velocity handle exceptions?
        }
        return columnValue;
    }

}
