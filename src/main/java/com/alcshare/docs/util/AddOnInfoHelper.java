package com.alcshare.docs.util;

import com.controlj.green.addonsupport.AddOnInfo;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class AddOnInfoHelper
{
    private static final AtomicReference<String> addonNameRef = new AtomicReference<String>();
    private static final AtomicReference<File> addonPublicDirRef = new AtomicReference<File>();

    public static String getAddonName()
    {
        if (addonNameRef.get() == null)
            collectAddOnInfo();
        return addonNameRef.get();
    }

    public static File getPublicDir()
    {
        if (addonPublicDirRef.get() == null)
            collectAddOnInfo();
        return addonPublicDirRef.get();
    }

    private static void collectAddOnInfo()
    {
        AddOnInfo addOnInfo = AddOnInfo.getAddOnInfo();
        addonNameRef.set(addOnInfo.getName());
        addonPublicDirRef.set(addOnInfo.getPublicDir());
    }
}
