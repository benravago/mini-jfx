
# ln -s $(/usr/libexec/java_home) java-jdk-27

JDK = ./java-jdk-27
SDK = ./javafx-sdk-27

JRE_MODULES = jdk.jfr,jdk.unsupported,jdk.zipfs
JDK_MODULES = jdk.compiler,jdk.jartool,$(JRE_MODULES)
              # jdk.jlink adds jmod, jlink, jimage tools

javac = \
  mkdir -p bin ; \
  find src/$(1) -name '*.java' > files.$(1) ; \
  ./jdk/bin/javac --release 25 \
    --module $(1) --module-source-path src \
    -d bin -nowarn -Xmaxwarns 9999 -Xmaxerrs 9999 \
    @files.$(1) ; \
  rm -f files.$(1) ;

jar = \
  mkdir -p lib ; \
  ./jdk/bin/jar cvf lib/$(1) -C bin/$(basename $(1)) . ;

patch = find alt/$(1) -name '*.patch' -exec patch -p0 -i {} ';'
unzip = unzip -q $(SDK)/src.zip $(shell cat alt/$(1)/inc) -x $(shell cat alt/$(1)/exc) -d src 


.DEFAULT_GOAL := lib/javafx.controls.jar


jdk: 
	$(JDK)/bin/jlink --output $@ --add-modules $(JDK_MODULES)
    
jre: 
	$(JDK)/bin/jlink --output $@ --add-modules $(JRE_MODULES)
    

lib/javafx.controls.jar: bin/javafx.controls
	$(call jar,$(@F))

bin/javafx.controls: src/javafx.controls bin/javafx.graphics
	$(call javac,$(@F))

src/javafx.controls:
	$(call unzip,$(@F))
	$(call patch,$(@F))


lib/javafx.graphics.jar: bin/javafx.graphics
	$(call jar,$(@F))

bin/javafx.graphics: src/javafx.graphics bin/javafx.base
	$(call javac,$(@F))

src/javafx.graphics:
	$(call unzip,$(@F))
	$(call patch,$(@F))


lib/javafx.base.jar: bin/javafx.base
	$(call jar,$(@F))

bin/javafx.base: src/javafx.base bin/jfx.desktop
	$(call javac,$(@F))

src/javafx.base:
	$(call unzip,$(@F))
	$(call patch,$(@F))


lib/jfx.desktop.jar: bin/jfx.desktop
	$(call jar,$(@F))

bin/jfx.desktop: src/jfx.desktop
	$(call javac,$(@F))

src/jfx.desktop: jdk
	unzip -q $(JDK)/lib/src.zip $(shell cat alt/$(@F)/inc) -d src 
	mv -v src/{java,$(basename $(@F))}$(suffix $(@F))
	$(call patch,$(@F))


clean:
	rm -fr src bin lib

