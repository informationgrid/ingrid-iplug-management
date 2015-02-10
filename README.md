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

Obtain further information at https://dev.informationgrid.eu/


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

TBD

Support
-------

If you are having issues, please let us know: info@informationgrid.eu

License
-------

The project is licensed under the EUPL license.
