package edu.nyu.oop.util;

import xtc.tree.GNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;

//4-Tran
public abstract class MappingNode {
    private MappingNode() {}

    private static ArrayList<Object> entryRepository = new ArrayList<Object>();
    private static LinkedHashMap<String, ArrayList<Object>> entryRepositoryMap = new LinkedHashMap<String, ArrayList<Object>>();

    public static void setEntryRepository(ArrayList<Object> repo) {
        entryRepository = repo;
    }
    public static void setEntryRepositoryMap(LinkedHashMap<String, ArrayList<Object>> repoMap) {
        entryRepositoryMap = repoMap;
    }

    public static ArrayList<Object> getEntryRepository() {
        return entryRepository;
    }

    public static ArrayList<Object> getAllOfType(String key) {
        return entryRepositoryMap.get(key);
    }

    private static void addToEntryRepositoryMap(String key, Object toAdd) {
        ArrayList<Object> entries = entryRepositoryMap.get(key);

        if(entries == null) {
            entries = new ArrayList<Object>();
            entries.add(toAdd);
            entryRepositoryMap.put(key, entries);
        } else {
            entries.add(toAdd);
        }
    }

    //ESSENTIAL METHODS:
    public static GNode createMappingNode(String constructType) {

        GNode construct = GNode.create(constructType);
        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = new LinkedHashMap<String, ArrayList<ArrayList<Integer>>>();


        ArrayList<ArrayList<Integer>> all = new ArrayList<ArrayList<Integer>>();
        all.add(new ArrayList<Integer>());
        all.add(new ArrayList<Integer>());


        dataMap.put("%ALL", all);

        construct.add(new InvisiblePrintObject(dataMap));


        //addToEntryRepositoryMap(construct.getName(), construct);

        return construct;
    }

    public static Object addDataField(GNode node, String fieldNameKey, String value) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();

        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(fieldNameKey);

        ArrayList<ArrayList<Integer>> all = (ArrayList<ArrayList<Integer>>)dataMap.get("%ALL");


        DataField df;

        if (localGlobalIndices == null || localGlobalIndices.size() == 0) {
            int nextAvailID = entryRepository.size();

            df = MappingNode.createDataField(fieldNameKey, value, nextAvailID);

            ArrayList<Integer> localIndices = new ArrayList<Integer>();
            localIndices.add(node.size());
            ArrayList<Integer> globalIndices = new ArrayList<Integer>();
            globalIndices.add(nextAvailID);

            localGlobalIndices = new ArrayList<ArrayList<Integer>>();
            localGlobalIndices.add(localIndices);
            localGlobalIndices.add(globalIndices);

            entryRepository.add(df);

            all.get(0).add(node.size());
            all.get(1).add(nextAvailID);

            dataMap.put(fieldNameKey, localGlobalIndices);


        } else {
            int nextAvailID = entryRepository.size();

            df = MappingNode.createDataField(fieldNameKey, value, nextAvailID);

            localGlobalIndices.get(0).add(node.size());

            localGlobalIndices.get(1).add(nextAvailID);

            entryRepository.add(df);

            all.get(0).add(node.size());
            all.get(1).add(nextAvailID);
        }

        node.add(df);


        addToEntryRepositoryMap(fieldNameKey, df);

