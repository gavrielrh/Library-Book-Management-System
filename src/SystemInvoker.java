/*
 * Filename: SystemInvoker.java
 * SystemInvoker is used to simulate the LBMS running.
 *
 * @author - Brendan Jones (bpj1651@rit.edu)
 * @author - Gavriel Rachael-Homann (gxr2329@rit.edu)
 */

/* imports */

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class SystemInvoker {
    private LBMS self;
    private String partialRequest;

    /**
     * Constructor for SystemInvoker.
     *
     * @param self the system's LBMS.
     */
    public SystemInvoker(LBMS self) {
        this.self = self;
        this.partialRequest = "";
    }

    /**
     * Gets the LBMS associated with the invoker.
     *
     * @return the LBMS
     */
    public LBMS getLBMS() {
        return this.self;
    }

    /**
     * Handles the given request
     *
     * @param requestLine the given request
     * @return the response after handling the request
     */
    public String handleCommand(String requestLine) {
        requestLine = this.partialRequest + requestLine;

        if (requestLine.endsWith(";")) {
            this.partialRequest += requestLine;
        } else {
            this.partialRequest = "";
        }

        // All requests must end with ";". Otherwise it's a PartialRequest.
        if (!requestLine.endsWith(";")) {
            Request partialRequest = new PartialRequest();
            partialRequest.execute();

            this.partialRequest = requestLine;

            return partialRequest.response();
        } else {
            this.partialRequest = "";

            if (Character.isDigit(requestLine.charAt(0))) {
                return this.handleClientCommand(requestLine);
            }

            requestLine = requestLine.substring(0, requestLine.length() - 1);

            String[] tokens = RequestParser.getTokens(requestLine);

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

            switch (tokens[0]) {

                //<1> Register Visitor - register,first name,last name,address, phone-number;
                case "register": {

                    //Find all missing parameters.
                    if (tokens.length < 5) {
                        ArrayList<String> missingParameters = new ArrayList<String>();

                        for (int i = tokens.length; i < 5; i++) {
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

                        return missingParam.response();
                        // Begin visit is valid, get all necessary data.
                    } else {
                        String firstName = tokens[1];
                        String lastName = tokens[2];
                        String address = tokens[3];
                        String phoneNum = tokens[4];

                        Request register = new RegisterVisitorRequest(self, firstName, lastName, address, phoneNum);
                        register.execute();

                        return register.response();
                    }
                }


                //<2> Begin Visit - arrive,visitor ID;
                case "arrive":
                    if (tokens.length < 2) {
                        ArrayList<String> missingParameters = new ArrayList<String>();

                        missingParameters.add(0, "visitor ID");

                        Request missingParam = new MissingParamsRequest("arrive", missingParameters);
                        missingParam.execute();

                        return missingParam.response();
                    } else {
                        String visitorId = tokens[1];

                        Request beginVisitRequest = new BeginVisitRequest(self, visitorId);
                        beginVisitRequest.execute();

                        return beginVisitRequest.response();
                    }
                    //<3> End Visit - depart,visitor ID;
                case "depart":
                    if (tokens.length < 2) {
                        ArrayList<String> missingParameters = new ArrayList<String>();

                        missingParameters.add(0, "visitor ID");

                        Request missingParam = new MissingParamsRequest("End Visit", missingParameters);
                        missingParam.execute();

                        return missingParam.response();
                    } else {
                        String visitorId = tokens[1];

                        Request endVisitRequest = new EndVisitRequest(self, visitorId);
                        endVisitRequest.execute();

                        return endVisitRequest.response();
                    }
                    //<4> Library Book Search - info,title,{authors},[isbn, [publisher,[sort order]]];
                case "info":
                    if (tokens.length < 3) {
                        ArrayList<String> missingParameters = new ArrayList<String>();

                        for (int i = tokens.length; i < 3; i++) {
                            if (i == 1) {
                                missingParameters.add(missingParameters.size(), "title");
                            } else if (i == 2) {
                                missingParameters.add(missingParameters.size(), "{authors}");
                            }
                        }

                        Request missingParam = new MissingParamsRequest("Library Book Search", missingParameters);
                        missingParam.execute();

                        return missingParam.response();
                    } else {
                        String title = tokens[1].equals("*") ? "*" : tokens[1].substring(1, tokens[1].length() - 1);
                        String authors = tokens[2].equals("*") ? "*" : tokens[2].substring(1, tokens[2].length() - 1);
                        String isbn = "*";
                        String publisher = "*";
                        String sortOrder = "*";

                        if (tokens.length >= 4) {
                            isbn = tokens[3];

                            if (tokens.length >= 5) {
                                publisher = tokens[4];

                                if (tokens.length >= 6) {
                                    sortOrder = tokens[5];
                                }
                            }
                        }

                        Request libraryBookSearchRequest = new LibraryBookSearchRequest(self, title, authors, isbn, publisher, sortOrder);
                        libraryBookSearchRequest.execute();

                        return libraryBookSearchRequest.response();
                    }
                    //<5> Borrow Book - borrow,visitor ID,{id};
                case "borrow":
                    if (tokens.length < 3) {
                        ArrayList<String> missingParameters = new ArrayList<String>();

                        for (int i = tokens.length; i < 3; i++) {
                            if (i == 1) {
                                missingParameters.add(missingParameters.size(), "visitor ID");
                            } else if (i == 2) {
                                missingParameters.add(missingParameters.size(), "{id}");
                            }
                        }

                        Request missingParam = new MissingParamsRequest("Borrow Book", missingParameters);
                        missingParam.execute();

                        return missingParam.response();
                    } else {
                        ArrayList<String> bookIds = new ArrayList<>();
                        String visitorId = tokens[1];

                        bookIds.addAll(Arrays.asList(tokens).subList(2, tokens.length));

                        Request borrowBookRequest = new BorrowBookRequest(self, visitorId, bookIds);
                        borrowBookRequest.execute();

                        return borrowBookRequest.response();
                    }
                    //<6> Find Borrowed Books - borrowed,visitor ID;
                case "borrowed":
                    if (tokens.length < 2) {
                        ArrayList<String> missingParameters = new ArrayList<String>();

                        missingParameters.add(0, "visitor ID");

                        Request missingParam = new MissingParamsRequest("Find Borrowed Books", missingParameters);
                        missingParam.execute();

                        return missingParam.response();
                    } else {
                        String visitorId = tokens[1];

                        Request findBorrowedBooks = new FindBorrowedBooksRequest(self, visitorId);
                        findBorrowedBooks.execute();

                        return findBorrowedBooks.response();
                    }
                    //<7> Return Book - return,visitor ID,id[,ids];
                case "return":
                    if (tokens.length < 3) {
                        ArrayList<String> missingParameters = new ArrayList<String>();

                        for (int i = tokens.length; i < 3; i++) {
                            if (i == 1) {
                                missingParameters.add(missingParameters.size(), "visitor ID");
                            } else if (i == 2) {
                                missingParameters.add(missingParameters.size(), "id");
                            }
                        }

                        Request missingParam = new MissingParamsRequest("Return Book", missingParameters);
                        missingParam.execute();

                        return missingParam.response();
                    } else {
                        String visitorId = tokens[1];
                        ArrayList<Integer> bookIds = new ArrayList<>();

                        for (int i = 2; i < tokens.length; i++) {
                            bookIds.add(Integer.parseInt(tokens[i]));
                        }

                        Request returnBookRequest = new ReturnBookRequest(self, visitorId, bookIds);
                        returnBookRequest.execute();

                        return returnBookRequest.response();
                    }
                    //<8> Pay Fine - pay,visitor ID,amount;
                case "pay":
                    if (tokens.length < 3) {
                        ArrayList<String> missingParameters = new ArrayList<String>();

                        for (int i = tokens.length; i < 3; i++) {
                            if (i == 1) {
                                missingParameters.add(missingParameters.size(), "visitor ID");
                            } else if (i == 2) {
                                missingParameters.add(missingParameters.size(), "amount");
                            }
                        }

                        Request missingParam = new MissingParamsRequest("Pay Fine", missingParameters);
                        missingParam.execute();

                        return missingParam.response();
                    } else {
                        String visitorId = tokens[1];
                        double amount = Double.parseDouble(tokens[2]);

                        Request payFineRequest = new PayFineRequest(self, visitorId, amount);
                        payFineRequest.execute();

                        return payFineRequest.response();
                    }
                    //<9> Book Store Search - search,title,[{authors},isbn[,publisher[,sort order]]];
                case "search":
                    if (tokens.length < 2) {
                        ArrayList<String> missingParameters = new ArrayList<String>();
                        missingParameters.add(0, "title");

                        Request missingParam = new MissingParamsRequest("Book Store Search", missingParameters);
                        missingParam.execute();

                        return missingParam.response();
                    } else {
                        String title = tokens[1].equals("*") ? "*" : tokens[1].substring(1, tokens[1].length() - 1);
                        String authors = "*";
                        String isbn = "*";
                        String publisher = "*";
                        String sortOrder = "*";

                        if (tokens.length >= 3) {
                            authors = tokens[2].equals("*") ? "*" : tokens[2].substring(1, tokens[2].length() - 1);

                            if (tokens.length >= 4) {
                                isbn = tokens[3];

                                if (tokens.length >= 5) {
                                    publisher = tokens[4];

                                    if (tokens.length >= 6) {
                                        sortOrder = tokens[5];
                                    }
                                }
                            }
                        }

                        Request bookStoreSearchRequest = new BookStoreSearchRequest(self, title, authors, isbn, publisher, sortOrder, this.getLBMS().getBookService());
                        bookStoreSearchRequest.execute();

                        return bookStoreSearchRequest.response();
                    }
                    //<10> Book Purchase - buy,quantity,id[,ids];
                case "buy":
                    if (tokens.length < 3) {
                        ArrayList<String> missingParameters = new ArrayList<String>();

                        for (int i = tokens.length; i < 3; i++) {
                            if (i == 1) {
                                missingParameters.add(missingParameters.size(), "quantity");
                            } else if (i == 2) {
                                missingParameters.add(missingParameters.size(), "id");
                            }
                        }

                        Request missingParam = new MissingParamsRequest("Book Purchase", missingParameters);
                        missingParam.execute();

                        return missingParam.response();
                    } else {
                        String quantityVal = tokens[1];
                        int quantity = Integer.parseInt(quantityVal);
                        ArrayList<Integer> ids = new ArrayList<>();

                        for (int i = 2; i < tokens.length; i++) {
                            int id = Integer.parseInt(tokens[i]);

                            if (self.getBookFromQueryId(id) != null) {
                                ids.add(id);
                            }
                        }

                        Request bookPurchaseRequest = new BookPurchaseRequest(self, quantity, ids);
                        bookPurchaseRequest.execute();

                        return bookPurchaseRequest.response();
                    }
                    //<11> Advance Time - advance,number-of-days[,number-of-hours];
                case "advance":
                    if (tokens.length < 2) {
                        ArrayList<String> missingParameters = new ArrayList<String>();
                        missingParameters.add(0, "number-of-days");

                        Request missingParam = new MissingParamsRequest("Advance Time", missingParameters);
                        missingParam.execute();

                        return missingParam.response();
                    } else {
                        if (tokens.length == 2) {
                            //just days
                            int days = Integer.parseInt(tokens[1]);

                            Request advanceTimeRequest = new AdvanceTimeRequest(self, days);
                            advanceTimeRequest.execute();

                            return advanceTimeRequest.response();
                        } else if (tokens.length == 3) {
                            int days = Integer.parseInt((tokens[1]));
                            int hours = Integer.parseInt((tokens[2]));

                            Request advanceTimeRequest = new AdvanceTimeRequest(self, days, hours);
                            advanceTimeRequest.execute();

                            return advanceTimeRequest.response();
                        }
                    }
                    //<12> Current Date & Time - datetime;
                case "datetime":
                    Request currentTime = new CurrentTimeRequest(self);
                    currentTime.execute();

                    return currentTime.response();
                //<13> Library Statistics Report - report[,days];
                case "report":
                    Request LibraryStatisticsReport = new LibraryStatisticsReportRequest(self);
                    LibraryStatisticsReport.execute();

                    return LibraryStatisticsReport.response();
                //<14> Connect Client - connect;
                case "connect":
                    Request connectClientRequest = new ConnectClientRequest(self);
                    connectClientRequest.execute();

                    return connectClientRequest.response();
                //TODO: Replace this with the actual prompt for shutting down the system.
                case "currentclient":
                    Request currentClientRequest = new CurrentClientRequest(self);
                    currentClientRequest.execute();

                    return currentClientRequest.response();
                case "quit":
                    shutdown(self);
                    return "Shutting down";
            }
        }
        return null;
    }

    /**
     * Handles the given command
     *
     * @param inputLine the given command
     * @return the response after handling the command
     */
    public String handleClientCommand(String inputLine) {

        //inputline is given as:
        //clientID,<any request>;
        String[] tokens = RequestParser.getTokens(inputLine);
        String firstWord = tokens[1];

        if (firstWord.endsWith(";")) {
            firstWord = firstWord.substring(0, firstWord.length() - 1);
        }

        String lastWord = tokens[tokens.length - 1];

        if (lastWord.endsWith(";")) {
            tokens[tokens.length - 1] = lastWord.substring(0, lastWord.length() - 1);
        }
        String clientId = tokens[0];

        if (this.self.hasClientId(clientId)) {
            Client client = self.getClient(clientId);
            switch (firstWord) {
                case "disconnect":
                    Request disconnetClientRequest = new DisconnectClientRequest(self, clientId);
                    disconnetClientRequest.execute();

                    return disconnetClientRequest.response();
                case "create":
                    //client ID,create,username,password,role,visitor ID;
                    String username = tokens[2];
                    String password = tokens[3];
                    String role = tokens[4];
                    String visitorId = tokens[5];

                    Request createAccountRequest = new CreateAccountRequest(client, self, username, password, role, visitorId);
                    createAccountRequest.execute();

                    return createAccountRequest.response();
                case "login":
                    //client ID,login,username,password;
                    String userName = tokens[2];
                    String pass = tokens[3];

                    Request loginRequest = new LoginRequest(client, self, userName, pass);
                    loginRequest.execute();

                    return loginRequest.response();
                case "logout":
                    Request logOutRequest = new LogoutRequest(client, self);
                    logOutRequest.execute();

                    return logOutRequest.response();
                default:
                    if (client.clientLoggedIn()) {
                        StringBuilder builder = new StringBuilder();

                        for (int i = 1; i < tokens.length; i++) {
                            builder.append(tokens[i]);
                            if (i + 1 < tokens.length) {
                                builder.append(",");
                            }
                        }

                        return client.handleClientCommand(builder.toString() + ";");
                    } else {
                        //TODO: client not authenticated.
                        return "invalid-client-id";
                    }
            }
        } else {
            return "invalid-client-id;";
        }
    }

    /**
     * Running SystemInvoker's main method will start up the LBMS
     * and prompt for requests.
     * Requests are then invoked using the Command pattern and LBMS - request subsystem
     * When the invoker is done running, all data is saved using a "Shutdown-thread"
     *
     * @param args - not used.
     */
    public static void main(String[] args) {

        LBMS system = startUp();
        SystemInvoker invoker = new SystemInvoker(system);
        Scanner inputRequest = new Scanner(System.in);
        while (true) {
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
     *
     * @return - the LBMS the invoker will use.
     */
    public static LBMS startUp() {
        File SystemData = new File("data/LBMS-SYSTEM.ser");

        boolean firstStartUp = !SystemData.exists();

        if (firstStartUp) {
            LBMS system = new LBMS();

            try {
                system.seedInitialLibrary("data/books.txt");
            } catch (IOException e) {
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
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
