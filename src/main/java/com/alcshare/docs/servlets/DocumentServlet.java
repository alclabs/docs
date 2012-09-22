package com.alcshare.docs.servlets;

import com.alcshare.docs.MimeManager;
import com.alcshare.docs.util.AddOnFiles;
import com.alcshare.docs.util.Logging;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 */
public class DocumentServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File docFile = getDocFile(req.getPathInfo());
        if (!docFile.canRead()) {
            resp.sendError(404, "File "+docFile+" cannot be read");
            return;
        }
        String mime = MimeManager.getMimeTypeForExtension(getExtension(docFile));
        resp.setContentType(mime);

        String title = req.getParameter("title");
        if (title != null) {
            resp.setHeader("Content-Disposition","inline; filename=TEST");
        }

        try {
            resp.setContentLength((int)docFile.length());

            ServletOutputStream outputStream = resp.getOutputStream();
            FileInputStream inputStream = new FileInputStream(docFile);
            try {
                IOUtils.copy(inputStream, outputStream);
                outputStream.flush();
            }
            finally {
                inputStream.close();
            }
        } catch (IOException e) {
            Logging.println("Error copying file from '"+req.getPathInfo()+"'", e);
        }
    }

    @Override protected long getLastModified(HttpServletRequest req) {
        File docFile = getDocFile(req.getPathInfo());
        long lm = docFile.lastModified();
        if (lm == 0L) {
            lm = System.currentTimeMillis();
        }
        return lm;
    }

    private static String getExtension(File docFile) {
        String name = docFile.getName();
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex >= 0) {
            return name.substring(dotIndex+1);
        } else {
            return "";
        }
    }

    private static File getDocFile(String docPath) {
        File docBaseFile = AddOnFiles.getDocDirectory();
        return new File(docBaseFile,docPath);
    }
}
