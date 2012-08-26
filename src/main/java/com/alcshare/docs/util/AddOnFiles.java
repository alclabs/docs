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
    private static final AtomicReference<File> addOnContentBaseFile = new AtomicReference<File>();

    public static File getDocBaseFile() {
        File docBaseFile = addOnDocBaseFile.get();
        if (docBaseFile == null) {
            docBaseFile = new File(getAddOnPrivateFile(), "docs");
            docBaseFile.mkdirs();
            addOnDocBaseFile.set(docBaseFile);
        }
        return docBaseFile;
    }

    public static File getConfigBaseFile() {
        File configBaseFile = addOnConfigBaseFile.get();
        if (configBaseFile == null) {
            configBaseFile = new File(getAddOnPrivateFile(), "config");
            configBaseFile.mkdirs();
            addOnConfigBaseFile.set(configBaseFile);
        }
        return configBaseFile;
    }

    public static File getContentBaseFile() {
        File contentBaseFile = addOnContentBaseFile.get();
        if (contentBaseFile == null) {
            contentBaseFile = new File(getAddOnPrivateFile(), "content");
            contentBaseFile.mkdirs();
            addOnContentBaseFile.set(contentBaseFile);
        }
        return contentBaseFile;
    }

    private static File getAddOnPrivateFile() {
        File privateFile = addOnPrivateFile.get();
        if (privateFile == null) {
            AddOnInfo aoi = AddOnInfo.getAddOnInfo();
            File publicFile = aoi.getPublicDir();
            String name = aoi.getName();
            File systemFile = publicFile.getParentFile().getParentFile();
            String systemName = systemFile.getName();
            String relPath = systemName+"/webapp_data/"+name+"/private";
            privateFile = new File(systemFile, relPath);
            addOnPrivateFile.set(privateFile);
        }
        return privateFile;
    }
}