        return node;
    }

    public static Object addDataFieldMultiVals(GNode node, String fieldNameKey, ArrayList<String> values) {

        if(node == null)return null;

        Object success = null;
        for(String value : values) {
            if((success = addDataField(node, fieldNameKey, value)) == null)return null;
        }
        return success;
    }

    // TODO: IS A COMMA-DELIMITED STRING GOOD ENOUGH, OR SHOULD I MODIFY SO A LIST OF STRINGS IS CONTAINED, WOULD REQUIRE MORE WORK, BUT DOABLE
    public static Object addDataFieldConcatenatedList(GNode node, String fieldNameKey, ArrayList<String> values) {

        if(node == null)return null;

        Object success = null;
        StringBuilder sb = new StringBuilder();
        int s = 0;
        int bound = values.size() - 1;
        for(; s < bound; ++s) {
            sb.append(values.get(s));
            sb.append(", ");
        }
        sb.append(values.get(s));

        if((success = addDataField(node, fieldNameKey, sb.toString())) == null)return null;

        return success;
    }

    public static GNode createAndLinkDataFieldOneShot(GNode parent, String constructType, String fieldNameKey, String value) {

        if(parent == null)return null;

        GNode construct = createMappingNode(constructType);
        addNode(parent, construct);
        addDataField(construct, fieldNameKey, value);
        return construct;
    }

    public static GNode createAndLinkDataFieldOneShotMultiVals(GNode parent, String constructType, String fieldNameKey, ArrayList<String> values) {

        if(parent == null)return null;

        GNode construct = createMappingNode(constructType);
        addNode(parent, construct);

        for(String value : values) {
            addDataField(construct, fieldNameKey, value);
        }
        return construct;
    }

    public static GNode createAndLinkDataFieldOneShotConcatenatedList(GNode parent, String constructType, String fieldNameKey, ArrayList<String> values) {

        if(parent == null)return null;

        GNode construct = createMappingNode(constructType);
        addNode(parent, construct);

        StringBuilder sb = new StringBuilder();
        int s = 0;
        int bound = values.size() - 1;
        for(; s < bound; ++s) {
            sb.append(values.get(s));
            sb.append(", ");
        }
        sb.append(values.get(s));

        addDataField(construct, fieldNameKey, sb.toString());

        return construct;
    }

    public static Object addNode(GNode node, GNode child) {

        if(node == null || child == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();

        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(child.getName());

        ArrayList<ArrayList<Integer>> all = (ArrayList<ArrayList<Integer>>)dataMap.get("%ALL");


        if(localGlobalIndices == null || localGlobalIndices.size() == 0) {
            int nextAvailID = entryRepository.size();

            ArrayList<Integer> localIndices = new ArrayList<Integer>();
            localIndices.add(node.size());
            ArrayList<Integer> globalIndices = new ArrayList<Integer>();
            globalIndices.add(nextAvailID);

            localGlobalIndices = new ArrayList<ArrayList<Integer>>();
            localGlobalIndices.add(localIndices);
            localGlobalIndices.add(globalIndices);

            entryRepository.add(child);

            all.get(0).add(node.size());
            all.get(1).add(nextAvailID);

            dataMap.put(child.getName(), localGlobalIndices);


        } else {
            int nextAvailID = entryRepository.size();
            localGlobalIndices.get(0).add(node.size());

            localGlobalIndices.get(1).add(nextAvailID);

            entryRepository.add(child);

            all.get(0).add(node.size());
            all.get(1).add(nextAvailID);
        }

        addToEntryRepositoryMap(child.getName(), child);

        node.add(child);

        return node;
    }



    public static Object getInstanceOf(GNode node, String key, int index) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || index >= localGlobalIndices.get(0).size() || index < 0)return null;

        return node.get(localGlobalIndices.get(0).get(index));
    }

    public static ArrayList<Object> getAllLocalInstancesOf(GNode node, String key) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0)return null;

        ArrayList<Object> out = new ArrayList<Object>();

        ArrayList<Integer> localIndices = localGlobalIndices.get(0);
        int len = localIndices.size();
        for(int i = 0; i < len; ++i) {
            out.add(node.get(localIndices.get(i)));
        }

        return out;
    }

    public static ArrayList<DataField> getAllLocalDataFields(GNode node) {
        if(node == null)return null;

        ArrayList<DataField> out = new ArrayList<DataField>();

        int num = node.size();

        for(int i = 1; i < num; ++i) {
            Object o = node.get(i);
            if(o instanceof DataField)out.add((DataField)o);
        }

        return out;

    }

    public static ArrayList<GNode> getAllLocalConstructs(GNode node) {
        if(node == null)return null;

        ArrayList<GNode> out = new ArrayList<GNode>();

        int num = node.size();

        for(int i = 1; i < num; ++i) {
            Object o = node.get(i);
            if(o instanceof GNode)out.add((GNode)o);
        }

        return out;
    }

    public static ArrayList<Object> getAllLocalEntries(GNode node) {
        if(node == null)return null;

        ArrayList<Object> out = new ArrayList<Object>();

        int num = node.size();

        for(int i = 1; i < num; ++i) {
            Object o = node.get(i);
            if(o instanceof DataField || o instanceof GNode)out.add(o);
        }

        return out;
    }

    public static Integer getLocalIndexOf(GNode node, String key, int ithOccurrence) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0 || ithOccurrence >= localGlobalIndices.get(0).size() || ithOccurrence < 0)return null;

        return localGlobalIndices.get(0).get(ithOccurrence);
    }

    public static ArrayList<Integer> getAllLocalIndicesOf(GNode node, String key) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0)return null;

        ArrayList<Integer> out = new ArrayList<Integer>();

        ArrayList<Integer> localIndices = localGlobalIndices.get(0);

        out.addAll(localIndices);

        return out;
    }

    public static Integer getGlobalIndexOf(GNode node, String key, int ithOccurence) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0 || ithOccurence >= localGlobalIndices.get(1).size() || ithOccurence < 0)return null;

        return localGlobalIndices.get(1).get(ithOccurence);
    }

    public static ArrayList<Integer> getAllGlobalIndicesOf(GNode node, String key) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0)return null;

        ArrayList<Integer> out = new ArrayList<Integer>();

        ArrayList<Integer> globalIndices = localGlobalIndices.get(1);

        out.addAll(globalIndices);

        return out;
    }


    public static Object replaceLocalDataFieldValue(GNode node, String key, String value, int ithOccurrence) {

        if(node == null)return null;

        Object o;
        Integer i;
        if((i = (Integer)(getLocalIndexOf(node, key, ithOccurrence))) == null
                || (o = node.get(i)) == null)return null;

        if(o instanceof DataField) {
            ((DataField) o).setVal(value);
            return node;
        }

        return null;
    }

    // NOTE: Better not to use UPDATE: Using this will break the entryRepositoryMap. BE CAREFUL (Please try not to use this)
    @Deprecated
    private static Object replaceNode(GNode node, GNode childReplacement, int ithOccurrence) {

        if(node == null || childReplacement == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();

        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(childReplacement.getName());

        if(localGlobalIndices == null || localGlobalIndices.size() == 0)return null;

        Integer globalIndex;
        if((globalIndex = getGlobalIndexOf(node, childReplacement.getName(), ithOccurrence)) == null)return null;
        entryRepository.set(globalIndex, childReplacement);

        node.set(getLocalIndexOf(node, childReplacement.getName(), ithOccurrence), childReplacement);

        return node;
    }


    public static DataField createDataField(String key, String val, int ID) {
        return new DataField(key, val, ID);
    }

    public static class DataField extends MappingNode {
        private String key;
        private String val;
        private int     ID;

        private static boolean printValOnly;

        public DataField(String key, String val, int ID) {
            this.key = key;
            this.val = val;
            this.ID  =  ID;
        }

        public String getKey() {
            return this.key;
        }
        public void setVal(String val) {
            this.val = val;
        }

        public String getVal() {
            return this.val;
        }

        public int getID() { return this.ID; }

        public static void togglePrintValOnly() { DataField.printValOnly = !DataField.printValOnly; }

        @Override
        public String toString() {
            return (DataField.printValOnly) ? this.val : this.key + " = " + this.val;
        }
    }
}
