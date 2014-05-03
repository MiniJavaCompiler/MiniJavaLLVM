#include <stdio.h>

// Initial hello world project
//
//    compile to native executable
//      clang hello.c -o hello
//
//    compile to llvm bitcode
//      clang -emit-llvm hello.c -c -o hello.bc
//
//    compile to llvm assembly
//      clang -emit-llvm hello.c -S -o hello.ll
//
//    run llvm in interpreted mode
//      lli hello.bc
//
//    compile from llvm bitcode to native
//      llc hello.bc -o hello.s
//      gcc hello.s -o hello
//
//
//
//

int main (int argc, char *argv[])
{
  printf("hello world\n");
  return 0;
}
