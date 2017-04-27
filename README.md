# salesforce-apex-rest
An example about how to consume a Salesforce api rest, in particular "/services/apexrest/"

You will need a Salesforce connected app: Go to your org --> Setup -->Build-->Create-->Apps-->Connected apps-->New. 

Enable oauth settings.
Callback URL: http://localhost (we write somthing because is a required field, we won't use it)
Selected OAuth Scopes: Access and manage your data (api)

After saving we get the Consumer Key (client id) and Consumer Secret (client secret)

Open the class SalesforceApiRest, fill your credentials in the constructor and run!

