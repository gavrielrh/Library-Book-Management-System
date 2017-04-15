/**
 * Filename: TestUtil.java
 * @author - Brendan Jones (bpj1651@rit.edu)
 * TestUtil is a utility file that helps run tests more cleanly.
 * There are various utility methods to help tests run better.
 */

public class TestUtil {

    /* Allow tests access to the system */
    private LBMS lbms;

    /* tests will use the invoker to give commands */
    private SystemInvoker invoker;

    /**
     * TestUtil constructor - sets up the LBMS system and invoker
     */
    public TestUtil(){
        this.lbms = SystemInvoker.startUp();
        this.invoker = new SystemInvoker(this.lbms);
    }


    /**
     * getInvoker is a simple get method for the invoker.
     * @return - SystemInvoker invoker that the testUtil uses.
     */
    public SystemInvoker getInvoker(){
        return this.invoker;
    }

    /**
     * getLbms is a simple get method for the lbms.
     * @return - LBMS lbms that the testUtil uses.
     */
    public LBMS getLbms(){
        return this.lbms;
    }
    /**
     * registerVisitor is a utility method used by some of the tests to help with using a visitor.
     * @return - the String Id of the Visitor that is being registered
     */
    public String registerVisitor(){
        String response = this.invoker.handleCommand("register,sampleFirst,sampleLast,sample address,1234561223;");
        String[] responseArray = response.split(",");
        return responseArray[1];
    }

    public String registerSecond(){
        String response = this.invoker.handleCommand("register,sampleFirst1,sampleLast1,sample address1,1224561223;");
        String[] responseArray = response.split(",");
        return responseArray[1];
    }

    public String registerThird(){
        String response = this.invoker.handleCommand("register,sampleFirst2,sampleLast2,sample address2,1234561223;");
        String[] responseArray = response.split(",");
        return responseArray[1];
    }

    public void arriveVisitor(String visitorId){
        String request = "arrive," + visitorId + ";";
        this.invoker.handleCommand(request);
    }

    public void searchBooksFromStore(){
        String request = "search,*,*,*,*,title;";
        this.invoker.handleCommand(request);
    }

    public void purchaseBooksFromSearch(){
        this.searchBooksFromStore();
        String request = "buy,10,1,2,3,4,5;";
        this.invoker.handleCommand(request);
    }

    public void setUpNotAvailableBook() {
        String visitorId = this.registerVisitor();
        this.arriveVisitor(visitorId);
        this.searchBooksFromStore();
        this.invoker.handleCommand("buy,1,6;");
        this.invoker.handleCommand("info,*,*;");
        this.invoker.handleCommand("borrow," + visitorId + "9780979616310;");
    }



}
