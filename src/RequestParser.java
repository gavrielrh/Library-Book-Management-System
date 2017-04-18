/*
 * File: RequestParser.java
 * Author: Gavriel Rachael-Homann (gxr2329@rit.edu)
 *
 * Util for parsing tokens from requests.
 */

public class RequestParser {
    /**
     * Returns tokens found in request
     * @param request the request string
     * @return the tokens array
     */
    public String[] getTokens(String request) {
        String[] tokens = request.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        for(int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
        }

        return tokens;
    }

    public static void main(String[] args) {
        RequestParser parser = new RequestParser();
        String request = "search, \"Harry\", *,{\"cow\"},* ,test";
        String[] tokens = parser.getTokens(request);
        for(String token : tokens) {
            System.out.println("Token: " + token);
        }
    }
}
