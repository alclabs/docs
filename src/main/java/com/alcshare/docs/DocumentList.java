package com.alcshare.docs;

import com.alcshare.docs.util.Logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class DocumentList extends ArrayList<DocumentReference> {
    public Map<String,DocumentList> groupBy(String columnName) {
        HashMap<String,DocumentList> result = new HashMap<String, DocumentList>();

        for (DocumentReference next : this) {
            String val = next.get(columnName).toString();
            if (val != null) {
                addReference(result, val, next);
            } else {
                Logging.println("Can't group by column named '"+columnName+"'");
            }
        }

        return result;
    }

    private static void addReference(HashMap<String,DocumentList> map, String key, DocumentReference reference) {
        DocumentList list = map.get(key);
        if (list == null) {
            list = new DocumentList();
            map.put(key, list);
        }
        list.add(reference);
    }
}
