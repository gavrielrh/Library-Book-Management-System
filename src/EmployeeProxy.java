/**
 * Created by brendanjones44 on 4/18/17.
 */
public class EmployeeProxy implements ClientProxy {

    private SystemInvoker systemInvoker;

    public EmployeeProxy(SystemInvoker systemInvoker){
        this.systemInvoker = systemInvoker;
    }
}
