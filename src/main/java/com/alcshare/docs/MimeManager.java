package com.alcshare.docs;

import com.alcshare.docs.util.AddOnFiles;
import com.alcshare.docs.util.FileResource;
import com.alcshare.docs.util.Logging;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Properties;

/**
 *
 */
public class MimeManager {
    private static final String DEFAULT_MIME_TYPE = "text/plain";
    private static final FileResource MIME_CONFIG = new FileResource(AddOnFiles.getConfigDirectory(), "mime.properties", MimeManager.class, true);

    private static final Properties mappings = new Properties();

    public static synchronized String getMimeTypeForExtension(String extension) {
        String result = (String) mappings.get(extension);
        return result != null ? result : DEFAULT_MIME_TYPE;
    }

    private static synchronized void loadMimeTypes() {
        mappings.clear();

        InputStream is = null;
        try {
            is = MIME_CONFIG.openBufferedStream();
            mappings.load(is);
        } catch (IOException e) {
            Logging.println("Error loading "+MIME_CONFIG, e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public static void initialize() {
        loadMimeTypes();
    }
}
