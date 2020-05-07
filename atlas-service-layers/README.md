# AERIUS monitor service mock project

This project contains various mock services used by other projects.

## Building .war container

Build the project with maven `mvn clean install`. This will build a war file. This war file can be deployed to a Java webserver, like Tomcat.

## Run spring-boot independently

This project can also be run independently using `mvn spring-boot:run`, which packages a super-jar and runs it on an embedded Tomcat server. This goal will reload the application when source files change, so it is advised to use this approach while developing.

## IDE and configuration

Import this project in an IDE.

Create a Maven run configuration.

Point it to this project and give it the `spring-boot:run` goal.

Run it.

Exactly like the above, the spring-boot dev tools will reload/redeploy the application as source files change. 

### Further notes

- Note the properties file in src/main/resources/application.properties.

- To configure logging, add a `logback.xml` to your classpath, the spring-boot packager will include it through the system scope.  