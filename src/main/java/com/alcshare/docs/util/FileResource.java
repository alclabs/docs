package com.alcshare.docs.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;

/**
 * This class abstracts a File that, if not present, can have it's default copied from
 * a URL (i.e. from the .jar, or anywhere else that can be identified by a URL).
 */
public class FileResource {
    private final File resourceFile;
    private final URL defaultResourceURL;

    /**
     * Use this constructor to have the most flexibility regarding where the default
     * file comes from.
     * @param resourceFile The file on disk which this instance represents.
     * @param defaultResourceURL The URL to the default file contents.
     */
    public FileResource(File resourceFile, URL defaultResourceURL) {
        this.resourceFile = resourceFile;
        this.defaultResourceURL = defaultResourceURL;
    }

    /**
     * Use this constructor if the default file comes from a classloader.  The default
     * file should be named the same as the resource file, and should be on the classpath
     * in the same package as the resource class.
     * @param resourceFile The file on disk which this instance represents.
     * @param resourceClass A class whose classloader & package is used to find the default file.
     */
    public FileResource(File resourceFile, Class resourceClass) {
        this(resourceFile, resourceClass.getResource(resourceFile.getName()));
    }

    /**
     * Use this constructor if avoid needing to explicitly put the resourceDir and resourceFilename
     * together in a File object.  This constructor is equivalent to
     * <pre>
     *     new FileResource(new File(resourceDir, resourceFilename), resourceClass);
     * </pre>
     * @param resourceDir The directory where the resource file is located.
     * @param resourceFilename The name of the resource file.
     * @param resourceClass A class whose classloader & package is used to find the default file.
     */
    public FileResource(File resourceDir, String resourceFilename, Class resourceClass) {
        this(new File(resourceDir, resourceFilename), resourceClass);
    }

    public File getFile() throws IOException {
        extractIfNeeded();
        return resourceFile;
    }

    public InputStream openStream() throws IOException {
        return new FileInputStream(getFile());
    }

    public InputStream openBufferedStream() throws IOException {
        return new BufferedInputStream(openStream());
    }

    /**
     * Returns the absolute path of the resource file.  Does NOT indicate the default file URL.
     */
    @Override public String toString() {
        return resourceFile.getAbsolutePath();
    }

    private void extractIfNeeded() throws IOException {
        if (!resourceFile.exists()) {
            try {
                extractResource();
            } catch (IOException e) {
                throw new IOException("Error writing default file "+resourceFile.getAbsolutePath(), e);
            }
        }
    }

    private void extractResource() throws IOException {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = defaultResourceURL.openStream();
            os = new FileOutputStream(resourceFile);
            IOUtils.copy(is, os);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }
}
