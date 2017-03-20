import java.text.SimpleDateFormat;

/**
 * Created by brendanjones44 on 3/20/17.
 */
public class CurrentTimeRequest implements Request {
    private LBMS lbms;
    private String time;
    private String date;

    public CurrentTimeRequest(LBMS lbms){
        this.lbms = lbms;
        this.time = null;
        this.date = null;
    }

    public void execute(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        this.time = formatter.format(this.lbms.getTime());
        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy/MM/dd");
        this.date = formatterDate.format(this.lbms.getTime());
    }

    public String response(){
        return "datetime," + this.date + "," + this.time + ";";
    }
}
