package com.alcshare.docs.util;

import com.controlj.green.addonsupport.AddOnInfo;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public class AddOnFiles {
    private static final AtomicReference<File> privateDirectoryReference = new AtomicReference<File>();
    private static final AtomicReference<File> docDirectoryReference = new AtomicReference<File>();
    private static final AtomicReference<File> configDirectoryReference = new AtomicReference<File>();
    private static final AtomicReference<File> templateDirectoryReference = new AtomicReference<File>();
    private static final AtomicReference<File> imageDirectoryReference = new AtomicReference<File>();
    private static final AtomicReference<File> jsDirectoryReference = new AtomicReference<File>();

    public static File getDocDirectory() {
        return getDirectoryHelper("content", docDirectoryReference);
    }

    public static File getConfigDirectory() {
        return getDirectoryHelper("config", configDirectoryReference);
    }
    
    public static File getTemplatesDirectory() {
        return getDirectoryHelper("templates", templateDirectoryReference);
    }

    public static File getImageDirectory() {
        return getDirectoryHelper("img", getDocDirectory(), imageDirectoryReference);
    }

    public static File getJSDirectory() {
        return getDirectoryHelper("js", getDocDirectory(), jsDirectoryReference);
    }

    private static File getDirectoryHelper(String name, AtomicReference<File> ref) {
        return getDirectoryHelper(name, getAddOnPrivateDirectory(), ref);
    }

    private static File getDirectoryHelper(String name, File base, AtomicReference<File> ref) {
        File directory = ref.get();
        if (directory == null) {
            directory = new File(base, name);
            directory.mkdirs();
            ref.set(directory);
        }
        return directory;
    }


    private static File getAddOnPrivateDirectory() {
        File privateFile = privateDirectoryReference.get();
        if (privateFile == null) {
            File systemFile = AddOnInfoHelper.getPublicDir().getParentFile().getParentFile();
            String relPath = "webapp_data/"+AddOnInfoHelper.getAddonName()+"/private";
            privateFile = new File(systemFile, relPath);
            privateDirectoryReference.set(privateFile);
        }
        return privateFile;
    }
}
