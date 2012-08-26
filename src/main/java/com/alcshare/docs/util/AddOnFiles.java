package com.alcshare.docs.util;

import com.controlj.green.addonsupport.AddOnInfo;

import java.io.File;

/**
 *
 */
public class AddOnFiles {
    private static File addOnPrivateFile;
    private static File docBaseFile;
    private static File configBaseFile;
    private static File templatesFile;

    public static File getDocDirectory() {
        if (docBaseFile  == null) {
            docBaseFile = new File(getAddOnPrivateDirectory(), "docs");
            docBaseFile.mkdirs();
        }
        return docBaseFile;
    }

    public static File getConfigDirectory() {
        if (configBaseFile  == null) {
            configBaseFile = new File(getAddOnPrivateDirectory(), "config");
            configBaseFile.mkdirs();
        }
        return configBaseFile;
    }

    public static File getTemplatesDirectory() {
        if (templatesFile == null) {
            templatesFile = new File(getAddOnPrivateDirectory(), "templates");
            templatesFile.mkdirs();
        }
        return templatesFile;
    }

    public static File getAddOnPrivateDirectory() {
        if (addOnPrivateFile  == null) {
            AddOnInfo aoi = AddOnInfo.getAddOnInfo();
            File publicFile = aoi.getPublicDir();
            String name = aoi.getName();
            File systemFile = publicFile.getParentFile().getParentFile();
            String relPath = "webapp_data/"+name+"/private";
            addOnPrivateFile = new File(systemFile, relPath);
            addOnPrivateFile.mkdirs();
        }
        return addOnPrivateFile;
    }


}
