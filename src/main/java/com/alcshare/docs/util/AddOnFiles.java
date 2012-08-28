package com.alcshare.docs.util;

import com.controlj.green.addonsupport.AddOnInfo;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public class AddOnFiles {
    private static final AtomicReference<File> addOnPrivateFile = new AtomicReference<File>();
    private static final AtomicReference<File> addOnDocBaseFile = new AtomicReference<File>();
    private static final AtomicReference<File> addOnConfigBaseFile = new AtomicReference<File>();
    private static final AtomicReference<File> addOnTemplateBaseFile = new AtomicReference<File>();

    public static File getDocDirectory() {
        File docBaseFile = addOnDocBaseFile.get();
        if (docBaseFile  == null) {
            docBaseFile = new File(getAddOnPrivateDirectory(), "docs");
            docBaseFile.mkdirs();
            addOnDocBaseFile.set(docBaseFile);
        }
        return docBaseFile;
    }

    public static File getConfigDirectory() {
        File configBaseFile = addOnConfigBaseFile.get();
        if (configBaseFile  == null) {
            configBaseFile = new File(getAddOnPrivateDirectory(), "config");
            configBaseFile.mkdirs();
            addOnConfigBaseFile.set(configBaseFile);
        }
        return configBaseFile;
    }
    
    public static File getTemplatesDirectory() {
        File templatesBaseFile = addOnTemplateBaseFile.get();
        if (templatesBaseFile == null) {
            templatesBaseFile = new File(getAddOnPrivateDirectory(), "templates");
            templatesBaseFile.mkdirs();
            addOnTemplateBaseFile.set(templatesBaseFile);
        }
        return templatesBaseFile;
    }


    private static File getAddOnPrivateDirectory() {
        File privateFile = addOnPrivateFile.get();
        if (privateFile == null) {
            AddOnInfo aoi = AddOnInfo.getAddOnInfo();
            File systemFile = aoi.getPublicDir().getParentFile().getParentFile();
            String relPath = "webapp_data/"+aoi.getName()+"/private";
            privateFile = new File(systemFile, relPath);
            addOnPrivateFile.set(privateFile);
        }
        return privateFile;
    }
}
