package com.alcshare.docs.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * This class abstracts a File that, if not present, can have it's default copied from
 * a URL (i.e. from the .jar, or anywhere else that can be identified by a URL).
 */
public class FileResource {
    private final File resourceFile;
    private final URL defaultResourceURL;
    private final boolean isText;

    /**
     * Use this constructor to have the most flexibility regarding where the default
     * file comes from.
     * @param resourceFile The file on disk which this instance represents.
     * @param defaultResourceURL The URL to the default file contents.
     * @param isText true if the resource is a text file
     */
    public FileResource(File resourceFile, URL defaultResourceURL, boolean isText) {
        this.resourceFile = resourceFile;
        this.defaultResourceURL = defaultResourceURL;
        this.isText = isText;
    }

    /**
     * Use this constructor if the default file comes from a classloader.  The default
     * file should be named the same as the resource file, and should be on the classpath
     * in the same package as the resource class.
     * @param resourceFile The file on disk which this instance represents.
     * @param resourceClass A class whose classloader & package is used to find the default file.
     * @param isText true if the resource is a text file
     */
    public FileResource(File resourceFile, Class resourceClass, boolean isText) {
        this(resourceFile, resourceClass.getResource(resourceFile.getName()), isText);
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
     * @param isText true if the resource is a text file
     */
    public FileResource(File resourceDir, String resourceFilename, Class resourceClass, boolean isText) {
        this(new File(resourceDir, resourceFilename), resourceClass, isText);
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

    public void extractIfNeeded() throws IOException {
        if (!resourceFile.exists()) {
            try {
                extractResource();
            } catch (IOException e) {
                throw new IOException("Error writing default file "+resourceFile.getAbsolutePath(), e);
            }
        }
    }

    public static Collection<FileResource> getFileResourcesBeneathContextPath(ServletContext context,
                                                       String contextPath, File targetDir, boolean isText) {
        List<FileResource> result = new ArrayList<FileResource> ();
        Set<String> resourcePaths = context.getResourcePaths(contextPath);
        for (String path : resourcePaths) {
            if (!path.startsWith(contextPath)) {
                Logging.println("Error getting resource - unexpected path from context.getResourcePaths.  Returned '"+
                    path+"' from '"+ contextPath+"'");
                continue;
            }
            int index = contextPath.length();

            String name = path.substring(index + 1);

            try {
                result.add(new FileResource(new File(targetDir, name),
                        context.getResource(path), isText));
            } catch (MalformedURLException e) {
                Logging.println("Error getting resource", e);
            }
        }
        return result;
    }

    public static void extractIfNeeded(Collection<FileResource> collection) throws IOException {
        for (FileResource fileResource : collection) {
            fileResource.extractIfNeeded();
        }
    }

    private void extractResource() throws IOException {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = defaultResourceURL.openStream();
            os = new FileOutputStream(resourceFile);
            if (isText) {
                copyTextFile(is, resourceFile);
            } else {
                IOUtils.copy(is, os);
            }
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    /**
     * Copies the contents of the input stream to a target text file utilizing the system specific line endings.
     * @param in - source InputStream
     * @param target - file to copy into
     */

    // yes these parameters are a little asymmetric, but it makes it easier to get the file name in case of error
    private static void copyTextFile (InputStream in, File target) {
        Reader src = null;
        PrintWriter dest = null;
        try {
            src = new InputStreamReader(in);
            dest = new PrintWriter(target);
            LineIterator li = IOUtils.lineIterator(src);
            while (li.hasNext()) {
                String line = li.nextLine();
                dest.println(line);
            }
        } catch (Exception ex) {
            Logging.println("Error copying default file '"+target.getName()+"'", ex);
        } finally {
            IOUtils.closeQuietly(src);
            IOUtils.closeQuietly(dest);
        }

    }



}
