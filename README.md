MiniJavaLLVM
============

MiniJava frontend for LLVM

Mark Smith and Mitch Souders
CS510 - Advanced Programming Language Implementations

Project Notes

This directory contains the top-level project for the cross-language system built on the initial miniJava program created by Professor Mark Jones of PSU with adaptations by Mark Smith and Mitch Souders for outputting code compatible with LLVM.

The project supports a miniJava source program which can be run in interpreted mode, compiled to 32-bit x86 and/or output to LLVM runtime.

The top level directory contains this README file and wrappers for running the underlying compiler/interpreter.  The main program entry point is 

Usage:

    Interpret mode:
      java javaLLC -interp inputFile

    Compile to x86 (32-bit only)
      java javaLLC -compileX86 inputFile outputFile

    Compile to llvm bitcode
      java javaLLC -compileLLVM inputFile outputFile

    Compile by llvm to native
      java javaLLC -compileLLx86 inputFile outputFile


Directory Contents

    mjc       Contains sources for miniJava intepreter/compiler with 
              modifications to support output to llvm assembly
 
    units     Directory containing unit tests and results

    example   Project starter with hello world and llvm usage examples

