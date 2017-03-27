/**
 * Tests end visit request with success/failures
 * @author - Brendan Jones (bpj1651@rit.edu)
 */

import org.junit.*;
import static org.junit.Assert.*;
import java.io.InputStream;

import java.util.Date;

public class TestEndVisit {

    /* invoker is part of the test to test the handleCommand method */
    private SystemInvoker invoker;

    /* systemDate required for repsonse */
    private Date systemDate;

    private String alreadyVistingVisitorId;

    private String notVisitingVisitorId;

    /**
     * setUp the test with the LBMS and invoker created.
     * Also get the time for response formatting.
     */
    @Before
    public void setUp(){
        LBMS system = SystemInvoker.startUp();
        this.invoker = new SystemInvoker(system);
        this.systemDate = system.getTime();
        this.invoker.getLBMS().setTime((long)1.5341688e+12);
        this.alreadyVistingVisitorId = this.invoker.handleCommand("register,sampleFirst,sampleLast," +
                "sample address,1234561223;").split(",")[1];
        this.invoker.handleCommand("arrive," + this.alreadyVistingVisitorId + ";");
        this.notVisitingVisitorId = this.invoker.handleCommand("register,first,last,sample address,12345632132;").split(",")[1];
    }

    @Test
    public void testSuccessWithoutTime(){
        String response = this.invoker.handleCommand("depart," + this.alreadyVistingVisitorId + ";");
        String expected = "depart," + this.alreadyVistingVisitorId + "," +
                LBMS.timeFormatter.format(this.invoker.getLBMS().getTime()) + "," + "00:00:00" + ";";
        assertEquals(expected, response);
    }


    @Test
    public void testSuccessWithTime(){
        this.invoker.handleCommand("advance,0,1;");
        String response = this.invoker.handleCommand("depart," + this.alreadyVistingVisitorId + ";");
        String expected = "depart," + this.alreadyVistingVisitorId + "," +
                LBMS.timeFormatter.format(this.invoker.getLBMS().getTime()) + "," + "01:00:00" + ";";
        assertEquals(expected, response);
    }

    @Test
    public void testInvalidId(){
        String response = this.invoker.handleCommand("depart," + this.notVisitingVisitorId + ";");
        String expected = "arrive,invalid-id;";
        assertEquals(expected, response);
    }


}

