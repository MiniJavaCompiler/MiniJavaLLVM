.SUFFIXES:	.class .java .s .jar .exe

MKFILE_PATH := $(abspath $(lastword $(MAKEFILE_LIST)))
MKFILE_DIR := $(dir $(MKFILE_PATH))

DIRS = mjc
CLASSES = javaLLC.class

LIB_JAVA:=$(MKFILE_DIR)lib/java/
JUNIT4_JAR:=/usr/share/java/junit4.jar
BRIDJ_JAR:=$(LIB_JAVA)/bridj-0.6.2.jar
LLVM_J_JAR:=$(LIB_JAVA)/llvm-j.jar
LLVM_HOME:=/usr/
LLVM_J_CLASSPATH:=$(JUNIT4_JAR):$(BRIDJ_JAR):.
CLASSPATH:=$(LLVM_J_JAR):$(LLVM_J_CLASSPATH)

UBUNTU_DEPS:=\
	libjna-java \
	llvm-3.4-dev \
	mercurial \
	gcc-4.7-multilib \
	g++-4.7-multilib \
	junit4 \

.PHONY: all git_config clean llvm_j_test test ubuntu_depends

all: javaLLC.class

git_config: 
	git config filter.java_code.clean "astyle --options=$(MKFILE_DIR)/.javastyle < %f"

test : llvm_j_test
	@echo "Tests Complete"

javaLLC.class:	javaLLC.java $(BRIDJ_JAR) $(LLVM_J_JAR)
	javac -classpath . javaLLC.java
	for d in $(DIRS); do (cd $$d && $(MAKE)); done

ubuntu_depends:
	sudo apt-get install $(UBUNTU_DEPS)

$(LIB_JAVA):
	mkdir -p $(LIB_JAVA)

$(BRIDJ_JAR): | $(LIB_JAVA)
	cd $(LIB_JAVA) && wget -N https://bridj.googlecode.com/files/$(notdir $(BRIDJ_JAR))

$(LLVM_J_JAR) : $(BRIDJ_JAR) | $(LIB_JAVA)
	find llvm-j -iname '*.class' -exec rm {} \;
	cd llvm-j && make clean && make llvm LLVMHOME=$(LLVM_HOME)
	cd llvm-j/src/ && javac -cp $(LLVM_J_CLASSPATH):main/java/:test/java/ test/java/org/llvm/test/*.java 
	cd llvm-j/src/main/java && jar cvf "$(LLVM_J_JAR)" org
	cd llvm-j && git reset --hard

llvm_j_test: $(LLVM_J_JAR)
	cd llvm-j/src/ && java  -cp $(CLASSPATH):test/java/ org.junit.runner.JUnitCore \
		org.llvm.test.TestJIT org.llvm.test.TestLLVM org.llvm.test.TestFactorial
clean:
	rm $(CLASSES)
	rm -rf lib/java
	for d in $(DIRS); do (cd $$d && $(MAKE) clean); done
	find llvm-j -iname '*.class' -exec rm {} \;
	cd llvm-j && make clean 

