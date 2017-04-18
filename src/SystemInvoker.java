/**
 * Filename: SystemInvoker.java
 * SystemInvoker is used to simulate the LBMS running.
 * @author - Brendan Jones (bpj1651@rit.edu)
 */


/* imports */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class SystemInvoker {
    private LBMS self;
    private String partialRequest;
    private BookStoreSearchRequest.BOOKSERVICE BOOKSERVICE;

    public SystemInvoker(LBMS self){
        this.self = self;
        this.partialRequest = "";
        this.BOOKSERVICE = BookStoreSearchRequest.BOOKSERVICE.local;
    }

    public LBMS getLBMS(){
        return this.self;
    }
    public String handleCommand(String requestLine){
        requestLine = this.partialRequest + requestLine;
        if(requestLine.endsWith(";")){
            this.partialRequest += requestLine;
        }else{
            this.partialRequest = "";
        }

        //Prepend any partialRequestStrings
        String[] request = requestLine.split(",");

        // All requests must end with ";". Otherwise it's a PartialRequest.
        if (!(request[request.length - 1].endsWith(";"))) {
            Request partialRequest = new PartialRequest();
            partialRequest.execute();
            this.partialRequest = requestLine;
            return (partialRequest.response());
        } else {
            this.partialRequest = "";
            if(Character.isDigit(requestLine.charAt(0))){
                return this.handleClientCommand(requestLine);
            }


            /*
            * requests can be:
            *
            * <num> Name of Request - format,of,request;
            * <1> Register Visitor - register,first name,last name,address, phone-number;
            * <2> Begin Visit - arrive,visitor ID;
            * <3> End Visit - depart,visitor ID;
            * <4> Library Book Search - info,title,{authors},[isbn, [publisher,[sort order]]];
            * <5> Borrow Book - borrow,visitor ID,{id};
            * <6> Find Borrowed Books - borrowed,visitor ID;
            * <7> Return Book - return,visitor ID,id[,ids];
            * <8> Pay Fine - pay,visitor ID,amount;
            * <9> Book Store Search - search,title,[{authors},isbn[,publisher[,sort order]]];
            * <10> Book Purchase - buy,quantity,id[,ids];
            * <11> Advance Time - advance,number-of-days[,number-of-hours];
            * <12> Current Date & Time - datetime;
            * <13> Library Statistics Report - report[,days];
            *
            *
             */

            //Slice off the ; in the first word for easier cases in switch statement.
            String firstWord = request[0];
            if (firstWord.endsWith(";")) {
                firstWord = firstWord.substring(0, firstWord.length() - 1);
            }
            String lastWord = request[request.length - 1];
            if (lastWord.endsWith(";")) {
                request[request.length - 1] = lastWord.substring(0, lastWord.length() - 1);
            }



            switch (firstWord) {

                //<1> Register Visitor - register,first name,last name,address, phone-number;
                case "register": {

                    //Find all missing parameters.
                    if (request.length < 5) {
                        ArrayList<String> missingParameters = new ArrayList<String>();
                        for (int i = request.length; i < 5; i++) {
                            if (i == 1) {
                                missingParameters.add(missingParameters.size(), "first name");
                            } else if (i == 2) {
                                missingParameters.add(missingParameters.size(), "last name");
                            } else if (i == 3) {
                                missingParameters.add(missingParameters.size(), "address");
                            } else if (i == 4) {
                                missingParameters.add(missingParameters.size(), "phone-number");
                            }
                        }
                        Request missingParam = new MissingParamsRequest("register", missingParameters);
                        missingParam.execute();
                        return (missingParam.response());

                        // Begin visit is valid, get all necessary data.
                    } else {
                        String firstName = request[1];
                        String lastName = request[2];
                        String address = request[3];
                        String phoneNum = request[4];
                        //slice off the ; ending the request.

                        Request register = new RegisterVisitorRequest(self, firstName, lastName, address, phoneNum);
                        register.execute();
                        return (register.response());
                    }
                }


                //<2> Begin Visit - arrive,visitor ID;
                case "arrive":
                    if (request.length < 2) {
                        ArrayList<String> missingParameters = new ArrayList<String>();
                        missingParameters.add(0, "visitor ID");
                        Request missingParam = new MissingParamsRequest("arrive", missingParameters);
                        missingParam.execute();
                        return (missingParam.response());
                    } else {
                        String visitorId = request[1];
                        Request beginVisitRequest = new BeginVisitRequest(self, visitorId);
                        beginVisitRequest.execute();
                        return (beginVisitRequest.response());
                    }
                    //<3> End Visit - depart,visitor ID;
                case "depart":
                    if (request.length < 2) {
                        ArrayList<String> missingParameters = new ArrayList<String>();
                        missingParameters.add(0, "visitor ID");
                        Request missingParam = new MissingParamsRequest("End Visit", missingParameters);
                        missingParam.execute();
                        return (missingParam.response());
                    } else {
                        String visitorId = request[1];
                        Request endVisitRequest = new EndVisitRequest(self, visitorId);
                        endVisitRequest.execute();
                        return (endVisitRequest.response());
                    }
                    //<4> Library Book Search - info,title,{authors},[isbn, [publisher,[sort order]]];
                case "info":
                    if (request.length < 3) {
                        ArrayList<String> missingParameters = new ArrayList<String>();
                        for (int i = request.length; i < 3; i++) {
                            if (i == 1) {
                                missingParameters.add(missingParameters.size(), "title");
                            } else if (i == 2) {
                                missingParameters.add(missingParameters.size(), "{authors}");
                            }
                        }
                        Request missingParam = new MissingParamsRequest("Library Book Search", missingParameters);
                        missingParam.execute();
                        return (missingParam.response());
                    } else {
                        String isbn = null;
                        String sortOrder = null;
                        String publisher = null;
                        String authors = null;
                        String title = null;

                        requestLine = requestLine.substring("info,".length(), requestLine.length() - 1);

                        if (requestLine.startsWith("*")) {
                            requestLine = requestLine.substring(1);
                        } else {
                            requestLine = requestLine.substring(1);
                            title = requestLine.substring(0, requestLine.indexOf("\""));
                        }
                        requestLine = requestLine.substring(title != null ? title.length() + 2 : 1, requestLine.length());


                        if (requestLine.startsWith("{")) {
                            authors = requestLine.substring(1, requestLine.indexOf("}"));
                            requestLine = requestLine.substring(authors.length() + 2);
                        } else {
                            requestLine = requestLine.substring(1);
                        }

                        if (!requestLine.isEmpty()) {
                            String[] optionalParts = requestLine.split(",");
                            isbn = optionalParts[1].equals("*") ? null : optionalParts[1];
                            if (optionalParts.length > 2) {
                                publisher = optionalParts[2].equals("*") ? null : optionalParts[2];
                            }
                            if (optionalParts.length > 3) {
                                sortOrder = optionalParts[3].equals("*") ? null : optionalParts[3];
                            }
                        }

                        Request libraryBookSearchRequest = new LibraryBookSearchRequest(self, title, authors, isbn, publisher, sortOrder);
                        libraryBookSearchRequest.execute();
                        return (libraryBookSearchRequest.response());
                    }
                    //<5> Borrow Book - borrow,visitor ID,{id};
                case "borrow":
                    if (request.length < 3) {
                        ArrayList<String> missingParameters = new ArrayList<String>();
                        for (int i = request.length; i < 3; i++) {
                            if (i == 1) {
                                missingParameters.add(missingParameters.size(), "visitor ID");
                            } else if (i == 2) {
                                missingParameters.add(missingParameters.size(), "{id}");
                            }
                        }
                        Request missingParam = new MissingParamsRequest("Borrow Book", missingParameters);
                        missingParam.execute();
                        return (missingParam.response());
                    } else {
                        ArrayList<String> bookIds = new ArrayList<>();
                        String visitorId = request[1];
                        for (int i = 2; i < request.length; i++) {
                            bookIds.add(request[i]);
                        }
                        Request borrowBookRequest = new BorrowBookRequest(self, visitorId, bookIds);
                        borrowBookRequest.execute();
                        return (borrowBookRequest.response());
                    }
                    //<6> Find Borrowed Books - borrowed,visitor ID;
                case "borrowed":
                    if (request.length < 2) {
                        ArrayList<String> missingParameters = new ArrayList<String>();
                        missingParameters.add(0, "visitor ID");
                        Request missingParam = new MissingParamsRequest("Find Borrowed Books", missingParameters);
                        missingParam.execute();
                        return (missingParam.response());
                    } else {
                        String visitorId = request[1];
                        Request findBorrowedBooks = new FindBorrowedBooksRequest(self, visitorId);
                        findBorrowedBooks.execute();
                        return (findBorrowedBooks.response());
                    }
                    //<7> Return Book - return,visitor ID,id[,ids];
                case "return":
                    if (request.length < 3) {
                        ArrayList<String> missingParameters = new ArrayList<String>();
                        for (int i = request.length; i < 3; i++) {
                            if (i == 1) {
                                missingParameters.add(missingParameters.size(), "visitor ID");
                            } else if (i == 2) {
                                missingParameters.add(missingParameters.size(), "id");
                            }
                        }
                        Request missingParam = new MissingParamsRequest("Return Book", missingParameters);
                        missingParam.execute();
                        return (missingParam.response());
                    }else{
                        String visitorId = request[1];
                        ArrayList<Integer> bookIds = new ArrayList<>();
                        for (int i = 2; i < request.length; i++){
                            bookIds.add(Integer.parseInt(request[i]));
                        }
                        Request returnBookRequest = new ReturnBookRequest(self, visitorId, bookIds);
                        returnBookRequest.execute();
                        return returnBookRequest.response();
                    }
                    //<8> Pay Fine - pay,visitor ID,amount;
                case "pay":
                    if (request.length < 3) {
                        ArrayList<String> missingParameters = new ArrayList<String>();
                        for (int i = request.length; i < 3; i++) {
                            if (i == 1) {
                                missingParameters.add(missingParameters.size(), "visitor ID");
                            } else if (i == 2) {
                                missingParameters.add(missingParameters.size(), "amount");
                            }
                        }
                        Request missingParam = new MissingParamsRequest("Pay Fine", missingParameters);
                        missingParam.execute();
                        return (missingParam.response());
                    }else{
                        String visitorId = request[1];
                        double amount = Double.parseDouble(request[2]);
                        Request payFineRequest = new PayFineRequest(self, visitorId, amount);
                        payFineRequest.execute();
                        return payFineRequest.response();
                    }
                    //<9> Book Store Search - search,title,[{authors},isbn[,publisher[,sort order]]];
                case "search":
                    if (request.length < 2) {
                        ArrayList<String> missingParameters = new ArrayList<String>();
                        missingParameters.add(0, "title");
                        Request missingParam = new MissingParamsRequest("Book Store Search", missingParameters);
                        missingParam.execute();
                        return (missingParam.response());
                    } else {
                        String isbn = null;
                        String sortOrder = null;
                        String publisher = null;
                        String authors = null;
                        String title = null;

                        requestLine = requestLine.substring("search,".length(), requestLine.length() - 1);

                        if (requestLine.startsWith("*")) {
                            requestLine = requestLine.substring(1);
                        } else {
                            requestLine = requestLine.substring(1);
                            title = requestLine.substring(0, requestLine.indexOf("\""));
                        }
                        requestLine = requestLine.substring(title != null ? title.length() + 1 : 0, requestLine.length());
                        if (!requestLine.isEmpty()) {
                            requestLine = requestLine.substring(1);

                            if (requestLine.startsWith("{")) {
                                authors = requestLine.substring(1, requestLine.indexOf("}"));
                                requestLine = requestLine.substring(authors.length() + 2);
                            } else {
                                requestLine = requestLine.substring(1);
                            }
                            if (!requestLine.isEmpty()) {
                                String[] optionalParts = requestLine.split(",");
                                isbn = optionalParts[1].equals("*") ? null : optionalParts[1];
                                if (optionalParts.length > 2) {
                                    publisher = optionalParts[2].equals("*") ? null : optionalParts[2];
                                }
                                if (optionalParts.length > 3) {
                                    sortOrder = optionalParts[3].equals("*") ? null : optionalParts[3];
                                }
                            }
                        }

                        Request bookStoreSearchRequest = new BookStoreSearchRequest(self, title, authors, isbn, publisher, sortOrder, this.BOOKSERVICE);
                        bookStoreSearchRequest.execute();
                        return (bookStoreSearchRequest.response());
                    }
                    //<10> Book Purchase - buy,quantity,id[,ids];
                case "buy":
                    if (request.length < 3) {
                        ArrayList<String> missingParameters = new ArrayList<String>();
                        for (int i = request.length; i < 3; i++) {
                            if (i == 1) {
                                missingParameters.add(missingParameters.size(), "quantity");
                            } else if (i == 2) {
                                missingParameters.add(missingParameters.size(), "id");
                            }
                        }
                        Request missingParam = new MissingParamsRequest("Book Purchase", missingParameters);
                        missingParam.execute();
                        return (missingParam.response());
                    } else {
                        String quantityVal = request[1];
                        int quantity = Integer.parseInt(quantityVal);
                        ArrayList<Integer> ids = new ArrayList<>();
                        for (int i = 2; i < request.length; i++) {
                            int id = Integer.parseInt(request[i]);

                            if(self.getBookFromQueryId(id) != null) {
                                ids.add(id);
                            }
                        }

                        Request bookPurchaseRequest = new BookPurchaseRequest(self, quantity, ids);
                        bookPurchaseRequest.execute();
                        return (bookPurchaseRequest.response());


                    }
                    //<11> Advance Time - advance,number-of-days[,number-of-hours];
                case "advance":
                    if (request.length < 2) {
                        ArrayList<String> missingParameters = new ArrayList<String>();
                        missingParameters.add(0, "number-of-days");
                        Request missingParam = new MissingParamsRequest("Advance Time", missingParameters);
                        missingParam.execute();
                        return(missingParam.response());
                    } else {
                        if (request.length == 2) {
                            //just days
                            int days = Integer.parseInt(request[1]);
                            Request advanceTimeRequest = new AdvanceTimeRequest(self, days);
                            advanceTimeRequest.execute();
                            return (advanceTimeRequest.response());
                        } else if (request.length == 3) {
                            int days = Integer.parseInt((request[1]));
                            int hours = Integer.parseInt((request[2]));
                            Request advanceTimeRequest = new AdvanceTimeRequest(self, days, hours);
                            advanceTimeRequest.execute();
                            return (advanceTimeRequest.response());
                        }
                    }
                    //<12> Current Date & Time - datetime;
                case "datetime":
                    Request currentTime = new CurrentTimeRequest(self);
                    currentTime.execute();
                    return (currentTime.response());
                //<13> Library Statistics Report - report[,days];
                case "report":
                    Request LibraryStatisticsReport = new LibraryStatisticsReportRequest(self);
                    LibraryStatisticsReport.execute();
                    return (LibraryStatisticsReport.response());
                //TODO: Replace this with the actual prompt for shutting down the system.

                //<14> Connect Client - connect;
                case "connect":
                    Request connectClientRequest = new connectClientRequest(self);
                    connectClientRequest.execute();
                    return (connectClientRequest.response());
                case "quit":
                    shutdown(self);
                    return ("Shutting down");
            }
        }
        return null;
    }

    public String handleClientCommand(String inputLine){

        //inputline is given as:
        //clientID,<any request>;
        String[] request = inputLine.split(",");
        String firstWord = request[1];
        if (firstWord.endsWith(";")) {
            firstWord = firstWord.substring(0, firstWord.length() - 1);
        }
        String lastWord = request[request.length - 1];
        if (lastWord.endsWith(";")) {
            request[request.length - 1] = lastWord.substring(0, lastWord.length() - 1);
        }
        String clientId = request[0];
        if(this.self.hasClientId(clientId)){
            Client client = self.getClient(clientId);
            switch (firstWord){
                case "disconnect":
                    Request disconnetClientRequest = new DisconnectClientRequest(self, clientId);
                    disconnetClientRequest.execute();
                    return disconnetClientRequest.response();
                case "create":
                    //client ID,create,username,password,role,visitor ID;
                    String username = request[2];
                    String password = request[3];
                    String role = request[4];
                    String visitorId = request[5];
                    Request createAccountRequest = new CreateAccountRequest(client, self, username, password, role, visitorId);
                    createAccountRequest.execute();
                    return createAccountRequest.response();
                case "login":
                    //client ID,login,username,password;
                    String userName = request[2];
                    String pass = request[3];
                    Request loginRequest = new LoginRequest(client, self, userName, pass);
                    loginRequest.execute();
                    return loginRequest.response();
                case "logout":
                    //TODO: create logout command
                    return null;
                case "undo":
                    //TODO: undo logic
                    return null;
                case "redo":
                    //TODO: redo logic
                    return null;
                default:
                    return null;
            }
        }else{
            return "invalid-client-id;";
        }
    }



    /**
     * Running SystemInvoker's main method will start up the LBMS
     * and prompt for requests.
     * Requests are then invoked using the Command pattern and LBMS - request subsystem
     * When the invoker is done running, all data is saved using a "Shutdown-thread"
     * @param args - not used.
     */
    public static void main(String[] args){

        LBMS system = startUp();
        SystemInvoker invoker = new SystemInvoker(system);
        Scanner inputRequest = new Scanner(System.in);
        while (true){
            String requestLine = inputRequest.nextLine();
            String response = invoker.handleCommand(requestLine);
            System.out.println(response);
        }
        /*
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                invoker.shutdown(invokerself);
            }
        }, "Shutdown-thread"));
        */
    }



    /**
     * startUp gets the LBMS used for the invoker. There are two possibilities:
     * (1) - LBMS is loaded from the data/LBMS-DATA.ser
     * (2) - If no data is there, LBMS is "booted" for the first time.
     * @return - the LBMS the invoker will use.
     */
    public static LBMS startUp() {
        File SystemData = new File("data/LBMS-SYSTEM.ser");
        boolean firstStartUp = (!SystemData.exists());
        if (firstStartUp) {
            LBMS system = new LBMS();
            try {
                system.seedInitialLibrary("data/books.txt");
            }catch(IOException e){
                e.printStackTrace();
            }
            return system;
        } else {
            try {
                FileInputStream fileIn = new FileInputStream("data/LBMS-SYSTEM.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                Object o = in.readObject();
                return (LBMS) o;
            } catch (IOException i) {
                i.printStackTrace();
            } catch (ClassNotFoundException c) {
                System.out.println("Data not found");
                c.printStackTrace();
            }
        }
        //Should never happen
        return new LBMS();
    }

    /**
     * shutdown saves all LBMS data using serialization.
     */
    public static void shutdown(LBMS system) {
        try {
            FileOutputStream fileOut = new FileOutputStream("data/LBMS-SYSTEM.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(system);
            fileOut.close();
        }catch (IOException i){
            i.printStackTrace();
        }
    }
}
