package com.alcshare.docs;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.alcshare.docs.util.Logging;
import com.controlj.green.addonsupport.InvalidConnectionRequestException;
import com.controlj.green.addonsupport.access.*;
import com.controlj.green.addonsupport.web.WebContext;
import com.controlj.green.addonsupport.web.WebContextFactory;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 *
 */
public enum DocumentManager {
    INSTANCE;

    private static String[] defaultHeader = new String[]{"refpath", "disppath", "title", "docpath"};

    private final HashMap<String,List<DocumentReference>> docRefs = new HashMap<String,List<DocumentReference>>();
    public void loadConfiguration(File configFile) throws IOException, SystemException, ActionExecutionException {
        SystemConnection connection = DirectAccess.getDirectAccess().getRootSystemConnection();
        loadConfigurationInternal(new FileReader(configFile), connection);
    }

    private void loadConfigurationInternal(Reader configReader, SystemConnection connection) throws IOException, SystemException, ActionExecutionException {
        connection.runReadAction(new LoadConfigurationAction(configReader, docRefs));
    }

    private static class LoadConfigurationAction implements ReadAction {
        private CSVReader reader;
        private HashMap<String, List<DocumentReference>> docRefs;
        private String[] customColumns = new String[0];

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
                if (nextLine.length >= defaultHeader.length) {
                    DocumentReference ref = new DocumentReference(nextLine[0], nextLine[2], nextLine[3],
                            findLocation(access, nextLine[0]),
                            loadExtraColumns(nextLine));
                    addRef(ref);
                }
            }
        }

        private Map<String,String> loadExtraColumns(String[] line) {
            if (line.length > defaultHeader.length) {
                HashMap<String,String> result = new HashMap<String, String>();
                for (int i=defaultHeader.length; i<line.length; i++) {
                    result.put(customColumns[i-defaultHeader.length], line[i]);
                }
                return result;
            } else {
                return Collections.emptyMap();
            }
        }

        private void validateHeaders(String[] headers) throws Exception {
            if (headers != null) {
                if (headers.length >= defaultHeader.length) {
                    if (arrayStartsWith(headers, defaultHeader)) {
                        customColumns = new String[headers.length - defaultHeader.length];
                        for (int i=defaultHeader.length; i<headers.length; i++) {
                            customColumns[i-defaultHeader.length] = headers[i];
                        }
                        return;
                    }
                }
            }

            throw new Exception("Invalid config file header.  Headers should be refpath,disppath,title,docpath");
        }

        private boolean arrayStartsWith(String[] test, String[] starts) {
            if (test.length < starts.length) { return false; }

            for (int i=0; i<starts.length; i++) {
                if (!test[i].equals(starts[i])) { return false; }
            }

            return true;
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

    public void saveConfiguration(File configFile) {
        SystemConnection connection = DirectAccess.getDirectAccess().getRootSystemConnection();
        try {
            FileWriter outWriter = new FileWriter(configFile);
            connection.runReadAction(new WriteConfigurationAction(outWriter, docRefs));
            outWriter.close();
        } catch (Exception e) {
            Logging.println("Error writing configuration file", e);
        }
    }

    private static class WriteConfigurationAction implements ReadAction {
        private final CSVWriter output;
        private final HashMap<String, List<DocumentReference>> docRefs;

        public WriteConfigurationAction(Writer output, HashMap<String, List<DocumentReference>> docRefs) {
            this.output = new CSVWriter(output);
            this.docRefs = docRefs;
        }

        @Override
        public void execute(@NotNull SystemAccess access) throws Exception {
            output.writeNext(defaultHeader);
            access.visit(access.getGeoRoot(), new ConfigWriterVisitor());
        }

        private class ConfigWriterVisitor extends Visitor {
            public ConfigWriterVisitor() { super(false); }

            @Override
            public void visit(@NotNull Location location) {
                String lus = location.getPersistentLookupString(true);
                List<DocumentReference> docRefList;
                synchronized (docRefs) {
                    docRefList = docRefs.get(lus);
                }
                if (docRefList != null) {
                    // todo - insert existing stuff
                } else {
                    writeDefaultRow(location);
                }
            }

            private void writeDefaultRow(Location location) {

                output.writeNext(new String[] { getGQLPath(location), getFullDisplayPath(location) });
            }

            private String getGQLPath(Location location) {
                String referenceName = location.getReferenceName();
                if (referenceName.startsWith("#")) {
                    return referenceName;
                } else if (location.hasParent()) {
                    try {
                        return getGQLPath(location.getParent()) + "/" + referenceName;
                    } catch (UnresolvableException e) {
                        Logging.println("Unexpected exception while creating GQL path", e);
                        return "";
                    }
                } else {
                    return "/trees/geographic";
                }
            }

            private String getFullDisplayPath(Location location) {
                String displayName = location.getDisplayName();
                if (location.hasParent()) {
                    try {
                        return getFullDisplayPath(location.getParent()) + "/" + displayName;
                    } catch (UnresolvableException e) {
                        Logging.println("Error formatting display path", e);
                        return "";
                    }
                } else {
                    return displayName;
                }

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
