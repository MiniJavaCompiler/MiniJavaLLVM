.SUFFIXES:	.class .java .s .jar .jacc .MF .exe

CLASSES = Compiler.class compiler/*.class \
	  lexer/*.class syntax/*.class checker/*.class \
	  codegen/*.class interp/*.class

all: mjc.jar

Compiler.class:	syntax/MjcParser.java syntax/MjcTokens.java
	javac -classpath . Compiler.java

syntax/MjcParser.java syntax/MjcTokens.java:
	jacc syntax/Mjc.jacc

mjc.jar: Compiler.class MANIFEST.MF
	#-rm mjc.jar
	jar cfm mjc.jar MANIFEST.MF $(CLASSES)

MANIFEST.MF:
	echo "Manifest-Version: 1.0"  >  MANIFEST.MF
	echo "Main-Class: Compiler"   >> MANIFEST.MF

ts.exe:	ts.s runtime.c   # for Windows
	gcc -m32 -o ts.exe ts.s runtime.c

ts:	ts.s runtime.c   # For Linux/mac
	gcc -m32 -o ts ts.s runtime.c

ts.s:	ts.j Compiler.class
	java -classpath . Compiler ts.j ts.s

clean:
	rm mjc.jar MANIFEST.MF $(CLASSES)
