/*
 * File: LibraryBookSearchRequest.java
 * Author: Gavriel Rachael-Homann (gxr2329@rit.edu)
 *
 * Request parsing utility functions
 */

/**
 * Provides request parsing util
 */
public class RequestParser {

    /**
     * Returns tokens found in request
     *
     * @param request the request string
     * @return the tokens array
     */
    public static String[] getTokens(String request) {
        String[] tokens = request.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
        }

        return tokens;
    }
}
