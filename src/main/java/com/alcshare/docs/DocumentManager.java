package com.alcshare.docs;

import au.com.bytecode.opencsv.CSVReader;
import com.alcshare.docs.util.Logging;
import com.controlj.green.addonsupport.InvalidConnectionRequestException;
import com.controlj.green.addonsupport.access.*;
import com.controlj.green.addonsupport.web.WebContext;
import com.controlj.green.addonsupport.web.WebContextFactory;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public enum DocumentManager {
    INSTANCE;

    private final HashMap<String,List<DocumentReference>> docRefs = new HashMap<String,List<DocumentReference>>();
    public synchronized void loadConfiguration(File configFile) throws IOException, SystemException, ActionExecutionException {
        SystemConnection connection = DirectAccess.getDirectAccess().getRootSystemConnection();
        loadConfigurationInternal(new FileReader(configFile), connection);
    }

    private void loadConfigurationInternal(Reader configReader, SystemConnection connection) throws IOException, SystemException, ActionExecutionException {
        connection.runReadAction(new LoadConfigurationAction(configReader, docRefs));
    }

    private static class LoadConfigurationAction implements ReadAction {
        private CSVReader reader;
        private HashMap<String, List<DocumentReference>> docRefs;

        public LoadConfigurationAction(Reader reader, HashMap<String,List<DocumentReference>> docRefs) {
            this.reader = new CSVReader(reader);
            this.docRefs = docRefs;
        }

        @Override
        public void execute(@NotNull SystemAccess access) throws Exception {
            String[] headers = reader.readNext();
            validateHeaders(headers);

            clearConfiguration();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                DocumentReference ref = new DocumentReference(nextLine[0], nextLine[1], nextLine[2], findLocation(access, nextLine[0]));
                addRef(ref);
            }
        }

        private void validateHeaders(String[] headers) throws Exception {
            if (headers != null) {
                if (headers.length >= 3) {
                    if (headers[0].equals("refpath") &&
                        headers[1].equals("title") &&
                        headers[2].equals("docpath")) {
                        return;
                    }
                }
            }

            throw new Exception("Invalid config file header.  Header should be ");
        }

        private Location findLocation(SystemAccess access, String refPath) throws UnresolvableException {
            return access.resolveGQLPath(refPath);
        }

        private void addRef(DocumentReference ref) {
            Location loc = ref.getLocation();
            String luString = loc.getPersistentLookupString(true);

            List<DocumentReference> refList;
            synchronized (docRefs) {
                refList = docRefs.get(luString);
                if (refList == null) {
                    refList = new ArrayList<DocumentReference>();
                    docRefs.put(luString, refList);
                }
            }
            refList.add(ref);
        }

        private void clearConfiguration() {
            synchronized (docRefs) {
                docRefs.clear();
            }
        }
    }

    public List<DocumentReference> getReferences(final HttpServletRequest request) {
        try {
            SystemConnection connection = DirectAccess.getDirectAccess().getUserSystemConnection(request);

            return connection.runReadAction(new ReadActionResult<List<DocumentReference>>() {
                @Override
                public List<DocumentReference> execute(@NotNull SystemAccess access) throws Exception {
                    List<DocumentReference> result = Collections.emptyList();
                    try {
                        if (WebContextFactory.hasLinkedWebContext(request)) {
                            WebContext context = WebContextFactory.getLinkedWebContext(request);
                            Location location = context.getLinkedFromLocation(access.getTree(SystemTree.Geographic));

                            result = getReferencesForLocation(location.getPersistentLookupString(true), docRefs);
                        }
                    } catch (UnresolvableException e) {  } // ignore and return empty list

                    return result;
                }
            });
        } catch (Exception e) {
            Logging.println("Error getting document references", e);
            return Collections.emptyList();
        }
    }

    private static List<DocumentReference> getReferencesForLocation(String locationString, HashMap<String,List<DocumentReference>> docRefs) {
        List<DocumentReference> result = Collections.emptyList();

        List<DocumentReference> referenceList;
        synchronized (docRefs) {
            referenceList = docRefs.get(locationString);
        }
        if (referenceList != null) {
            result = referenceList;
        }
        return result;
    }
}
