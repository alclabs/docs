package com.alcshare.docs;

import com.controlj.green.addonsupport.AddOnInfo;
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
    private static File docBaseFile;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File docFile = getDocFile(req.getPathInfo());
        String mime = getMimeType(getExtension(docFile));
        resp.setContentType(mime);


        ServletOutputStream outputStream = resp.getOutputStream();
        IOUtils.copy(new FileInputStream(docFile), outputStream);
    }

    private static File getDocFile(String docPath) {
        if (docBaseFile  == null) {
            AddOnInfo aoi = AddOnInfo.getAddOnInfo();
            File publicFile = aoi.getPublicDir();
            String name = aoi.getName();
            File systemFile = publicFile.getParentFile().getParentFile();
            String systemName = systemFile.getName();
            String relPath = systemName+"/webapp_data/"+name+"/private/docs/";
            docBaseFile = new File(systemFile, relPath);
            docBaseFile.mkdirs();
        }
        return new File(docBaseFile,docPath);
    }

    private static String getExtension(File docFile) {
        String name = docFile.getName();
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex >= 0) {
            return name.substring(dotIndex);
        } else {
            return "";
        }
    }

    private static String getMimeType(String extension) {
        if (extension.equalsIgnoreCase("pdf")) {
            return "application/pdf";
        } else if (extension.equalsIgnoreCase("html")) {
            return "text/html";
        } else if (extension.equalsIgnoreCase("css")) {
            return "text/css";
        } else if (extension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        return "text/plain";
    }


}
