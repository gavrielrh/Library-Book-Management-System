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

    /**
     * startUp gets the LBMS used for the invoker. There are two possibilities:
     * (1) - LBMS is loaded from the data/LBMS-DATA.ser
     * (2) - If no data is there, LBMS is "booted" for the first time.
     * @return - the LBMS the invoker will use.
     */
    private LBMS startUp() {
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
    private void shutdown(LBMS system) {
        try {
            FileOutputStream fileOut = new FileOutputStream("data/LBMS-SYSTEM.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(system);
            fileOut.close();
        }catch (IOException i){
            i.printStackTrace();
        }

    }

    private void promptRequests(LBMS self){
        while (true) {

            //requests are from System.in
            Scanner inputRequest = new Scanner(System.in);
            //get the request and split it by comma
            String requestLine = inputRequest.nextLine();
            String[] request = requestLine.split(",");

            // All requests must end with ";". Otherwise it's a PartialRequest.
            if (!(request[request.length - 1].endsWith(";"))) {
                Request partialRequest = new PartialRequest();
                partialRequest.execute();
                System.out.println(partialRequest.response());
            } else {
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
                            Request missingParam = new MissingParamsRequest("Register Visitor", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());

                            // Begin visit is valid, get all necessary data.
                        } else {
                            String firstName = request[1];
                            String lastName = request[2];
                            String address = request[3];
                            String phoneNum = request[4];
                            //slice off the ; ending the request.

                            Request register = new RegisterVisitorRequest(self, firstName, lastName, address, phoneNum);
                            register.execute();
                            System.out.println(register.response());
                        }
                    }
                    break;


                    //<2> Begin Visit - arrive,visitor ID;
                    case "arrive":
                        if (request.length < 2) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            missingParameters.add(0, "visitor ID");
                            Request missingParam = new MissingParamsRequest("Begin Visit", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
                        } else {
                            String visitorId = request[1];
                            Request beginVisitRequest = new BeginVisitRequest(self, visitorId);
                            beginVisitRequest.execute();
                            System.out.println(beginVisitRequest.response());
                        }
                        break;
                    //<3> End Visit - depart,visitor ID;
                    case "depart":
                        if (request.length < 2) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            missingParameters.add(0, "visitor ID");
                            Request missingParam = new MissingParamsRequest("End Visit", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
                        } else {
                            String visitorId = request[1];
                            Request endVisitRequest = new EndVisitRequest(self, visitorId);
                            endVisitRequest.execute();
                            System.out.println(endVisitRequest.response());
                        }
                        break;
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
                            System.out.println(missingParam.response());
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
                            System.out.println(libraryBookSearchRequest.response());
                        }
                        break;
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
                            System.out.println(missingParam.response());
                        } else {
                            ArrayList<String> bookIds = new ArrayList<String>();
                            String visitorId = request[1];
                            for (int i = 2; i < request.length; i++) {
                                bookIds.add(request[i]);
                            }
                            Request borrowBookRequest = new BorrowBookRequest(self, visitorId, bookIds);
                            borrowBookRequest.execute();
                            System.out.println(borrowBookRequest.response());
                        }
                        break;
                    //<6> Find Borrowed Books - borrowed,visitor ID;
                    case "borrowed":
                        if (request.length < 2) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            missingParameters.add(0, "visitor ID");
                            Request missingParam = new MissingParamsRequest("Find Borrowed Books", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
                        } else {
                            String visitorId = request[1];
                            Request findBorrowedBooks = new FindBorrowedBooksRequest(self, visitorId);
                            findBorrowedBooks.execute();
                            System.out.println(findBorrowedBooks.response());
                        }
                        break;
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
                            System.out.println(missingParam.response());
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
                            System.out.println(missingParam.response());
                        }
                        break;
                    //<9> Book Store Search - search,title,[{authors},isbn[,publisher[,sort order]]];
                    case "search":
                        if (request.length < 2) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            missingParameters.add(0, "title");
                            Request missingParam = new MissingParamsRequest("Book Store Search", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
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

                            Request bookStoreSearchRequest = new BookStoreSearchRequest(self, title, authors, isbn, publisher, sortOrder);
                            bookStoreSearchRequest.execute();
                            System.out.println(bookStoreSearchRequest.response());
                        }
                        break;
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
                            System.out.println(missingParam.response());
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
                            System.out.println(bookPurchaseRequest.response());


                        }
                        break;
                    //<11> Advance Time - advance,number-of-days[,number-of-hours];
                    case "advance":
                        if (request.length < 2) {
                            ArrayList<String> missingParameters = new ArrayList<String>();
                            missingParameters.add(0, "number-of-days");
                            Request missingParam = new MissingParamsRequest("Advance Time", missingParameters);
                            missingParam.execute();
                            System.out.println(missingParam.response());
                        } else {
                            if (request.length == 2) {
                                //just days
                                int days = Integer.parseInt(request[1]);
                                Request advanceTimeRequest = new AdvanceTimeRequest(self, days);
                                advanceTimeRequest.execute();
                                System.out.println(advanceTimeRequest.response());
                            } else if (request.length == 3) {
                                int days = Integer.parseInt((request[1]));
                                int hours = Integer.parseInt((request[2]));
                                Request advanceTimeRequest = new AdvanceTimeRequest(self, days, hours);
                                advanceTimeRequest.execute();
                                System.out.println(advanceTimeRequest.response());
                            }
                        }
                        break;
                    //<12> Current Date & Time - datetime;
                    case "datetime":
                        Request currentTime = new CurrentTimeRequest(self);
                        currentTime.execute();
                        System.out.println(currentTime.response());
                        //<13> Library Statistics Report - report[,days];
                    case "report":
                        Request LibraryStatisticsReport = new LibraryStatisticsReportRequest(self);
                        LibraryStatisticsReport.execute();
                        System.out.println(LibraryStatisticsReport.response());

                        //TODO: Replace this with the actual prompt for shutting down the system.
                    case "quit":
                        System.out.println("Shutting down");
                        this.shutdown(self);
                        break;
                }
            }
        }
    }

    /**
     * Running SystemInvoker's main method will start up the LBMS
     * and prompt for requests.
     * Requests are then invoked using the Command pattern and LBMS - request subsystem
     * @param args - not used.
     */
    public static void main(String[] args){
        SystemInvoker invoker = new SystemInvoker();
        LBMS self = invoker.startUp();
        invoker.promptRequests(self);

    }
}
