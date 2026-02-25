## A minimal jre with javafx

This is an experiment about making a minimal runtime for javafx.

In this case, 'minimal' means being able to start a simple HelloWorld javafx application with the least javafx and jre modules
which turned out to be javafx.base, javafx.graphics, javafx.controls, and a couple of bits from the java.core:java.desktop module.

To build:
1. check out the repository and install a java [jdk](https://jdk.java.net/) and javafx [sdk](https://jdk.java.net/)
2. make links to the java-jdk and javafx-sdk directories for the Makefile references
3. edit the <code>Makefile</code> if needed
4. run <code>"make"</code> to build the <code>./lib</code> directory which contains a minimal javafx jars

STATUS:
1. Currently, the patches only allow the javafx.base, javafx.graphics, javafx.controls code to compile without platform specific code (such as for prism and glass)
2. Platform specific code will be supplied via additional modules which will be attached via the ServiceLoader api

TODO:
1. make more notes ...
   
