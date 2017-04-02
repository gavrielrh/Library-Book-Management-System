/**
 * Tests end visit request with success/failures
 * @author - Brendan Jones (bpj1651@rit.edu)
 */

/* imports */
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Date;

public class TestEndVisit {

    /* invoker is part of the test to test the handleCommand method */
    private SystemInvoker invoker;

    /* systemDate required for repsonse */
    private Date systemDate;

    private String alreadyVistingVisitorId;

    private String notVisitingVisitorId;

    private TestUtil testUtil;

    /**
     * setUp the test with the LBMS and invoker created.
     * Also get the time for response formatting.
     */
    @Before
    public void setUp(){
        this.testUtil = new TestUtil();
        this.invoker = this.testUtil.getInvoker();
        this.invoker.getLBMS().setTime((long)1.5341688e+12);
        this.alreadyVistingVisitorId = this.testUtil.registerVisitor();
        this.testUtil.arriveVisitor(this.alreadyVistingVisitorId);
        this.notVisitingVisitorId = this.testUtil.registerSecond();
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
    public void testSuccessWithLibraryClosing(){
        String visitorId = this.testUtil.registerThird();
        this.testUtil.arriveVisitor(visitorId);
        this.invoker.handleCommand("advance,1;");
        Visit visit = this.invoker.getLBMS().getVisits().get(this.invoker.getLBMS().getVisits().size()-1);
        //From 10am to 7pm is 9 hours which is 32400000 ms.
        long timeLibraryIsOpen = 32400000;
        assertEquals(timeLibraryIsOpen, visit.getVisitDuration());
    }

    @Test
    public void testInvalidId(){
        String response = this.invoker.handleCommand("depart," + this.notVisitingVisitorId + ";");
        String expected = "arrive,invalid-id;";
        assertEquals(expected, response);
    }

}

