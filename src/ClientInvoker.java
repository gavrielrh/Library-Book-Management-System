/*
 * Filename: Client.java
 * @author - Brendan Jones (bpj1651@rit.edu)
 * @author - Gavriel Rachael-Homann (gxr2329@rit.edu)
 *
 * Client represents a client being connected to LBMS.
 */

import java.util.ArrayList;
import java.util.Stack;

public class ClientInvoker {
    private LBMS lbms;

    private Account account;
    private String visitorId;

    private Stack<Request> undoStack;
    private Stack<UndoableCommand> redoStack;

    /**
     * Constructor for ClientInvoker.
     *
     * @param lbms    the lbms being acted on
     * @param account the currently logged in account
     */
    public ClientInvoker(LBMS lbms, Account account) {
        this.lbms = lbms;
        this.account = account;
        this.visitorId = account.getVisitorId();

        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    /**
     * Handles command requests.
     *
     * @param requestLine the request
     * @return the response based on the request
     */
    public String handleCommand(String requestLine) {
        String[] tokens = RequestParser.getTokens(requestLine);

        //Slice off the ; in the first word for easier cases in switch statement.
        String firstWord = tokens[0];
        if (firstWord.endsWith(";")) {
            firstWord = firstWord.substring(0, firstWord.length() - 1);
        }

        String lastWord = tokens[tokens.length - 1];
        if (lastWord.endsWith(";")) {
            tokens[tokens.length - 1] = lastWord.substring(0, lastWord.length() - 1);
        }

        Request requestExecuted = null;
        boolean differentVisitor;

        switch (firstWord) {
            case "register":
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
                    requestExecuted = new MissingParamsRequest("register", missingParameters);
                    // Begin visit is valid, get all necessary data.
                } else {
                    String firstName = tokens[1];
                    String lastName = tokens[2];
                    String address = tokens[3];
                    String phoneNum = tokens[4];

                    requestExecuted = new RegisterVisitorRequest(lbms, firstName, lastName, address, phoneNum);
                }
                break;

            case "arrive":
                if (tokens.length == 1) {
                    requestExecuted = new BeginVisitRequest(this.lbms, this.visitorId);
                } else {
                    requestExecuted = new BeginVisitRequest(this.lbms, tokens[1]);
                }
                break;
            case "depart":
                if (tokens.length == 1) {
                    requestExecuted = new EndVisitRequest(this.lbms, this.visitorId);
                } else {
                    requestExecuted = new EndVisitRequest(this.lbms, tokens[1]);
                }
                break;
            case "undo":
                boolean success = false;
                if (this.undoStack.size() > 0) {
                    Request requestUndo = undoStack.pop();
                    if (isUndoable(requestUndo)) {
                        UndoableCommand undoableCommand = (UndoableCommand) requestUndo;
                        this.redoStack.push(undoableCommand);

                        success = undoableCommand.undo();
                    }
                    if (success) {
                        return "undo,success;";
                    }
                } else {
                    return "undo,cannot-undo;";
                }
                break;
            case "redo":
                if (this.redoStack.size() > 0) {
                    UndoableCommand commandToRedo = redoStack.pop();

                    if (commandToRedo.redo()) {
                        return "redo,success;";
                    }
                } else {
                    return "redo,cannot-redo;";
                }
                break;
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

                    return (missingParam.response());
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

                    requestExecuted = new LibraryBookSearchRequest(lbms, title, authors, isbn, publisher, sortOrder);

                    break;
                }
            case "borrow":
                differentVisitor = tokens.length == 3;

                //borrow,{bookids},visitorId
                //all ids will be token[1]
                //token1
                String[] bookIdsArray = tokens[1].substring(1, tokens[1].length() - 1).split(",");
                ArrayList<String> bookIds = new ArrayList<>();

                for (int i = 0; i < bookIdsArray.length; i++) {
                    bookIds.add(bookIdsArray[i]);
                }

                if (differentVisitor) {
                    requestExecuted = new BorrowBookRequest(lbms, tokens[2], bookIds);
                } else {
                    requestExecuted = new BorrowBookRequest(lbms, this.visitorId, bookIds);
                }

                break;
            case "borrowed":
                differentVisitor = tokens.length == 2;

                if (differentVisitor) {
                    requestExecuted = new FindBorrowedBooksRequest(lbms, tokens[1]);
                } else {
                    requestExecuted = new FindBorrowedBooksRequest(lbms, this.visitorId);
                }

                break;
            case "return":
                differentVisitor = tokens.length == 3;

                //return[visitorId],{bookids};
                if (differentVisitor) {
                    String differentVisitorId = tokens[1];
                    String[] bookArray = tokens[2].substring(1, tokens[2].length() - 1).split(",");
                    ArrayList<Integer> booksToReturn = new ArrayList<>();

                    for (String b : bookArray) {
                        booksToReturn.add(Integer.parseInt(b));
                    }

                    requestExecuted = new ReturnBookRequest(lbms, differentVisitorId, booksToReturn);
                } else {
                    String[] bookArray = tokens[1].split(",");
                    ArrayList<Integer> booksToReturn = new ArrayList<>();

                    for (String b : bookArray) {
                        booksToReturn.add(Integer.parseInt(b));
                    }

                    requestExecuted = new ReturnBookRequest(lbms, this.visitorId, booksToReturn);
                }

                break;
            case "service":
                // TODO missing parameters not handled
                String service = tokens[1];

                requestExecuted = new SetBookInfoServiceRequest(lbms, this.visitorId, service);
                break;

            case "pay":
                differentVisitor = tokens.length == 3;
                double amount = Double.parseDouble(tokens[1]);

                if (differentVisitor) {
                    requestExecuted = new PayFineRequest(lbms, tokens[2], amount);
                } else {
                    requestExecuted = new PayFineRequest(lbms, this.visitorId, amount);
                }
                break;

            case "search":
                if (tokens.length < 2) {
                    ArrayList<String> missingParameters = new ArrayList<String>();
                    missingParameters.add(0, "title");

                    requestExecuted = new MissingParamsRequest("Book Store Search", missingParameters);
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

                    requestExecuted = new BookStoreSearchRequest(lbms, title, authors, isbn, publisher, sortOrder, lbms.getBookService());
                }
                break;

            case "buy":
                int quantity = Integer.parseInt(tokens[1]);
                ArrayList<Integer> idsFromQuery = new ArrayList<>();
                for (int i = 2; i < tokens.length; i++) {
                    int id = Integer.parseInt(tokens[i]);

                    if (lbms.getBookFromQueryId(id) != null) {
                        idsFromQuery.add(id);
                    }
                }
                requestExecuted = new BookPurchaseRequest(lbms, quantity, idsFromQuery);
                break;

            case "advance":
                if (tokens.length < 2) {
                    ArrayList<String> missingParameters = new ArrayList<String>();
                    missingParameters.add(0, "number-of-days");

                    requestExecuted = new MissingParamsRequest("Advance Time", missingParameters);
                } else {
                    if (tokens.length == 2) {
                        //just days
                        int days = Integer.parseInt(tokens[1]);

                        requestExecuted = new AdvanceTimeRequest(lbms, days);

                    } else if (tokens.length == 3) {
                        int days = Integer.parseInt((tokens[1]));
                        int hours = Integer.parseInt((tokens[2]));

                        requestExecuted = new AdvanceTimeRequest(lbms, days, hours);
                    }
                }
                break;

            case "datetime":
                requestExecuted = new CurrentTimeRequest(lbms);
                break;

            case "report":
                requestExecuted = new LibraryStatisticsReportRequest(lbms);
                break;
        }

        if (requestExecuted != null) {
            requestExecuted.execute();
            this.undoStack.push(requestExecuted);

            return requestExecuted.response();
        } else {
            return "unrecognized command;";
        }
    }

    /**
     * Returns whether not the given request is undoable
     *
     * @param request the given request
     * @return if the request is undoable or not
     */
    private boolean isUndoable(Request request) {
        return request instanceof UndoableCommand;
    }
}
