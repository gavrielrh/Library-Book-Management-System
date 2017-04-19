/**
 * Created by Eric on 4/19/2017.
 *
 * Gets the number of clients running in the system
 */
public class CurrentClientRequest implements Request{
    /* have the LBMS be inside the request for getting Id */
    private LBMS lbms;
    int currentClients;

    /**
     * Constructor for ConnectClientRequest
     *
     * @param lbms - the LBMS itself.
     */
    public CurrentClientRequest(LBMS lbms) {
        this.lbms = lbms;
    }

    /**
     * execute sets the clientId to the request for the response
     */
    @Override
    public void execute() {
        this.currentClients = this.lbms.getNumClient();
    }

    /**
     * response is the response to executing the command with the client Id
     *
     * @return - String of response
     */
    @Override
    public String response() {
        return Integer.toString(this.currentClients);
    }

}
