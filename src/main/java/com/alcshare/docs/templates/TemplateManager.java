package com.alcshare.docs.templates;

import com.alcshare.docs.util.LineCopy;
import com.alcshare.docs.util.Logging;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;

/**
 *
 */
public class TemplateManager {
    private static String[] templates = new String[] {"default.vm"};

    public static void copyDefaultTemplates(File targetDirectory) {
        for (String template : templates) {
            File target = new File(targetDirectory, template);
            if (!target.exists()) {
                LineCopy.copy(TemplateManager.class.getResourceAsStream(template), target);
            }
        }
    }
}
