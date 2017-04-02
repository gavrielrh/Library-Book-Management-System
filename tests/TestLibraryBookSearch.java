/**
 *
 * @author - Brendan Jones (bpj1651@rit.edu)
 */

import org.junit.*;
import static org.junit.Assert.*;
import java.io.InputStream;

import java.util.Date;

public class TestLibraryBookSearch {

    private TestUtil testUtil;

    /* invoker is part of the test to test the handleCommand method */
    private SystemInvoker invoker;

    private LBMS system;


    /**
     * setUp the test with the LBMS and invoker created.
     * Also get the time for response formatting.
     */
    @Before
    public void setUp(){
        this.testUtil = new TestUtil();
        this.invoker = this.testUtil.getInvoker();
        this.system = this.testUtil.getLbms();
        this.testUtil.searchBooksFromStore();
        this.testUtil.purchaseBooksFromSearch();
    }

    public void testStarTitleStarAuthors(){
        String response = this.invoker.handleCommand("info,*,*;");


    }



}

