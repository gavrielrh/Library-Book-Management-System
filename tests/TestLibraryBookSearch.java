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
    public void testStarTitleStarAuthors(){
        String response = this.invoker.handleCommand("info,*,*;");


    }

    /**
     * info,*,{Mark Twain};
     */
    public void testJustAuthors(){
        String response = this.invoker.handleCommand("info,*,{Mark Twain};");
    }


    /**
     * info,"Surviving High School",*;
     */
    public void testJustTitle(){
        String response = this.invoker.handleCommand("info,'Surviving High School',*;");
    }

    /**
     * info,*,*,9780838712474;
     */
    public void testJustIsbn(){
        String response = this.invoker.handleCommand("info,*,*,9780838712474;");
    }

    /**
     * info,*,*,*,Cengage Learning;
     */
    public void testJustPublisher(){
        String response = this.invoker.handleCommand("info,*,*,*,Human Kinetics;");
    }

    /**
     * info,*,*,*,*,title;
     */
    public void testSortByTitle(){
        String response = this.invoker.handleCommand("info,*,*,*,*,title;");
    }

    /**
     * info,*,*,*,*,publish-date;
     */
    public void testSortByPublishDate(){
        String response = this.invoker.handleCommand("info,*,*,*,*,publish-date;");
    }

    /**
     * info,*,*,*,*,book-status;
     */
    public void testSortByBookStatus(){
        String response = this.invoker.handleCommand("info,*,*,*,*,book-status;");
    }

}

