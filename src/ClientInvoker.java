import java.util.ArrayList;
import java.util.Stack;

public class ClientInvoker {
    private LBMS lbms;
    private Account account;
    private String visitorId;
    private Stack<Request> undoStack;
    private Stack<UndoableCommand> redoStack;

    public ClientInvoker(LBMS lbms, Account account){
        this.lbms = lbms;
        this.account = account;
        this.visitorId = account.getVisitorId();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public String handleCommand(String requestLine){
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

        switch (firstWord){

            case "arrive":
                if(tokens.length == 1) {
                    requestExecuted = new BeginVisitRequest(this.lbms, this.visitorId);
                }else{
                    requestExecuted = new BeginVisitRequest(this.lbms, tokens[1]);
                }
                break;
            case "depart":
                if(tokens.length == 1) {
                    requestExecuted = new EndVisitRequest(this.lbms, this.visitorId);
                }else{
                    requestExecuted = new EndVisitRequest(this.lbms, tokens[1]);
                }
                break;
            case "undo":
                boolean success = false;
                if(this.undoStack.size() > 0){
                    Request requestUndo = undoStack.pop();
                    if(isUndoable(requestUndo)){
                        UndoableCommand undoableCommand = (UndoableCommand) requestUndo;
                        this.redoStack.push(undoableCommand);
                        success = undoableCommand.undo();
                    }
                    if(success) {
                        return "undo,success;";
                    }
                }else{
                    return "undo,cannot-undo;";
                }
                break;
            case "redo":
                if(this.redoStack.size() > 0){
                    UndoableCommand commandToRedo = redoStack.pop();
                    if(commandToRedo.redo()){
                        return "redo,success;";
                    }
                }else{
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
                String[] bookIdsArray = tokens[1].substring(1, tokens[1].length()-1).split(",");
                ArrayList<String> bookIds = new ArrayList<>();
                for(int i = 0; i < bookIdsArray.length; i++) {
                    bookIds.add(bookIdsArray[i]);
                }
                if(differentVisitor){
                    requestExecuted = new BorrowBookRequest(lbms, tokens[2], bookIds);
                }else {
                    requestExecuted = new BorrowBookRequest(lbms, this.visitorId, bookIds);
                }
                break;
            case "borrowed":
                differentVisitor = tokens.length == 2;
                if(differentVisitor){
                    requestExecuted = new FindBorrowedBooksRequest(lbms, tokens[1]);
                }else{
                    requestExecuted = new FindBorrowedBooksRequest(lbms, this.visitorId);
                }
                break;
            case "return":
                differentVisitor = tokens.length == 3;
                //return[visitorId],{bookids};
                if(differentVisitor){
                    String differentVisitorId = tokens[1];
                    String[] bookArray = tokens[2].substring(1, tokens[2].length()-1).split(",");
                    ArrayList<Integer> booksToReturn = new ArrayList<>();
                    for(String b : bookArray){
                        booksToReturn.add(Integer.parseInt(b));
                    }
                    requestExecuted = new ReturnBookRequest(lbms, differentVisitorId, booksToReturn);
                }else{
                    String[] bookArray = tokens[1].substring(1, tokens[1].length()-1).split(",");
                    ArrayList<Integer> booksToReturn = new ArrayList<>();
                    for(String b : bookArray){
                        booksToReturn.add(Integer.parseInt(b));
                    }
                    requestExecuted = new ReturnBookRequest(lbms, this.visitorId, booksToReturn);
                }
                break;




        }
        if(requestExecuted != null){
            requestExecuted.execute();
            this.undoStack.push(requestExecuted);
            return requestExecuted.response();
        }else{
            return "unrecognized command;";
        }
    }

    private boolean isUndoable(Request request){
        if(request instanceof UndoableCommand){
            return true;
        }
        return false;
    }
}
