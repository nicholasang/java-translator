package edu.nyu.oop;
import java.util.ArrayList;

@Deprecated
public class ValueWrapper
{
    private Object o;
    private boolean noPrint;

    ValueWrapper(Object o)
    {
        this.o = o;
    }

    ValueWrapper(Object o, boolean noPrint)
    {
        this.o = o;
        this.noPrint = noPrint;

    }

    public Object get()
    {
        return o;
    }

    public void set(Object o)
    {
        this.o = o;
    }

    @Override
    public String toString()
    {
        return (this.noPrint) ? Integer.toString(((ArrayList<ValueWrapper>)o).size()) : o.toString();
    }

}
