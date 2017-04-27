package com.example.salesforce.apex.rest;

/**
 *
 * @author Andres Canavesi
 */
public class SalesforceApiException extends Exception {

    /**
     *
     * @param message
     */
    public SalesforceApiException(String message) {
        super(message);
    }

}
