/**
 * Created by brendanjones44 on 4/18/17.
 */
public class VisitorProxy implements ClientProxy {
    private SystemInvoker systemInvoker;

    public VisitorProxy(SystemInvoker systemInvoker){
        this.systemInvoker = systemInvoker;
    }
}
