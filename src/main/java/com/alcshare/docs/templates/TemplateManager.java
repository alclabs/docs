package com.alcshare.docs.templates;

import com.alcshare.docs.util.AddOnFiles;
import com.alcshare.docs.util.FileResource;
import com.alcshare.docs.util.Logging;

import java.io.*;

/**
 *
 */
public class TemplateManager {
    private static String[] templateNames = new String[] {"default.vm", "collapse.vm", "group.vm"};
    private static final FileResource[]  TEMPLATES = getFileResources(templateNames);

    public static void copyDefaultTemplates() {
        for (FileResource template : TEMPLATES) {
            try {
                template.getFile();
            } catch (IOException e) {
                Logging.println("Error getting template ", e);
            }
        }
    }

    private static FileResource[] getFileResources(String[] names) {
        FileResource[] result = new FileResource[names.length];
        for (int i=0; i<names.length; i++) {
            result[i] = new FileResource(AddOnFiles.getTemplatesDirectory(), names[i], TemplateManager.class, true);
        }
        return result;
    }
}
