.SUFFIXES:	.class .java .s .jar .exe

DIRS = mjc
CLASSES = javaLLC.class

all: javaLLC.class

javaLLC.class:	javaLLC.java
	javac -classpath . javaLLC.java
	for d in $(DIRS); do (cd $$d && $(MAKE)); done
clean:
	rm $(CLASSES)
	for d in $(DIRS); do (cd $$d && $(MAKE) clean); done
