import org.junit.*;
import static org.junit.Assert.*;
import java.io.InputStream;

import java.util.Date;
import java.util.Scanner;

/**
 * Tests is a J-Unit testing file used to test the behavior of LBMS
 * @author - Brendan Jones (bpj1651@rit.edu)
 */
public class TestRegister {
    private SystemInvoker invoker;
    private Date systemDate;

    @Before
    public void setUp() {
        LBMS system = SystemInvoker.startUp();
        this.invoker = new SystemInvoker(system);
        this.systemDate = system.getTime();
        this.invoker.handleCommand("register,duplicateFirst,duplicateLast,duplicate address,1231231234;");
    }

    @Test
    public void testSuccess() {
        String response = this.invoker.handleCommand("register,sampleFirst,sampleLast,sample address,1234561223;");
        String expectedDate = LBMS.dateFormatter.format(this.systemDate);
        String expected = "register" + expectedDate + ";";
        //Ignore the 10 digit ID, it's random so can't be tested
        String[] responseArray = response.split(",");
        response = responseArray[0] + responseArray[2];
        assertEquals(expected, response);
    }

    @Test
    public void testDuplicate() {
        String response = this.invoker.handleCommand("register,duplicateFirst,duplicateLast,duplicate address,1231231234;");
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
