/* * MiniJava Compiler - X86, LLVM Compiler/Interpreter for MiniJava.
 * Copyright (C) 2014, 2008 Mitch Souders, Mark A. Smith, Mark P. Jones
 *
 * MiniJava Compiler is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * MiniJava Compiler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MiniJava Compiler; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

#include <stdio.h>
#include <stdlib.h>

extern void Main_main();
extern void MJCStatic_init();

void System_out(int x) {
  printf("%d\n", x);
}

#define HEAPLEN (8 * 1024 * 1024)
int freeHeap = 0;
int heap[HEAPLEN];

void MJC_globalRoot(void *root) {
  // not used as no gc for x86
  return;
}

int* MJC_allocObject(int size) {
  int* newObj = heap + freeHeap;
  if (size + freeHeap >= HEAPLEN) {
    fprintf(stderr, "Out of memory!");
    exit(1);
  }
  freeHeap += size;
  return newObj;
}

void MJC_die() {
  exit(-1);
}

int* MJC_allocArray(int elementSize, int len) {
  int size = elementSize * len;
  int* newArr = heap + freeHeap;
  if (size + freeHeap >= HEAPLEN) {
    fprintf(stderr, "Out of memory!");
    exit(1);
  }
  freeHeap += size;
  return newArr;

}

void MJC_putc(char c) {
  printf("%c", c);
}
int main() {
  //    printf("Starting:\n");
  MJCStatic_init();
  Main_main();
  //    printf("Finishing (%d words allocated).\n",freeHeap);
  return 0;
}

