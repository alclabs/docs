package com.alcshare.docs.servlets;

import com.alcshare.docs.DocumentList;
import com.alcshare.docs.DocumentManager;
import com.alcshare.docs.DocumentReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
public class DocumentJSONServlet extends HttpServlet {
    private ServletContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = config.getServletContext();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        String pathInfo = request.getPathInfo();

        if (request.getParameter("reload") != null) {
            Startup.reInitialize(context);
        }

        try {
            if (pathInfo == null) { // no extra path info, must be a request for a refresh
                JSONArray results = new JSONArray();

                /*
                // make fake list for testing
                DocumentList docs = new DocumentList();
                docs.add(new DocumentReference("#ahu1-1","Document Test","/test/test.pdf",DocumentReference.PathType.DOC,"",null, null));
                docs.add(new DocumentReference("#ahu1-2","Document Test 2","/test/Test.txt",DocumentReference.PathType.DOC,"",null, null));
                docs.add(new DocumentReference("#ahu1-3","Document Test 3","",DocumentReference.PathType.DOC,"",null, null));
                */

                DocumentList docs = DocumentManager.INSTANCE.getAllDocuments();

                for (DocumentReference doc : docs) {
                    JSONObject next = new JSONObject();
                    next.put("referencePath", doc.getReferencePath());
                    next.put("title", doc.getTitle());
                    next.put("docPath", doc.getDocPath());
                    next.put("pathType", doc.getPathType().toString());
                    next.put("category", doc.getCategory());
                    next.put("checkDocExists", doc.checkDocExists());

                    results.put(next);
                }

                results.write(response.getWriter());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
