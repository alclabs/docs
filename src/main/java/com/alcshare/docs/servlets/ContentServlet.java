package com.alcshare.docs.servlets;

import com.alcshare.docs.DocumentManager;
import com.alcshare.docs.DocumentReference;
import com.alcshare.docs.util.AddOnFiles;
import com.alcshare.docs.util.Logging;
import com.controlj.green.addonsupport.AddOnInfo;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 *
 */
public class ContentServlet extends HttpServlet{
    private static boolean velocityInitialized = false;
    private static final String DEFAULT_TEMPLATE = "default.vm";
    private static final String PARAM_STYLE = "style";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        loadVelocity(config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String style = req.getParameter(PARAM_STYLE);
        Template t;
        if (style == null) {
            style = DEFAULT_TEMPLATE;
        }
        try {
            t = Velocity.getTemplate(style);
            VelocityContext context = new VelocityContext();
            List<DocumentReference> references = DocumentManager.INSTANCE.getReferences(req);
            context.put("documents", references);
            context.put("addonName", AddOnInfo.getAddOnInfo().getName());
            t.merge(context, resp.getWriter());
        } catch (Exception e) {
            Logging.println("Error getting template for requested style '"+style+"'", e);
            RequestDispatcher dispatcher = req.getSession().getServletContext().getRequestDispatcher("/WEB-INF/error.jsp");
            req.setAttribute("style", style);
            dispatcher.include(req, resp);
        }
    }


    private void loadVelocity(ServletContext context) {
        if (!velocityInitialized) {
            Properties p = new Properties();
            try {
                p.setProperty("file.resource.loader.path", AddOnFiles.getTemplatesDirectory().getCanonicalPath());
                Velocity.init( p );
                velocityInitialized = true;
            } catch (IOException e) {
                Logging.println("Error initializing velocity properties", e);
            }
        }
    }
}
