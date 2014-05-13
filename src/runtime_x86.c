#include <stdio.h>
#include <stdlib.h>

extern void Main_main();

void System_out(int x) {
    printf("out: %d\n",x);
}

#define HEAPLEN 4000
int freeHeap = 0;
int heap[HEAPLEN];

int* new_object(int* vt) {
    int  size   = (*vt) / 4;
    int* newObj = heap+freeHeap;
    if (size+freeHeap >= HEAPLEN) {
        fprintf(stderr,"Out of memory!");
        exit(1);
    }
    freeHeap += size;
    *newObj   = (int)vt;
    return newObj;
}

int* new_array(int size) {
    int* newArr = heap+freeHeap;
    if (size+freeHeap >= HEAPLEN) {
        fprintf(stderr,"Out of memory!");
        exit(1);
    }
    freeHeap += size;
    return newArr;

}

int main() {
    printf("Starting:\n");
    Main_main();
    printf("Finishing (%d words allocated).\n",freeHeap);
    return 0;
}

