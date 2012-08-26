package com.alcshare.docs.servlets;

import com.alcshare.docs.DocumentManager;
import com.alcshare.docs.DocumentReference;
import com.alcshare.docs.util.AddOnFiles;
import com.alcshare.docs.util.Logging;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

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
    private static final String CONFIG_PROPERTIES = "/WEB-INF/velocityconfig.properties";
    private static final String DEFAULT_TEMPLATE = "/com/alcshare/docs/templates/default.vm";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        loadVelocity(config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Template t = Velocity.getTemplate(DEFAULT_TEMPLATE);
        VelocityContext context = new VelocityContext();
        List<DocumentReference> references = DocumentManager.INSTANCE.getReferences(req);
        context.put("documents", references);
        t.merge(context, resp.getWriter());
    }


    private void loadVelocity(ServletContext context) {
        if (!velocityInitialized) {
            InputStream stream = context.getResourceAsStream(CONFIG_PROPERTIES);
            if (stream == null) {
                Logging.println("ERROR: Can't find velocity configuration resource at '"+CONFIG_PROPERTIES+"'");
                return;
            }
            Properties p = new Properties();
            try {
                p.load(stream);
                p.setProperty("file.resource.loader.path", AddOnFiles.getTemplatesDirectory().getCanonicalPath());
                Velocity.init( p );
                velocityInitialized = true;
                Logging.println("Velocity subsystem initialized");
            } catch (IOException e) {
                Logging.println("Error initializing velocity properties", e);
            }
        }
    }
}
