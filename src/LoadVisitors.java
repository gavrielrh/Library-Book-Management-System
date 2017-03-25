import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by brendanjones44 on 3/25/17.
 */
public class LoadVisitors implements Loader {
    private ArrayList<Visitor> visitors;

    public LoadVisitors(){

    }

    @SuppressWarnings("unchecked")
    public void LoadObjects(){
        try {
            FileInputStream fileIn = new FileInputStream("data/LBMS-Visitors.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object o = in.readObject();
            if(o instanceof ArrayList){
                this.visitors = (ArrayList<Visitor>)o;
            }
        }catch(IOException i){
            i.printStackTrace();
        }catch(ClassNotFoundException c){
            System.out.println("Visitors class not found");
            c.printStackTrace();

        }

    }

    public ArrayList<Visitor> getVisitors(){
        return this.visitors;
    }
}
