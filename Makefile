
JAVA_SDK = /opt/jdk17
JAVAFX_SDK = /opt/jfx17

SRC = $(JAVAFX_SDK)/src.zip

BIN = $(JAVA_SDK)/bin

JAR = $(BIN)/jar
JAVAC = $(BIN)/javac -nowarn -XDignore.symbol.file
        # -XDignore... to suppress "warning: Unsafe is ..." message


JLINK = $(JAVA_SDK)/bin/jlink

JRE_MODULES = jdk.jfr,jdk.unsupported,jdk.zipfs
JDK_MODULES = jdk.compiler,jdk.jartool,$(JRE_MODULES)
              # jdk.jlink adds jmod, jlink, jimage tools


unjar  = cat alt/$(1)/xargs.unzip | xargs -r unzip -q -d bin/$(1) $(JAVAFX_SDK)/lib/$(1).jar
rmdir  = cat alt/$(1)/xargs.rmdir | xargs -t -r -I{} rm -fr src/$(1)/{}
rmfile = cat alt/$(1)/xargs.rm | xargs -t -r -I{} rm src/$(1)/{}
patch  = find alt/$(1) -name '*.patch' -exec patch -p0 -i {} ';'


jfx: lib/javafx.base.jar lib/javafx.graphics.jar lib/javafx.controls.jar


sdk: jdk jfx etc
	mkdir -p $@
	cp -rl $</* $@
	cp -rl etc/* $@
	cp -rl lib $@/jfx

rt: jre jfx etc
	mkdir -p $@
	cp -rl $</* $@
	cp -rl etc/* $@
	cp -rl lib $@/jfx


jre:
	$(JLINK) --output $@ --add-modules $(JRE_MODULES)

jdk:
	$(JLINK) --output $@ --add-modules $(JDK_MODULES)


etc:
	mkdir -p etc/lib
	cp -a -t etc/lib $(JAVAFX_SDK)/lib/javafx.properties
	cp -a -t etc/lib $(JAVAFX_SDK)/lib/lib{decora_sse,glass,glassgtk2,glassgtk3,javafx_font,javafx_font_freetype,javafx_font_pango,javafx_iio,prism_common,prism_es2,prism_sw}.so
	mkdir -p etc/legal
	cp -a -t etc/legal $(JAVAFX_SDK)/legal/javafx.{base,controls,graphics}


lib/javafx.base.jar: bin/javafx.base
	mkdir -p lib
	$(JAR) -c -f $@ -C $< .

lib/javafx.graphics.jar: bin/javafx.graphics
	mkdir -p lib
	$(JAR) -c -f $@ -C $< .

lib/javafx.controls.jar: bin/javafx.controls
	mkdir -p lib
	$(JAR) -c -f $@ -C $< .


bin/javafx.base: src/javafx.base
	mkdir -p $@
	$(eval L = $(subst javafx,,$(@F)))
	find $< -name '*.java' > bin/$(L)
	$(JAVAC) -d $@ -sourcepath $< @bin/$(L)

bin/javafx.graphics: src/javafx.graphics lib/javafx.base.jar
	mkdir -p $@
	$(eval L = $(subst javafx,,$(@F)))
	find $< -name '*.java' > bin/$(L)
	$(JAVAC) -p lib -d $@ -sourcepath $< @bin/$(L)
	$(call unjar,$(@F))

bin/javafx.controls: src/javafx.controls lib/javafx.base.jar
	mkdir -p $@
	$(eval L = $(subst javafx,,$(@F)))
	find $< -name '*.java' > bin/$(L)
	$(JAVAC) -p lib -d $@ -sourcepath $< @bin/$(L)
	$(call unjar,$(@F))


src/javafx.base:
	mkdir -p src
	unzip -q -d src $(SRC) '$(@F)/*'
	$(call patch,$(@F))

src/javafx.graphics:
	mkdir -p src
	unzip -q -d src $(SRC) '$(@F)/*'
	$(call patch,$(@F))
	$(call rmdir,$(@F))
	$(call rmfile,$(@F))

src/javafx.controls:
	mkdir -p src
	unzip -q -d src $(SRC) '$(@F)/*'


clean:
	rm -fr src bin lib etc
	rm -fr jre jdk
	rm -fr sdk rt

