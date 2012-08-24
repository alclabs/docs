package com.alcshare.docs;

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
        File publicDir = AddOnInfo.getAddOnInfo().getPublicDir();
        File configFile = new File(publicDir, CONFIG_NAME);

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
