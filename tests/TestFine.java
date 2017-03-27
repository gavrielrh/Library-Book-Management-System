/**
 * TODO: tests begin visit request and possible successes/failures
 * @author - Brendan Jones (bpj1651@rit.edu)
 */

import org.junit.*;
import static org.junit.Assert.*;
import java.io.InputStream;

import java.util.Date;

public class TestFine {

    /* invoker is part of the test to test the handleCommand method */
    private SystemInvoker invoker;

    /* systemDate required for repsonse */
    private Date systemDate;

    private Visitor visitor;

    /**
     * setUp the test with the LBMS and invoker created.
     * Also get the time for response formatting.
     */
    @Before
    public void setUp(){
        LBMS system = SystemInvoker.startUp();
        this.invoker = new SystemInvoker(system);
        //Set the time initially to 10 am
        this.systemDate = new Date((long)1.5341688e+12);
        String visitorId = this.invoker.handleCommand("register,first,last,sample address,12345632132;").split(",")[1];
        this.visitor = this.invoker.getLBMS().getVisitor(visitorId);
        this.invoker.handleCommand("arrive" + this.visitor.getUniqueId() + ";");
        this.invoker.handleCommand("search,*,{J.K. Rowling};");
        this.invoker.handleCommand("buy,1,0;");
        this.invoker.handleCommand("info,*,{J.K. Rowling},book-status");
        this.invoker.handleCommand("borrow,1;" + this.visitor.getUniqueId() + ",");

    }

}

