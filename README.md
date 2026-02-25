## A minimal jre with javafx

This is an experiment about making a minimal runtime for javafx.

In this case, 'minimal' means being able to start a simple HelloWorld javafx application with the least javafx and jre modules
which turned out to be javafx.base, javafx.graphics, javafx.controls, and java.base (with a couple of other small jdk modules).

Some patches were needed to remove the java.desktop dependency in javafx.base. Also, four small classes were copied from java.desktop:java.beans to com/sun/javafx/property/adapter.

Some patches were also applied to javafx.graphics to remove non-Linux dependencies as well as printer support (which relied on java.awt.print).

To build the runtime:
1. check out the repository and install a [jdk](https://jdk.java.net/) and javafx [sdk](https://gluonhq.com/products/javafx/)
2. edit the <code>Makefile</code> as needed; as it is, the jdk is assumed to be at <code>/opt/jdk17</code> and the javafx sdk at <code>/opt/jfx17</code>
3. run <code>"make sdk"</code> to build the <code>./sdk</code> directory which contains a minimal javac environment as well as the basic jfx jars
4. if there are no errors, run the <code>app/hello.sdk</code> script which will compile and run a small app which should show a page that looks something like
   this <p> ![HelloWorldFX](app/hello.png)
5. run <code>"make rt"</code> to build the <code>./rt</code> directory which contains a minimal java runtime
6. run the </code>app/hello.rt</code> script to run the previously built HelloFX class file again   
   
TODO:
1. port [Harmony](https://harmony.apache.org) CUPS/IPP print backend to be a PrintPipeline implementation
2. try to build [Processing4](https://github.com/processing/processing4) and dependencies [without java.awt](https://github.com/processing/processing4/wiki/Exorcising-AWT) 
