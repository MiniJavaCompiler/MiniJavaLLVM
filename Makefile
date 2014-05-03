.SUFFIXES:	.class .java .s .jar .exe

DIRS = mjc
CLASSES = javaLLC.class

.PHONY: all git_config clean

all: javaLLC.class

git_config: 
	git config filter.java_code.clean "astyle --options=$(shell pwd)/.javastyle < %f"

javaLLC.class:	javaLLC.java
	javac -classpath . javaLLC.java
	for d in $(DIRS); do (cd $$d && $(MAKE)); done
clean:
	rm $(CLASSES)
	for d in $(DIRS); do (cd $$d && $(MAKE) clean); done
