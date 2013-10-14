package com.alcshare.docs;

import com.alcshare.docs.util.Logging;

import java.util.*;

/**
 *
 */
public class DocumentList extends ArrayList<DocumentReference> {
    public Map<String,DocumentList> groupBy(String columnName) {
        int index = 0;
        HashMap<String, Integer> indexes = new HashMap<String, Integer>();

        /*
        Need to use the TreeMap and a custom comparator to make sure that the keys are ordered in the
        order that the grouping values are encountered in the configuration file
         */
        TreeMap<String,DocumentList> result = new TreeMap<String, DocumentList>(new GroupComparator(indexes));

        for (DocumentReference next : this) {
            String val = next.get(columnName).toString();
            if (val != null) {
                if (!indexes.containsKey(val)) {
                    indexes.put(val, index++);
                }
                addReference(result, val, next);
            } else {
                Logging.println("Can't group by column named '"+columnName+"'");
            }
        }

        return result;
    }

    private static void addReference(Map<String,DocumentList> map, String key, DocumentReference reference) {
        DocumentList list = map.get(key);
        if (list == null) {
            list = new DocumentList();
            map.put(key, list);
        }
        list.add(reference);
    }

    private class GroupComparator implements Comparator<String> {
        HashMap<String, Integer> indexes;
        public GroupComparator(HashMap<String, Integer> indexes) {
            this.indexes = indexes;
        }

        @Override
        public int compare(String s1, String s2) {
            Integer index1 = indexes.get(s1);
            Integer index2 = indexes.get(s2);
            if (index1 != null && index2 != null) {
                 return (index1.intValue() - index2.intValue());
            }
            Logging.println("got an unknown group from either "+s1+" or "+s2);
            return -1;
        }
    }
}
