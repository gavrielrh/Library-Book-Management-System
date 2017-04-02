/**
 * TODO: tests begin visit request and possible successes/failures
 * @author - Brendan Jones (bpj1651@rit.edu)
 */

import org.junit.*;
import static org.junit.Assert.*;
import java.io.InputStream;

import java.util.Date;

public class TestBookPurchase {

    /* invoker is part of the test to test the handleCommand method */
    private SystemInvoker invoker;

    /* systemDate required for repsonse */
    private Date systemDate;

    /**
     * setUp the test with the LBMS and invoker created.
     * Also get the time for response formatting.
     */
    @Before
    public void setUp(){
        LBMS system = SystemInvoker.startUp();
        this.invoker = new SystemInvoker(system);
        this.systemDate = system.getTime();
    }

    @Test
    public void testZeroBooks(){

    }

    @Test
    public void testOneISBN(){

    }

    @Test
    public void testMultipleISBNs(){

    }



}

