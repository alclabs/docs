package com.alcshare.docs;

import com.alcshare.docs.util.AddOnFiles;
import com.alcshare.docs.util.Logging;
import com.controlj.green.addonsupport.AddOnInfo;
import com.controlj.green.addonsupport.access.ActionExecutionException;
import com.controlj.green.addonsupport.access.DirectAccess;
import com.controlj.green.addonsupport.access.SystemConnection;
import com.controlj.green.addonsupport.access.SystemException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class Startup implements ServletContextListener
{
    private static final String CONFIG_NAME = "docs.csv";
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        File configFile = new File(AddOnFiles.getConfigBaseFile(), CONFIG_NAME);

        if (!configFile.exists())
        {
            createDefaultConfig(configFile);
        }

        try {
            DocumentManager.INSTANCE.loadConfiguration(configFile);
        } catch (Exception e) {
            Logging.println("Error parsing configuration file", e);
        }

        AddOnFiles.getDocBaseFile();  // just to make sure the directory exists
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
