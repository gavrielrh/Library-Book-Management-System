import java.util.concurrent.TimeUnit;

/**
 * Created by brendanjones44 on 3/20/17.
 */
public class LibraryStatisticsReportRequest implements Request {

    private LBMS lbms;

    public LibraryStatisticsReportRequest(LBMS lbms){
        this.lbms = lbms;
    }

    public void execute(){

    }

    public String response(){
        String report = "";
        report += String.format("Number of books currently owned by LBMS: %d\n", lbms.getBookStore().keySet().size());
        report += String.format("Number of unique visitors: %d/\n", lbms.getVisitorIds().size());
        String averageVisitDuration = getVisitDuration(lbms.averageVisitDuration());
        report += String.format("Average time per library visit: %s\n", averageVisitDuration);
        int booksPurchased = this.lbms.getBooks().keySet().size();
        report += String.format("Books purchased: %d\n", booksPurchased);
        report += String.format("Fines collected: %f\n", lbms.getFinesCollected());

        return report;
    }

    private String getVisitDuration(long millis){
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
