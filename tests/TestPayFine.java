/**
 * TODO: tests begin visit request and possible successes/failures
 * @author - Brendan Jones (bpj1651@rit.edu)
 */

import org.junit.*;
import static org.junit.Assert.*;

public class TestPayFine{

    /* invoker is part of the test to test the handleCommand method */
    private SystemInvoker invoker;

    private TestUtil testUtil;

    /**
     * setUp the test with the LBMS and invoker created.
     * Also get the time for response formatting.
     */
    @Before
    public void setUp(){
        this.testUtil = new TestUtil();
        this.invoker = this.testUtil.getInvoker();
        this.testUtil.checkOutBook();
    }


    @Test
    public void testRightBeforeFine(){
        //Book is being checked out on the August 13th, 2018
        //due: 20th
        //late: 21st
        //20th: No fee
        //21st: $10 fee
        //28th: $12 fee
        //4th: $14 fee
        //11th: $16 fee
        //18th: $18 fee
        //25th: $20 fee
        //2nd: $22 fee
        //9th: $24 fee
        //16th: $26 fee
        //23rd: $28 fee
        //30th: $30 fee
        //6th: $30 fee



        this.invoker.handleCommand("advance,7;");
        String result = this.invoker.handleCommand("pay," + this.testUtil.getVisitorId() + ",10;");
        String expected = "pay,invalid-amount,10.0,0.0;";
        assertEquals(expected, result);
    }

    @Test
    public void testFirstDayOfFine(){
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,1;");
        String result = this.invoker.handleCommand("pay," + this.testUtil.getVisitorId() + ",6;");
        String expected = "pay,success,4.0;";
        assertEquals(expected, result);
    }

    @Test
    public void testWeekAfterDue(){
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,1;");
        this.invoker.handleCommand("advance,7;");
        String result = this.invoker.handleCommand("pay," + this.testUtil.getVisitorId() + ",8;");
        String expected = "pay,success,4.0;";
        assertEquals(expected, result);
    }

    @Test
    public void testMultipleWeeksAfterDue(){
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,1;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        String result = this.invoker.handleCommand("pay," + this.testUtil.getVisitorId() + ",8;");
        String expected = "pay,success,12.0;";
        assertEquals(expected, result);
    }

    @Test
    public void testCappedFine(){
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,1;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,7;");
        String result = this.invoker.handleCommand("pay," + this.testUtil.getVisitorId() + ",8;");
        String expected = "pay,success,22.0;";
        assertEquals(expected, result);
    }

    @Test
    public void testMultiple(){
        this.testUtil.checkOutSecondBook();
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,1;");
        this.invoker.handleCommand("advance,7;");
        String result = this.invoker.handleCommand("pay," + this.testUtil.getVisitorId() + ",22;");
        String expected = "pay,success,2.0;";
        assertEquals(expected, result);

    }

}

