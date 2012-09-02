package com.alcshare.docs.servlets;

import com.alcshare.docs.DocumentManager;
import com.alcshare.docs.MimeManager;
import com.alcshare.docs.templates.TemplateManager;
import com.alcshare.docs.util.AddOnFiles;
import com.alcshare.docs.util.FileResource;
import com.alcshare.docs.util.Logging;
import org.apache.commons.lang.time.StopWatch;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Set;

/**
 *
 */
public class Startup implements ServletContextListener
{
    private static final String CONFIG_NAME = "docs.csv";
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        File configFile = new File(AddOnFiles.getConfigDirectory(), CONFIG_NAME);

        if (!configFile.exists())  // write config file if needed
        {
            StopWatch sw = new StopWatch();
            sw.start();
            DocumentManager.INSTANCE.saveConfiguration(configFile);
            sw.stop();
            Logging.println("Created new configuration file in "+sw);
        }

        MimeManager.initialize(sce.getServletContext());    // create and load mime types
        TemplateManager.initialize(sce.getServletContext());// copy default templates if needed

        initWebResources(sce.getServletContext());

        if (configFile.exists()) {
            try {
                DocumentManager.INSTANCE.loadConfiguration(configFile);
            } catch (Exception e) {
                Logging.println("Error parsing configuration file", e);
            }
        } else {
            Logging.println("Configuration file '"+configFile.getAbsolutePath()+"' is missing");
        }

        AddOnFiles.getDocDirectory();  // just to make sure the directory exists
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {

    }

    private void initWebResources(ServletContext context) {
        Collection<FileResource> imgFiles = FileResource.getFileResourcesBeneathContextPath(context,
                "/WEB-INF/img", AddOnFiles.getImageDirectory(), false);
        try {
            FileResource.extractIfNeeded(imgFiles);
        } catch (IOException e) {
            Logging.println("Error extracting images", e);
        }
    }
}
