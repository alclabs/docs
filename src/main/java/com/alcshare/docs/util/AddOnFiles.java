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

    public static File getDocBaseFile() {
        if (docBaseFile  == null) {
            docBaseFile = new File(getAddOnPrivateFile(), "docs");
            docBaseFile.mkdirs();
        }
        return docBaseFile;
    }

    public static File getConfigBaseFile() {
        if (configBaseFile  == null) {
            configBaseFile = new File(getAddOnPrivateFile(), "config");
            configBaseFile.mkdirs();
        }
        return configBaseFile;
    }

    public static File getAddOnPrivateFile() {
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
