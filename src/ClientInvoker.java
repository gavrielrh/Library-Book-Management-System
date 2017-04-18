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
            case "redo":
                if(this.redoStack.size() > 0){
                    UndoableCommand commandToRedo = redoStack.pop();
                    if(commandToRedo.redo()){
                        return "redo,success;";
                    }
                }else{
                    return "redo,cannot-redo;";
                }
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
