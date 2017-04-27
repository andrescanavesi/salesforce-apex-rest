//*** COPY THIS APEX CLASS TO YOUR ORG ***

@RestResource(urlMapping='/hellorest')
global class HelloRest {

    @HttpPost
    global static String echo(String name) {
        String echo = 'Hi, '+name;
        System.debug('Echo: '+echo);
        return echo;
    }
}
