package edu.nyu.oop;

import xtc.tree.Node;
import xtc.tree.GNode;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * Created by alex on 10/27/16.
 */
public class printOutputCpp extends xtc.tree.Visitor{

    File output = null;

    public printOutputCpp(File outFile){
        output = outFile;
    }

    public void visit(Node n){
        for (Object o: n){
            if (o instanceof Node) dispatch((Node) o);
        }
    }

    public void visitDimensions(GNode n){
        String dimensions = (String) n.get(0);
        for (int i = 0; i < dimensions.length(); i++){
            if (dimensions.charAt(i) == '['){
                //TODO print to file []
            }
        }
    }


}
