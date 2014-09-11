# MiniJavaLLVM

## MiniJava frontend for LLVM

[Mark Smith](http://github.com/maspdx) and [Mitch Souders](http://github.com/crzysdrs)

Based on the MiniJava Compiler by [Mark P. Jones](http://github.com/zipwith)

CS510 - Advanced Programming Language Implementations

This project is released under GPL2 as described in [LICENSE](LICENSE).

## Project Notes

This directory contains the top-level project for the cross-language system built on the initial miniJava program created by Professor Mark Jones of PSU with adaptations by Mark Smith and Mitch Souders for outputting code compatible with LLVM.

The project supports a miniJava source program which can be run in interpreted mode, compiled to 32-bit x86 and/or output to LLVM runtime.

## Installation

MiniJava requires the following dependencies on Ubuntu:

    ant ubuntu_depends

* libjna-java
* llvm-3.5-dev
* clang-3.5
* gcc-4.7-multilib
* g++-4.7-multilib
* junit4
* ant-contrib
* libcommons-cli-java

The MiniJava compiler can be built with:

    ant jar

## Usage:

```
java -jar build/jar/mjc.jar -help
usage: java -jar mjc.jar -i <INPUT> (--x86 <OUT>|--LLVM <OUT>|--llvm)
 -h,--help          Print this help message.
 -I,--interp        Run the interpreter.
 -i,--input <arg>   Compile to x86 Assembly.
 -L,--LLVM <arg>    LLVM Bitcode Output Location.
 -l,--llvm          Dump Human Readable LLVM Code.
 -V,--version       Version information.
 -x,--x86 <arg>     x86 Output File Location
```

LLVM can be run in 3 different modes:

* Interpreter 

 MJC will run the provided input MiniJava file and all output will be produced to stdout.
 ```
 java -jar mjc.jar -i input.j --interp
 ```

* X86 Compiler

 MJC will produce an x86 assembly .s file which can be compiled against the MJC runtime.

 ```
 java -jar mjc.jar -i input.j --x86 output.s
 gcc -m32 src/runtime_x86.c output.s -o x86_executable
 ```

* LLVM Compiler

 The MJC will produce a bitcode file with --LLVM or just dump the IR with --llvm.

 ```
java -jar mjc.jar -i input.j --LLVM output.bc
llc output.bc -filetype=obj -o output.o
gcc -lc output.o build/runtime/runtime_llvm.o -o llvm_executable
```

## Project Structure

```
src/         Contains sources for miniJava interpreter/compiler with 
             modifications to support output to llvm assembly
 
unitTests/   Directory containing unit tests and output
llvm-j/      Git submodule for LLVM-J library.
runTests.py  Script to run all existing test infrastructure.
             Also can be invoked via "ant test"

```

Please see [README_MJC](README_MJC) for details of the directory structure of the MJC.
