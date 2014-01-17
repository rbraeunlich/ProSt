![ProSt Icon](Logo.png "ProSt")
=====

ProSt or ProzessSteuerung is a project which uses Activiti as process engine, OSGi for modularity and EJBs for client-server communication. "Prost" is the German word for cheers.

Special Thanks to:

* Florian B. for the name
* Katrin P. for the icon

The modules contain a modified version of the Apache Aries Blueprint-Core project. I changed the ReflectionUtils class so it would recognize setters that don't return void. Because you can't compile it without the parent POM I included the target directory containing a compiled jar.
