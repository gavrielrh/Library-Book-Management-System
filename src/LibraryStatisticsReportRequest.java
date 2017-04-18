/*
 * Filename: LibraryStatisticsReportRequest.java
 * @author - Brendan Jones, bpj1651@rit.edu
 *
 * LibraryStatisticsReportRequest reprsents a ConcreteCommand for the Request subystem
 * Executing does nothing, but the response has the library info.
 */

/* imports */

import java.util.concurrent.TimeUnit;

public class LibraryStatisticsReportRequest implements Request {

    /* have the LBMS be in the Request for querying info */
    private LBMS lbms;

    /**
     * Constructor for LibraryStatisticsReportRequest
     *
     * @param lbms - the LBMS itself
     */
    public LibraryStatisticsReportRequest(LBMS lbms) {
        this.lbms = lbms;
    }

    @Override
    public void execute() {
        //do nothing, executing this does no action with the LBMS.
    }

    /**
     * response gets the report for the response for the library statistics report.
     *
     * @return - String response of the report.
     */
    @Override
    public String response() {
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

    /**
     * getVisitDuration is a helper method that gets a friendly format of the time duration.
     *
     * @param millis - the amount of ms (long) that the duration is.
     * @return - the String representation of the duration.
     */
    private String getVisitDuration(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
