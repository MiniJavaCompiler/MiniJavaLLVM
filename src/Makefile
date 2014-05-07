JAVA_SRC := \
	Compiler.java \
	$(wildcard compiler/*.java) \
	$(wildcard lexer/*.java) \
	$(wildcard syntax/*.java) \
	$(wildcard checker/*.java \
	$(wildcard codegen/*.java) \
	$(wildcard interp/*.java) \

CLASSES := $(JAVA_SRC:.java=.class)

include ../Makefile.global

all: mjc.jar

Compiler.class:	syntax/MjcParser.java syntax/MjcTokens.java Compiler.java
	javac -classpath $(CLASSPATH) Compiler.java

syntax/MjcParser.java syntax/MjcTokens.java:
	jacc syntax/Mjc.jacc

mjc.jar: $(CLASSES) MANIFEST.MF
	#-rm mjc.jar
	jar cfm mjc.jar MANIFEST.MF $(CLASSES)

MANIFEST.MF:
	echo "Manifest-Version: 1.0"  >  MANIFEST.MF
	echo "Main-Class: Compiler"   >> MANIFEST.MF
	echo "Class-Path: $(LLVM_J_JAR) $(SPRINTABOUT_JAR) $(BRIDJ_JAR) $(ASM_JAR)" >> MANIFEST.MF

ts.exe:	ts.s runtime.c   # for Windows
	gcc -m32 -o ts.exe ts.s runtime.c

ts:	ts.s runtime.c   # For Linux/mac
	gcc -m32 -o ts ts.s runtime.c

ts.s:	ts.j Compiler.class
	java -classpath . Compiler ts.j ts.s

clean:
	rm mjc.jar MANIFEST.MF $(CLASSES)
