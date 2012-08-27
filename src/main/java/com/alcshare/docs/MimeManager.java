package com.alcshare.docs;

import com.alcshare.docs.util.AddOnFiles;
import com.alcshare.docs.util.Logging;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Properties;

/**
 *
 */
public class MimeManager {
    private static final String DEFAULT_MIME_TYPE = "text/plain";

    private static Properties mappings = new Properties();
    private static final String MIME_CONFIG_NAME = "mime.properties";

    public static synchronized String getMimeTypeForExtension(String extension) {
        String result = null;
        if (mappings != null) {
            result = (String) mappings.get(extension);
        }
        if (result == null) {
            result = DEFAULT_MIME_TYPE;
        }
        return result;
    }

    private static synchronized void loadMimeTypes() {
        File mimeFile = getMimeConfigFile();
        mappings.clear();

        try {
            mappings.load(new FileInputStream(mimeFile));
        } catch (IOException e) {
            Logging.println("Error loading "+mimeFile.getAbsolutePath(), e);
        }
    }

    private static File getMimeConfigFile() {
        return new File(AddOnFiles.getConfigDirectory(), MIME_CONFIG_NAME);
    }

    public static void initialize() {
        File mimeFile = getMimeConfigFile();
        if (!mimeFile.exists()) {
            InputStream is = MimeManager.class.getResourceAsStream(MIME_CONFIG_NAME);
            try {
                FileOutputStream os = new FileOutputStream(mimeFile);
                IOUtils.copy(is, os);
                os.close();
            } catch (IOException e) {
                Logging.println("Error writing default file "+mimeFile.getAbsolutePath(), e);
            }
        }

        loadMimeTypes();
    }
}
