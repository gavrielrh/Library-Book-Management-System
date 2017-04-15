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

    }

    /** Book search requests can be in the following format:
     *   -info,title,{authors},[isbn, [publisher,[sort order]]];
     *
     *   With sort orders:
     *   -title, publish-date, book-status
     *
     */


    /**
     * info,*,*;
     */
    @Test
    public void testStarTitleStarAuthors(){
        this.testUtil.searchBooksFromStore();
        this.testUtil.purchaseBooksFromSearch();
        String response = this.invoker.handleCommand("info,*,*;");
        String expected = "info,5,\n" +
                "10,9780800759490,\"90 Minutes in Heaven\",{Don Piper, Cecil Murphey},Revell,2168-02-09,208,\n" +
                "10,9781440229886,\"100 Sexiest Women in Comics\",{Brent Frankenhoff},Krause Publications,2180-07-11,64,\n" +
                "10,9780486457437,\"1800 Mechanical Movements\",{Gardner Dexter Hiscox},Courier Corporation,2007-01-01,409,\n" +
                "10,9781592533251,\"101 Dog Tricks\",{Kyra Sundance, Chalcy},Quarry,2168-03-04,208,\n" +
                "10,9783836548458,\"75 Years of Marvel Comics\",{Roy Thomas},Taschen,2192-10-11,711;";
        assertEquals(response, expected);

    }

    /**
     * info,*,{Mark Twain};
     */
    @Test
    public void testJustAuthors(){
        this.testUtil.searchBooksFromStore();
        this.testUtil.purchaseBooksFromSearch();
        String response = this.invoker.handleCommand("info,*,{Roy Thomas};");
        String expected = "info,1,\n" +
                "10,9783836548458,\"75 Years of Marvel Comics\",{Roy Thomas},Taschen,2192-10-11,711;";
        assertEquals(response, expected);
    }


    /**
     * info,"Surviving High School",*;
     */
    @Test
    public void testJustTitle(){
        String response = this.invoker.handleCommand("info,'101 Dog Tricks',*;");
        String expected = "info,1,\n" +
                "10,9781592533251,\"101 Dog Tricks\",{Kyra Sundance, Chalcy},Quarry,2168-03-04,208;";
        assertEquals(response, expected);
    }

    /**
     * info,*,*,9780838712474;
     */
    @Test
    public void testJustIsbn(){
        String response = this.invoker.handleCommand("info,*,*,9780800759490;");
        String expected = "info,1,\n" +
                "10,9780800759490,\"90 Minutes in Heaven\",{Don Piper, Cecil Murphey},Revell,2168-02-09,208;";
        assertEquals(response, expected);
    }

    /**
     * info,*,*,*,Cengage Learning;
     */
    @Test
    public void testJustPublisher(){
        String response = this.invoker.handleCommand("info,*,*,*,Courier Corporation;");
        String expected = "info,1,\n" +
                "10,9780486457437,\"1800 Mechanical Movements\",{Gardner Dexter Hiscox},Courier Corporation," +
                "2007-01-01,409;";
        assertEquals(response, expected);
    }

    /**
     * info,*,*,*,*,title;
     */
    @Test
    public void testSortByTitle(){
        String response = this.invoker.handleCommand("info,*,*,*,*,title;");
        String expected = "info,5,\n" +
                "10,9781440229886,\"100 Sexiest Women in Comics\",{Brent Frankenhoff},Krause Publications,2180-07-11,64,\n" +
                "10,9781592533251,\"101 Dog Tricks\",{Kyra Sundance, Chalcy},Quarry,2168-03-04,208,\n" +
                "10,9780486457437,\"1800 Mechanical Movements\",{Gardner Dexter Hiscox},Courier Corporation,2007-01-01,409,\n" +
                "10,9783836548458,\"75 Years of Marvel Comics\",{Roy Thomas},Taschen,2192-10-11,711,\n" +
                "10,9780800759490,\"90 Minutes in Heaven\",{Don Piper, Cecil Murphey},Revell,2168-02-09,208;";
        assertEquals(response, expected);
    }

    /**
     * info,*,*,*,*,publish-date;
     */
    @Test
    public void testSortByPublishDate(){
        String response = this.invoker.handleCommand("info,*,*,*,*,publish-date;");
    }

    /**
     * info,*,*,*,*,book-status;
     */
    public void testSortByBookStatus(){
        this.testUtil.setUpNotAvailableBook();
        String response = this.invoker.handleCommand("info,*,*,*,*,book-status;");

    }

}

