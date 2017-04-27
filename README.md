# salesforce-apex-rest
An example about how to consume a Salesforce api rest, in particular "/services/apexrest/"

The example shows how to authenticate using username and password to get the access token and then how to do a request to a Salesforce webservice

# steps

Create an apex class in your org and paste the content of "HelloRest.cls"

You will need a Salesforce connected app: 
Go to your org --> Setup -->Build-->Create-->Apps-->Connected apps-->New. 

Enable oauth settings.
Callback URL: http://localhost (we write somthing because is a required field, we won't use it)
Selected OAuth Scopes: Access and manage your data (api)

After saving we get the Consumer Key (client id) and Consumer Secret (client secret)

Open the class SalesforceApiRest, fill your credentials in the constructor and run!

