#
set -xe

/opt/jdk15/bin/javac \
  --module-path jre/fx \
  --add-modules=javafx.controls \
  app/HelloFX.java

jre/bin/java \
  --module-path jre/fx \
  --add-modules=javafx.controls \
  -Djdk.gtk.verbose=true \
  -Dprism.verbose=true \
  -Dprism.debug=true \
  -cp app HelloFX

