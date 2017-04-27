package com.example.salesforce.apex.rest;

import java.io.IOException;
import java.util.logging.Logger;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Andres Canavesi
 */
public class SalesforceApiAuth {

    private static final Logger LOG = Logger.getLogger(SalesforceApiAuth.class.getName());

    private final String username;
    private final String password;
    /**
     * Get your security token from your org, go to (in classic) go to My settings --> Persona --> Reset my security
     * token
     */
    private final String securityToken;
    /**
     * The connected app client id.
     */
    private final String clientId;
    /**
     * The connected app client secret
     */
    private final String clientSecret;

    /**
     * After a success login we will use this access token to do requests that require authentication
     */
    private String accessToken;
    /**
     * The base org instance URL to do the authenticated requests. Example: "https://na35.salesforce.com"
     */
    private String instanceUrl;

    /**
     *
     * @param username
     * @param password
     * @param securityToken
     * @param clientId
     * @param clientSecret
     */
    public SalesforceApiAuth(String username, String password, String securityToken, String clientId, String clientSecret) {
        this.username = username;
        this.password = password;
        this.securityToken = securityToken;
        this.clientId = clientId;
        this.clientSecret = clientSecret;

    }

    /**
     *
     * @throws SalesforceLoginException
     * @throws IOException
     * @throws SalesforceApiException
     */
    public void login() throws SalesforceLoginException, IOException, SalesforceApiException {
        LOG.info("Doing login...");
        /**
         * ############### The password must be "password+securityToken" ###############
         */
        StringBuilder url = new StringBuilder();
        url.append("https://login.salesforce.com/services/oauth2/token?grant_type=password")
                .append("&client_id=")
                .append(clientId)
                .append("&client_secret=")
                .append(clientSecret)
                .append("&username=")
                .append(username)
                .append("&password=")
                .append(password)
                .append(securityToken);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            // Login requests must be POSTs
            HttpPost httpPost = new HttpPost(url.toString());
            CloseableHttpResponse response = httpClient.execute(httpPost);
            // verify response is HTTP OK
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    throw new SalesforceLoginException("Wrong credentials. " + response.getStatusLine().getReasonPhrase());
                } else {
                    throw new SalesforceApiException(response.getStatusLine().getReasonPhrase());
                }
            }
            String responseString = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = (JSONObject) new JSONTokener(responseString).nextValue();
            accessToken = jsonObject.getString("access_token");
            instanceUrl = jsonObject.getString("instance_url");

            LOG.info("Login ok");
        }
    }

    /**
     *
     * @return
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     *
     * @return
     */
    public String getInstanceUrl() {
        return instanceUrl;
    }

}
