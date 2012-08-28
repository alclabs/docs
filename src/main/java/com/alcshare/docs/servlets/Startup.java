package com.alcshare.docs.servlets;

import com.alcshare.docs.DocumentManager;
import com.alcshare.docs.MimeManager;
import com.alcshare.docs.util.AddOnFiles;
import com.alcshare.docs.util.Logging;
import com.controlj.green.addonsupport.access.DirectAccess;
import com.controlj.green.addonsupport.access.SystemConnection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

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

        if (!configFile.exists())
        {
            createDefaultConfig(configFile);
        }

        MimeManager.initialize();  // create default mime type file if needed

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

    private void createDefaultConfig(File configFile)
    {
        SystemConnection connection = DirectAccess.getDirectAccess().getRootSystemConnection();
        //todo - build default file
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {

    }
}
