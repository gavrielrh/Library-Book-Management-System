

/**
 * Created by brendanjones44 on 4/18/17.
 */
public class LogoutRequest implements Request {
    private Client client;
    private LBMS lbms;
    public LogoutRequest(Client client, LBMS lbms){
        this.client = client;
        this.lbms = lbms;
    }
    public void execute(){
        if(this.client.clientLoggedIn()){
            this.client.logout();
        }
    }
    public String response(){
        return "logout,success;";
    }
}
