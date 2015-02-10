Management iPlug
========

The Management iPlug provides an interface between the InGrid Portal database and the InGrid components. Information about partner and provider are manages in the portal admin GUI and stored in the portal database. All InGrid iPlugs need to access those data.


Requirements
-------------

- a running InGrid Software System

Installation
------------

Download from https://dev.informationgrid.eu/ingrid-distributions/ingrid-iplug-management/
 
or

build from source with `mvn package assembly:single`.

Execute

```
java -jar ingrid-iplug-management-x.x.x-installer.jar
```

and follow the install instructions.

Obtain further information at http://www.ingrid-oss.eu/ (sorry only in German)


Contribute
----------

- Issue Tracker: https://github.com/informationgrid/ingrid-iplug-management/issues
- Source Code: https://github.com/informationgrid/ingrid-iplug-management
 
### Set up eclipse project

```
mvn eclipse:eclipse
```

and import project into eclipse.

### Debug under eclipse

### Debug under eclipse

- execute `mvn install` to expand the base web application
- set up a java application Run Configuration with start class `de.ingrid.iplug.management.ManagementIPlug`
- add the VM argument `-Djetty.webapp=src/main/webapp` to the Run Configuration
- add src/main/resources to class path
- the admin gui starts per default on port 8082, change this with VM argument `-Djetty.port=8083`


Support
-------

If you are having issues, please let us know: info@informationgrid.eu

License
-------

The project is licensed under the EUPL license.
