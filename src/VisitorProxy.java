import java.util.Collection;
import java.util.HashSet;

/**
 * Created by brendanjones44 on 4/18/17.
 */
public class VisitorProxy implements PermissionProxy{
    private ClientInvoker clientInvoker;
    Account account;

    public VisitorProxy(ClientInvoker clientInvoker){
        this.clientInvoker = clientInvoker;
    }

    public String handleCommand(String input){
        String[] array = input.split(",");
        String commandName = array[0];
        if(commandName.contains(";")){
            commandName = commandName.substring(0, commandName.length() - 1);
        }
        if(hasPermission(commandName)){
            //check to make sure the visitor isn't attempting to check someone else in/out.
            if(commandName.equals("arrive") || commandName.equals("depart")){
                if(array.length > 1){
                    return "not-authorized;";
                }
            }
            return clientInvoker.handleCommand(input);
        }else{
            return "not-authorized;";
        }
    }

    public boolean hasPermission(String commandName){
        HashSet<String> acceptableCommands = new HashSet<>();
        acceptableCommands.add("arrive");
        acceptableCommands.add("info");
        acceptableCommands.add("borrow");
        acceptableCommands.add("depart");
        acceptableCommands.add("undo");
        acceptableCommands.add("redo");
        return acceptableCommands.contains(commandName);
    }
}
