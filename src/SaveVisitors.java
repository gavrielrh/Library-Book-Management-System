/**
 * Filename: SaveVisitors.java
 * SaveVisitors is the class used to Save all Visitor Objects in the LBMS.
 * This is invoked during LBMS shutdown
 * @author - Brendan Jones, bpj1651@rit.edu
 */

/* imports */
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class SaveVisitors implements Saver {

    /* Have all the visitor objects to write be in a Collection<Visitor> */
    private ArrayList<Visitor> visitors;

    /**
     * Constructor for SaveVisitors
     * @param visitors - The Collection of Visitor objects that the class is writing
     */
    public SaveVisitors(ArrayList<Visitor> visitors){
        this.visitors = visitors;
    }

    @Override
    public void writeObjects(){
        try{
            FileOutputStream fileOut = new FileOutputStream("data/LBMS-Visitors.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.visitors);
            out.close();
            fileOut.close();
        }catch(IOException i){
            i.printStackTrace();
        }
    }
}
