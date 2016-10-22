package edu.nyu.oop.customUtil;

public abstract class MappingNodeEntry {
    private MappingNodeEntry() {}

    public static DataField createDataField(String key, String val, int ID) {
        return new DataField(key, val, ID);
    }

    public static class DataField extends MappingNodeEntry {
        private String key;
        private String val;
        private int     ID;

        public DataField(String key, String val, int ID) {
            this.key = key;
            this.val = val;
            this.ID  =  ID;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public String get() {
            return this.val;
        }

        @Override
        public String toString() {
            return this.key + " = " + this.val;
        }
    }
}
