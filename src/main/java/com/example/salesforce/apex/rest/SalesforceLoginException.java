package com.example.salesforce.apex.rest;

/**
 *
 * @author Andres Canavesi
 */
public class SalesforceLoginException extends Exception {

    /**
     *
     * @param message
     */
    public SalesforceLoginException(String message) {
        super(message);
    }

}
