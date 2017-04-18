import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by brendanjones44 on 4/18/17.
 */
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
        String [] request = requestLine.split(",");
        //Slice off the ; in the first word for easier cases in switch statement.
        String firstWord = request[0];
        if (firstWord.endsWith(";")) {
            firstWord = firstWord.substring(0, firstWord.length() - 1);
        }
        String lastWord = request[request.length - 1];
        if (lastWord.endsWith(";")) {
            request[request.length - 1] = lastWord.substring(0, lastWord.length() - 1);
        }
        Request requestExecuted = null;
        switch (firstWord){
            case "arrive":
                requestExecuted = new BeginVisitRequest(this.lbms, this.visitorId);
                break;
            case "depart":
                requestExecuted = new EndVisitRequest(this.lbms, this.visitorId);
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

                    requestExecuted = new LibraryBookSearchRequest(lbms, title, authors, isbn, publisher, sortOrder);
                    break;
                }
            case "borrow":
                ArrayList<String> bookIds = new ArrayList<>();
                for(int i = 1; i < request.length; i++) {
                    bookIds.add(request[i]);
                }
                requestExecuted = new BorrowBookRequest(lbms, this.visitorId, bookIds);
                break;
        }
        if(requestExecuted != null){
            requestExecuted.execute();
            this.undoStack.push(requestExecuted);
            return requestExecuted.response();
        }else if(requestLine.equals("undo;")){
        }else{
            return "unrecognized command;";
        }
        return null;
    }

    private boolean isUndoable(Request request){
        if(request instanceof UndoableCommand){
            return true;
        }

        return false;
    }
}
