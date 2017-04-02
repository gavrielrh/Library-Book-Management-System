/**
 * TestBeginVisit tests begin visit request and possible successes/failures
 * @author - Brendan Jones (bpj1651@rit.edu)
 */


/* imports */
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Date;

public class TestBeginVisit {

    /* invoker is part of the test to test the handleCommand method */
    private SystemInvoker invoker;

    /* systemDate required for repsonse */
    private Date systemDate;

    private LBMS system;

    private TestUtil testUtil;
    private String alreadyVistingVisitorId;

    private String visitorId;

    /**
     * setUp the test with the LBMS and invoker created.
     * Also get the time for response formatting.
     */
    @Before
    public void setUp(){
        this.testUtil = new TestUtil();
        this.invoker = this.testUtil.getInvoker();
        this.system = this.testUtil.getLbms();
        this.systemDate = this.system.getTime();
        this.alreadyVistingVisitorId = this.testUtil.registerVisitor();
        this.visitorId = this.testUtil.registerSecond();
        this.testUtil.arriveVisitor(this.alreadyVistingVisitorId);
    }

    @Test
    public void testSuccess(){
        String response = this.invoker.handleCommand("arrive," + this.visitorId + ";");
        String expected = "arrive," + this.visitorId + "," + LBMS.dateFormatter.format(this.systemDate) + ","
                + LBMS.timeFormatter.format(this.systemDate) + ";";
        assertEquals(expected, response);
    }

    @Test
    public void testDuplicate(){
        String response = this.invoker.handleCommand("arrive," + this.alreadyVistingVisitorId + ";");
        String expected = "arrive,duplicate;";
        assertEquals(expected, response);
    }

    @Test
    public void testInvalidId(){
        String response = this.invoker.handleCommand("arrive," + "1234567890" + ";");
        String expected = "arrive,invalid-id;";
        assertEquals(expected, response);
    }

    @Test
    public void testMissingParamVisitorId(){
        String response = this.invoker.handleCommand("arrive;");
        String expected = "arrive,missing-parameters,{visitor ID};";
        assertEquals(expected, response);
    }

}

