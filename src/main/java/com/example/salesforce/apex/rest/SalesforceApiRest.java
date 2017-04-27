package com.example.salesforce.apex.rest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 *
 * @author Andres Canavesi
 */
public class SalesforceApiRest {

    private static final Logger LOG = Logger.getLogger(SalesforceApiRest.class.getName());

    private final SalesforceApiAuth apiAuth;

    /**
     * A main just for testing
     *
     * @param args
     */
    public static void main(String[] args) {
        SalesforceApiRest apiRest = new SalesforceApiRest();
        try {
            apiRest.echo("Andres");
        } catch (SalesforceApiException | SalesforceLoginException | IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     *
     */
    public SalesforceApiRest() {
        String username = "";
        String password = "";
        String securityToken = "";
        String clientId = "";
        String clientSecret = "";

        this.apiAuth = new SalesforceApiAuth(username, password, securityToken, clientId, clientSecret);
    }

    /**
     * Calls our Salesforce webservice to say hello.
     *
     * @param name
     * @throws SalesforceLoginException
     * @throws IOException
     * @throws SalesforceApiException
     */
    public void echo(String name) throws SalesforceLoginException, IOException, SalesforceApiException {
        if (apiAuth.getAccessToken() == null) {
            apiAuth.login();
        }
        LOG.info("Doing hello request...");
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            /**
             * Create a json {"name":"Andres"} . Where "name" is the name of the parameter in our Webservice
             */
            JSONObject update = new JSONObject();
            update.put("name", name);
            String requestUrl = apiAuth.getInstanceUrl() + "/services/apexrest/hellorest";
            HttpPost httpost = new HttpPost(requestUrl);
            httpost.addHeader("Authorization", "Bearer " + apiAuth.getAccessToken());
            httpost.addHeader("Content-type", "application/json");
            StringEntity messageEntity = new StringEntity(update.toString(), ContentType.create("application/json"));
            httpost.setEntity(messageEntity);
            CloseableHttpResponse response = httpclient.execute(httpost);
            // verify response is HTTP OK
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    throw new SalesforceLoginException("Seems the access token is not valid. Do a relogin. Error message from WS: " + response.getStatusLine().getReasonPhrase());
                } else {
                    throw new SalesforceApiException(response.getStatusLine().getReasonPhrase());
                }
            }
            LOG.log(Level.INFO, "Response Status: {0}", response.getStatusLine());
            String result = EntityUtils.toString(response.getEntity());
            LOG.log(Level.INFO, "Response message: {0}", result);
        }

    }
}
