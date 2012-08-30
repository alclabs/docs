package com.alcshare.docs.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;

/**
 * This class provides methods to copy text files and provides the correct line endings on the current platform.
 * Just doing a straight copy with Reader/Writers keeps the source line endings.
 */
public class LineCopy {
    public static void copy (InputStream in, File target) {
        Reader src = null;
        PrintWriter dest = null;
        try {
            src = new InputStreamReader(in);
            dest = new PrintWriter(target);
            LineIterator li = IOUtils.lineIterator(src);
            while (li.hasNext()) {
                String line = li.nextLine();
                Logging.println("line is "+line.length()+" characters long");
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
