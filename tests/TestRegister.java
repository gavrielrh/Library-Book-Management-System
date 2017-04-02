/**
 * TestRegister is a J-Unit testing file used to test the behavior of LBMS
 * @author - Brendan Jones (bpj1651@rit.edu)
 */


/* imports */
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Date;


public class TestRegister {
    private SystemInvoker invoker;
    private LBMS system;
    private Date systemDate;
    private TestUtil testUtil;

    @Before
    public void setUp() {
        this.testUtil = new TestUtil();
        this.invoker = this.testUtil.getInvoker();
        this.system = this.testUtil.getLbms();
        this.systemDate = this.system.getTime();
        /** used for testing duplicate:
         * registerVisitor registers a visitor with:
         * register,sampleFirst,sampleLast,sample address,1234561223;
         */
        this.testUtil.registerVisitor();
    }

    @Test
    public void testSuccess() {
        String response = this.invoker.handleCommand("register,sampleF,sampleL,sampleA,1231231234;");
        String visitorId = response.split(",")[1];
        String expectedDate = LBMS.dateFormatter.format(this.systemDate);
        String expected = "register," + visitorId + "," + expectedDate + ";";
        assertEquals(expected, response);
    }

    @Test
    public void testDuplicate() {
        String response = this.invoker.handleCommand("register,sampleFirst,sampleLast,sample address,1234561223;");
        String expected = "register,duplicate;";
        assertEquals(expected, response);
    }

    @Test
    public void testMissingParamPhoneNumber() {
        String response = this.invoker.handleCommand("register,fname,lname,address;");
        String expected = "register,missing-parameters,{phone-number};";
        assertEquals(expected, response);
    }

    @Test
    public void testMissingParamsAddressPhoneNumber(){
        String response = this.invoker.handleCommand("register,fname,lname;");
        String expected = "register,missing-parameters,{address,phone-number};";
        assertEquals(expected, response);
    }

    @Test
    public void testMissingParamsAddressPhoneNumberLastName(){
        String response = this.invoker.handleCommand("register,fname;");
        String expected = "register,missing-parameters,{last name,address,phone-number};";
        assertEquals(expected, response);
    }

    @Test
    public void testMissingParamsAddressPhoneNumberLastNameFirstName(){
        String response = this.invoker.handleCommand("register;");
        String expected = "register,missing-parameters,{first name,last name,address,phone-number};";
        assertEquals(expected, response);
    }
}
