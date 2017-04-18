/**
 * Created by brendanjones44 on 4/18/17.
 */
public interface PermissionProxy {
    boolean hasPermission(String commandName);
    String handleCommand(String input);
}
