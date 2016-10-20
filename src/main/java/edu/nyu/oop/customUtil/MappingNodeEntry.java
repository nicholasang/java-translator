package edu.nyu.oop.customUtil;
import java.util.*;
import xtc.tree.GNode;

public abstract class MappingNodeEntry
{
    private MappingNodeEntry(){}

    public static MappingNodeEntry createDataFieldList()
    {
        return new DataFieldList();
    }

    public static MappingNodeEntry createNodeEntry(GNode node)
    {
        try
        {
            return new NodeEntry(node);
        }
        catch(NoSuchElementException ex)
        {
            return null;
        }
    }


    public static class DataFieldList extends MappingNodeEntry
    {
        private List<String> dataFields;
        private boolean allMarker;

        public DataFieldList()
        {
            this.dataFields = new ArrayList<String>();
        }

        public DataFieldList(String toAdd)
        {
            this();
            this.dataFields.add(toAdd);
        }

        public DataFieldList(ArrayList<String> dfl)
        {
            this.dataFields = dfl;
        }

        public void append(String toAdd)
        {
            this.dataFields.add(toAdd);
        }

        public void append(ArrayList<String> toAdd)
        {
            for(String val : toAdd)
            {
                this.dataFields.add(val);
            }
        }

        public String getFirst()
        {
            return this.dataFields.get(0);
        }

        public String getAt(int i)
        {
            return this.dataFields.get(i);
        }

        public List<String> getList()
        {
            return this.dataFields;
        }

        public void setAsAllMarker()
        {
            this.allMarker = true;
        }

        @Override
        public String toString()
        {
            if(this.allMarker)
            {
                return "[" + Integer.toString(this.dataFields.size()) + "]";
            }

            return this.dataFields.toString();
        }
    }

    public static class NodeEntry extends MappingNodeEntry
    {
        private GNode node;

        private NodeEntry(){}

        public NodeEntry(GNode node) throws NoSuchElementException
        {
            if(node == null) throw new NoSuchElementException();

            this.node = node;
        }

        public GNode getNode()
        {
            return this.node;
        }
    }
}

