package com.alcshare.docs;

import com.alcshare.docs.util.AddOnFiles;
import com.controlj.green.addonsupport.AddOnInfo;
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
        File configFile = new File(AddOnFiles.getConfigBaseFile(), CONFIG_NAME);

        if (!configFile.exists())
        {
            createDefaultConfig(configFile);
        }
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
