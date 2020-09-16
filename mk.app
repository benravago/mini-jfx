#
set -xe

/opt/jdk15/bin/javac \
  --module-path dist/jre/fx \
  --add-modules=javafx.controls \
  app/HelloFX.java

dist/jre/bin/java \
  --module-path dist/jre/fx \
  --add-modules=javafx.controls \
  -Djdk.gtk.verbose=true \
  -Dprism.verbose=true \
  -Dprism.debug=true \
  -cp app HelloFX

