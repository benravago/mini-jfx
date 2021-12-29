
JAVA_SDK = /opt/jdk17
JAVAFX_SDK = /opt/jfx17

SRC = $(JAVAFX_SDK)/src.zip
LIB = $(JAVAFX_SDK)/lib
LEGAL = $(JAVAFX_SDK)/legal

BIN = $(JAVA_SDK)/bin

JAR = $(BIN)/jar
JAVAC = $(BIN)/javac -nowarn -XDignore.symbol.file
        # -XDignore... to suppress "warning: Unsafe is ..." message


JLINK = $(JAVA_SDK)/bin/jlink
JMOD = $(JAVA_SDK)/bin/jmod

JRE_MODULES = jdk.jfr,jdk.unsupported,jdk.zipfs
JDK_MODULES = jdk.compiler,jdk.jartool,$(JRE_MODULES)
              # jdk.jlink adds jmod, jlink, jimage tools


patch  = find alt/$(1) -name '*.patch' -exec patch -p0 -i {} ';'
rmfile = xargs -a alt/$(1)/xargs.rm -r -t -I{} rm src/$(1)/{}
rmdir  = xargs -a alt/$(1)/xargs.rmdir -r -t -I{} rm -fr src/$(1)/{}
unjar  = xargs -a alt/$(1)/xargs.unzip -r unzip -q -d bin/$(1)/classes $(LIB)/$(1).jar


sdk: jmods
	$(JLINK) --output $@ -p mod --add-modules $(JDK_MODULES),javafx.controls

rt: jmods
	$(JLINK) --output $@ -p mod --add-modules $(JRE_MODULES),javafx.controls


jre:
	$(JLINK) --output $@ --add-modules $(JRE_MODULES)

jdk:
	$(JLINK) --output $@ --add-modules $(JDK_MODULES)


jmods: mod/javafx.base.jmod mod/javafx.graphics.jmod mod/javafx.controls.jmod

mod/javafx.base.jmod: bin/javafx.base
	mkdir -p $(@D)
	$(JMOD) create --class-path $</classes --legal-notices $</legal --lib $</lib $@

mod/javafx.graphics.jmod: bin/javafx.graphics
	mkdir -p $(@D)
	$(JMOD) create --class-path $</classes --legal-notices $</legal --lib $</lib $@

mod/javafx.controls.jmod: bin/javafx.controls
	mkdir -p $(@D)
	$(JMOD) create --class-path $</classes --legal-notices $</legal $@


jars: lib/javafx.base.jar lib/javafx.graphics.jar lib/javafx.controls.jar

lib/javafx.base.jar: bin/javafx.base
	mkdir -p $(@D)
	$(JAR) -c -f $@ -C $</classes .

lib/javafx.graphics.jar: bin/javafx.graphics
	mkdir -p $(@D)
	$(JAR) -c -f $@ -C $</classes .

lib/javafx.controls.jar: bin/javafx.controls
	mkdir -p $(@D)
	$(JAR) -c -f $@ -C $</classes .


bin/javafx.base: src/javafx.base
	mkdir -p $@/classes
	$(eval L = $(subst javafx,,$(@F)))
	find $< -name '*.java' > bin/$(L)
	$(JAVAC) -d $@/classes -sourcepath $< @bin/$(L)
	mkdir -p $@/legal
	cp -a -t $@/legal $(LEGAL)/$(@F)
	mkdir -p $@/lib
	cp -a -t $@/lib $(LIB)/javafx.properties

bin/javafx.graphics: src/javafx.graphics lib/javafx.base.jar
	mkdir -p $@/classes
	$(eval L = $(subst javafx,,$(@F)))
	find $< -name '*.java' > bin/$(L)
	$(JAVAC) -p lib -d $@/classes -sourcepath $< @bin/$(L)
	$(call unjar,$(@F))
	mkdir -p $@/legal
	cp -a -t $@/legal $(LEGAL)/$(@F)
	mkdir -p $@/lib
	cp -a -t $@/lib $(LIB)/lib{decora_sse,glass,glassgtk2,glassgtk3,javafx_font,javafx_font_freetype,javafx_font_pango,javafx_iio,prism_common,prism_es2,prism_sw}.so

bin/javafx.controls: src/javafx.controls lib/javafx.base.jar lib/javafx.graphics.jar
	mkdir -p $@/classes
	$(eval L = $(subst javafx,,$(@F)))
	find $< -name '*.java' > bin/$(L)
	$(JAVAC) -p lib -d $@/classes -sourcepath $< @bin/$(L)
	$(call unjar,$(@F))
	mkdir -p $@/legal
	cp -a -t $@/legal $(LEGAL)/$(@F)


src/javafx.base:
	mkdir -p $(@D)
	unzip -q -d src $(SRC) '$(@F)/*'
	$(call patch,$(@F))

src/javafx.graphics:
	mkdir -p $(@D)
	unzip -q -d src $(SRC) '$(@F)/*'
	$(call patch,$(@F))
	$(call rmdir,$(@F))
	$(call rmfile,$(@F))

src/javafx.controls:
	mkdir -p $(@D)
	unzip -q -d src $(SRC) '$(@F)/*'


clean:
	rm -fr src bin lib mod
	rm -fr jre jdk
	rm -fr sdk rt

