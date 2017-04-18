/**
 * TODO: tests begin visit request and possible successes/failures
 * @author - Brendan Jones (bpj1651@rit.edu)
 */

import org.junit.*;
import static org.junit.Assert.*;

public class TestFine {

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
        double fine = this.getFine();
        double expectedFine = 0.0;
        assertEquals(expectedFine, fine, 0.0);
    }

    @Test
    public void testFirstDayOfFine(){
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,1;");
        double fine = this.getFine();
        double expectedFine = 10.0;
        assertEquals(expectedFine, fine, 0.0);

    }

    @Test
    public void testWeekAfterDue(){
        this.invoker.handleCommand("advance,7;");
        this.invoker.handleCommand("advance,1;");
        this.invoker.handleCommand("advance,7;");
        double expectedFine = 12;
        assertEquals(expectedFine, this.getFine(), 0.0);
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
        double expectedFine = 20;
        assertEquals(expectedFine, this.getFine(), 0.0);
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
        double expectedFine = 30;
        assertEquals(expectedFine, this.getFine(), 0.0);
    }
    private double getFine(){
        return this.invoker.getLBMS().getVisitor(this.testUtil.getVisitorId()).getBooksLoaned().get(0).calculateFine();
    }
}

