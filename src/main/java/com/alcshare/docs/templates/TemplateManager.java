package com.alcshare.docs.templates;

import com.alcshare.docs.util.AddOnFiles;
import com.alcshare.docs.util.FileResource;
import com.alcshare.docs.util.Logging;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.Collection;

/**
 *
 */
public class TemplateManager {
    public static void initialize(ServletContext context) {
        Collection<FileResource> templates = FileResource.getFileResourcesBeneathContextPath(context,
                 "/WEB-INF/templates/", AddOnFiles.getTemplatesDirectory(), true);

        try {
            FileResource.extractIfNeeded(templates);
        } catch (IOException e) {
            Logging.println("Error getting template ", e);
        }
    }
}
