#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>


//#define DEBUG_HEAP
//#define DEBUG_GC
//#define DEBUG_ALLOC
//#define DEBUG_VERIFY
#define VERIFY_HEAP

#define WORDSIZE(_bytes_) (((_bytes_ + sizeof(uintptr_t) - 1) / sizeof(uintptr_t)))

extern void Main_main();
extern void MJCStatic_init();
extern void MJCStatic_roots();

/* forward declaration(s) */
void gc_copy();
uintptr_t *forward(uintptr_t *p);
void die_w_msg(char *m) {
  printf("FATAL: %s\n", m);
  exit(-1);
}


#ifndef bool
typedef int bool;
enum bool { false, true };
#endif

/* Notes (Mark Anderson Smith)
  Modify array and object structures
    [type] <- points to global space defn of object type
    [length or fwd location]
    [data] <- alloc pointer 'a' points to data
*/
#define DEF_HEAP_SIZE   500     // heap size in words (4k bytes)
#define OBJ_HEADER_SIZE 2
#define OBJ_HEADER_TYPE_OFFSET   2
#define OBJ_HEADER_FWDPTR_OFFSET 1
#define OBJ_HEADER_SIZE_OFFSET   1
#define OBJ_HEADER_SIZE_OFFSET_FROM_TYPE 1
static const char *ARRAY_HEADER_TYPE = "ARRAYOBJ";
static const char *OBJECT_HEADER_TYPE = "CLASSOBJ";
static const char *OBJECT_FORWARDED = "FORWARDOBJ";



/* Notes (Mark Anderson Smith)
 *  LLVM type structure list on LLVM site:
 *      http://llvm.org/docs/GarbageCollection.html
 *  Notes on the data structures included for reference
*/

/// @brief The map for a single function's stack frame.  One of these is
///        compiled as constant data into the executable for each function.
///
/// Storage of metadata values is elided if the %metadata parameter to
/// @llvm.gcroot is null.
typedef struct {
  int32_t NumRoots;    //< Number of roots in stack frame.
  int32_t NumMeta;     //< Number of metadata entries.  May be < NumRoots.
  const void *Meta[0]; //< Metadata for each root.
} FrameMap;

/// @brief A link in the dynamic shadow stack.  One of these is embedded in
///        the stack frame of each function on the call stack.
typedef struct {
  struct StackEntry *Next;           //< Link to next stack entry (the caller's).
  const FrameMap *Map;        //< Pointer to constant FrameMap.
  void *Roots[0];             //< Stack roots (in-place array).
} StackEntry;


/// @brief The head of the singly-linked list of StackEntries.  Functions push
///        and pop onto this in their prologue and epilogue.
///
/// Since there is only a global list, this technique is not threadsafe.
extern StackEntry *llvm_gc_root_chain;


// Notes: Mark Anderson Smith
// Since LLVM does not track global roots these must be maintained
// as an independent linked list from the main llvm_gc_root_chain
typedef struct {
  uintptr_t **root;
  struct GlobalRootEntry
      *next;           //< Link to next stack entry (the caller's).
} GlobalRootEntry;
GlobalRootEntry *MJC_gc_global_root_chain = 0;

/*
 * End of LLVM references
 */


/* Heap allocation */
typedef struct space {          /* allocation space data              */
  uintptr_t *start;             /* first word of space                */
  uintptr_t *end;               /* one past last word of space        */
  uintptr_t *avail;             /* pointer to next word into which to create an object  */
} space;


/* allocate and initialize descriptor structure and storage for a space */
space *initspace(size_t words) {
  space *s = calloc(1, sizeof(space));
  if (!s) {
    die_w_msg("insufficient memory to init heap");
  }

  s->start = calloc(words, sizeof(uintptr_t));
  s->end = s->start + words;
  s->avail = s->start;
  return s;
}

/* initialize semi-space for copy-collection */
int tospace =  1;
space *tofrom_heap[2];

/* set heap pointer                           */
space *heap = 0;             // pointer to current half heap

/* initialize the heap
 * heap_size is total desired size (in words)
 */
void initialize_heap(size_t heap_words) {
  tofrom_heap[0] = initspace(heap_words);
  tofrom_heap[1] = initspace(heap_words);
  heap = tofrom_heap[0];  // set initial heap location

#ifdef DEBUG_HEAP
  printf("initialize_heap called, size=%zu\n", heap_words);
#endif
}

/* allocate a heap record of specified number of words */
uintptr_t *heapalloc(size_t words) {
  uintptr_t *p;

  // printf during heap alloc expensive/slow for large heap sizes
  // only enable when testing small heaps
#ifdef DEBUG_ALLOC
  printf("heapalloc request num words: %zu\n", words);
#endif

  if (heap->end - heap->avail < words) {
    gc_copy();  // garbage collect and switch heap to alt heap space
    if (heap->end - heap->avail < words) {

#ifdef DEBUG_HEAP
      printf("heap end: %zu, heap avail: %zu\n", (uintptr_t)heap->end,
             (uintptr_t)heap->avail);
#endif

      die_w_msg("out of heap space");
    }
  }
  p = heap->avail;
  heap->avail += words;
  return p;
}

/* debug function for listing heap content   */
void print_heap(space *h) {
  uintptr_t *pos = h->start;
  printf("current heap (start: %zu, avail: %zu)\n", (uintptr_t)h->start,
         (uintptr_t)h->avail);
  while (pos < h->avail) {
    if ((uintptr_t)OBJECT_HEADER_TYPE == (uintptr_t)(*pos)
        || (uintptr_t)ARRAY_HEADER_TYPE == (uintptr_t)(*pos)
        || (uintptr_t)OBJECT_FORWARDED == (uintptr_t)(*pos)) {
      printf("    heap @ %zu = %s\n", (uintptr_t)pos, (char*)*pos);
    } else {
      printf("    heap @ %zu = %zu\n", (uintptr_t)pos, *pos);
    }

    pos++;
  }
}

/* perform heap verification.  Die if inconsistencies exist within the heap*/
void verify_heap(space *h) {
  uintptr_t *end_block;
  uintptr_t *pos = h->start;

#ifdef DEBUG_VERIFY
  printf("verify heap (start: %zu, avail: %zu)\n", (uintptr_t)h->start,
         (uintptr_t)h->avail);
#endif

  while (pos < h->avail) {
    // beginning of block should be flagged as a header type
    if ((uintptr_t)OBJECT_HEADER_TYPE == (uintptr_t)(*pos)
        || (uintptr_t)ARRAY_HEADER_TYPE == (uintptr_t)(*pos)) {
      end_block = pos + *(pos + OBJ_HEADER_SIZE_OFFSET_FROM_TYPE) + OBJ_HEADER_SIZE;
      pos++;
      while (pos < end_block) {
        // inside alloc block there should not be another header
        if ((uintptr_t)OBJECT_HEADER_TYPE == (uintptr_t)(*pos)
            || (uintptr_t)ARRAY_HEADER_TYPE == (uintptr_t)(*pos)) {
          printf("heap corruption at %zu, unexpected %s\n", (uintptr_t)pos, (char*)*pos);
          die_w_msg("heap corruption");
        }
        pos++;
      }
    } else {
      printf("heap corruption at %zu, unexpected %s\n", (uintptr_t)pos, (char*)*pos);
      die_w_msg("heap corruption");
    }
  }

#ifdef DEBUG_VERIFY
  printf("verify heap: PASS\n");
#endif
}


/******************************************************************
*
*  Runtime operations for heap allocation and management
*
******************************************************************/
void MJC_globalRoot(uintptr_t **root) {
#ifdef DEBUG_GC
  printf("MJC_globalRoot called with root %zu\n", (uintptr_t)*root);
#endif

  // initialize heap on first use
  if (!heap) {
    initialize_heap(DEF_HEAP_SIZE);
  }

  // insert new roots at head of chain
  GlobalRootEntry *nextRoot = (GlobalRootEntry*)(malloc(sizeof(GlobalRootEntry)));
  nextRoot->root = root;
  nextRoot->next = (struct GlobalRootEntry *)MJC_gc_global_root_chain;
  MJC_gc_global_root_chain = nextRoot;

  return;
}

/* Object and field operations */

uintptr_t *MJC_allocObject(size_t size) {

  // initialize heap on first use
  // TODO: allow user to dynamically set heap size
  if (!heap) {
    initialize_heap(DEF_HEAP_SIZE);
  }

#ifdef DEBUG_ALLOC
  printf("MJC alloc object size %zu bytes (%zu words)\n", size, WORDSIZE(size));
#endif

  // size request was # bytes - convert to words
  size = WORDSIZE(size);

  uintptr_t *obj = heapalloc(size + OBJ_HEADER_SIZE) + OBJ_HEADER_SIZE;

  // store a class type into the header for later use
  obj[-OBJ_HEADER_TYPE_OFFSET] = (uintptr_t)(OBJECT_HEADER_TYPE);
  obj[-OBJ_HEADER_SIZE_OFFSET] = size;

  // initialize remaining alloc space to 0
  memset(obj, 0, size * sizeof(uintptr_t));

  return obj;
}

void MJC_putc(char c) {
  printf("%c", c);
}

/* Array operations */

uintptr_t *MJC_allocArray(int32_t elements, int32_t element_size) {

  // size request was # bytes - convert to words
  if (WORDSIZE(element_size) > 1) {
    die_w_msg("Array element should be 1 word");
  }
  int32_t size = elements * WORDSIZE(element_size);

  if (size < 0) {
    die_w_msg("Negative array size request");
  }

  // initialize heap on first use
  if (!heap) {
    initialize_heap(DEF_HEAP_SIZE);
  }

#ifdef DEBUG_ALLOC
  printf("MJC alloc array: num elements %d, size %d bytes (%d words)\n", elements,
         element_size, (int)WORDSIZE(element_size));
#endif

  // array header includes type and size
  uintptr_t *a = heapalloc(size + OBJ_HEADER_SIZE) + OBJ_HEADER_SIZE;

  // store the marker "ARRAY" to indicate an array object
  a[-OBJ_HEADER_TYPE_OFFSET] = (uintptr_t)(ARRAY_HEADER_TYPE);
  a[-OBJ_HEADER_SIZE_OFFSET] = size;

  // initialize remaining alloc space to 0
  memset(a, 0, size * sizeof(uintptr_t));

  return a;
}

int32_t array_length(uintptr_t *a) {
  return (int32_t)(*(a - OBJ_HEADER_SIZE_OFFSET));
}

char * MJC_arrayIndex(char *a, int32_t index) {
  return (char *)(((uintptr_t *)a) + index);
}

/* gc uses during scan phase to determine if this is pointer to be forwarded */
bool is_heap_pointer(uintptr_t *p) {
  // is this pointing back into the old heap?
  // and is the object pointed to a class obj?
  return (p >= heap->start && p < heap->avail
          && ((uintptr_t)OBJECT_HEADER_TYPE == (uintptr_t)(*(p - OBJ_HEADER_TYPE_OFFSET))
              || (uintptr_t)ARRAY_HEADER_TYPE == (uintptr_t)(*(p - OBJ_HEADER_TYPE_OFFSET))
              || (uintptr_t)OBJECT_FORWARDED == (uintptr_t)(*(p - OBJ_HEADER_TYPE_OFFSET))));
}

/* gc uses during scan phase to determine if this pointer has already been forwarded */
bool is_fwd_pointer(uintptr_t *p) {
  return ((uintptr_t)OBJECT_FORWARDED == (uintptr_t) * (p -
          OBJ_HEADER_TYPE_OFFSET));
}

void gc_printroots() {
  // forward global roots
  for (GlobalRootEntry *grootpos =  MJC_gc_global_root_chain; grootpos != NULL;
       grootpos = (GlobalRootEntry*)grootpos->next) {
    printf("Global Root Chain : %zu\n", *(uintptr_t *)grootpos->root);
  }


  // forward roots
  for (StackEntry *rootpos =  llvm_gc_root_chain; rootpos != NULL;
       rootpos = (StackEntry*)rootpos->Next) {

    for (int i = 0; i < rootpos->Map->NumRoots; i++) {
      printf("GcRoot Chain Map %d : %zu\n", i, (uintptr_t)rootpos->Roots[i]);
    }
  }

}
/******************************************************************
*
*  Basic copying collection (cheney algorithm)
*
******************************************************************/
void gc_copy() {

#ifdef DEBUG_GC
  printf("gc: initiated with tospace=%d\n", tospace);
  printf("    llvm root chain=%zu\n", (uintptr_t)llvm_gc_root_chain);
  if (llvm_gc_root_chain) {
    printf("    llvm root chain base stack num roots = %d\n",
           llvm_gc_root_chain->Map->NumRoots);
  }
  printf("    heap start=%zu, heap pos=%zu\n", (uintptr_t)heap->start,
         (uintptr_t)heap->avail);
  printf("    tospace=%zu\n", (uintptr_t)tofrom_heap[tospace]->start);
#endif

  uintptr_t *scan = tofrom_heap[tospace]->start;

#ifdef DEBUG_HEAP
  printf("gc: before heap copy\n");
  print_heap(heap);
#endif

  // forward global roots
  for (GlobalRootEntry *grootpos =  MJC_gc_global_root_chain; grootpos != NULL;
       grootpos = (GlobalRootEntry*)grootpos->next) {

    if (is_heap_pointer(*grootpos->root)) {
#ifdef DEBUG_GC
      printf("gc: forwarding global root object at %zu\n",
             (uintptr_t)(grootpos->root));
#endif

      *(grootpos->root) = forward(*grootpos->root);
    }

  }


  // forward roots
  for (StackEntry *rootpos =  llvm_gc_root_chain; rootpos != NULL;
       rootpos = (StackEntry*)rootpos->Next) {

    for (int i = 0; i < rootpos->Map->NumRoots; i++) {

      if (is_heap_pointer((uintptr_t *)(rootpos->Roots[i]))) {
#ifdef DEBUG_GC
        printf("gc: forwarding root object at %zu\n", (uintptr_t)rootpos->Roots[i]);
#endif

        rootpos->Roots[i] = forward((uintptr_t*)rootpos->Roots[i]);
      }
    }
  }


  uintptr_t *free = tofrom_heap[tospace]->avail;


  // scan the tospace (make sure to increment by size)
  while (scan < free) {

    if (is_heap_pointer((uintptr_t*)*scan)) {

#ifdef DEBUG_GC
      printf("gc: forwarding scanned object at %zu\n", (uintptr_t)*scan);
#endif

      // forward the object pointed to by scan
      *scan = (uintptr_t)forward((void *)(*scan));

      // update free
      free = tofrom_heap[tospace]->avail;
    }

    scan++;
  }

  // reset the 'soon-to-be-old' heap position
  heap->avail = heap->start;
  // reset heap to other half
  heap = tofrom_heap[tospace];

#ifdef DEBUG_HEAP
  printf("gc: after heap copy\n");
  print_heap(heap);
#endif

#ifdef VERIFY_HEAP
  verify_heap(heap);
#endif

  // reset tospace index
  tospace = 1 - tospace;
}

uintptr_t *forward(uintptr_t *p) {
  if (!p) {
    return NULL;
  }
  uintptr_t *fwdptr =
    (p) - OBJ_HEADER_SIZE;  // forward pointer start at offset -2

  /* check if object/array is already in the tospace */
  if (fwdptr >= tofrom_heap[tospace]->start
      && fwdptr < tofrom_heap[tospace]->end) {
    return fwdptr;
  } else if (*fwdptr == (uintptr_t)OBJECT_FORWARDED) {
    uintptr_t * fwd_addr =
      (uintptr_t*) * ((p) - OBJ_HEADER_FWDPTR_OFFSET); // forwarded pointer location
#ifdef DEBUG_GC
    printf("gc: scanned object previously forwarded - updating to %zu\n",
           (uintptr_t)fwd_addr);
#endif

    return fwd_addr;
  } else {
    size_t size =
      *((p) - OBJ_HEADER_SIZE_OFFSET) +
      OBJ_HEADER_SIZE; // size pointer is value at offset -1
    // copy data
#ifdef DEBUG_GC
    printf("  memcpy %zu to %zu, size=%zu\n", (uintptr_t)fwdptr,
           (uintptr_t)tofrom_heap[tospace]->avail, size);
#endif
    memcpy(tofrom_heap[tospace]->avail, fwdptr, size * sizeof(uintptr_t));

    // mark the object as forwarded and it's new location
    *(p - OBJ_HEADER_TYPE_OFFSET) = (uintptr_t)OBJECT_FORWARDED;
    *(p - OBJ_HEADER_FWDPTR_OFFSET) = (uintptr_t)(tofrom_heap[tospace]->avail +
                                      OBJ_HEADER_SIZE);

    // reset fwd pointer to it's new location
    fwdptr = tofrom_heap[tospace]->avail + OBJ_HEADER_SIZE;

    // reset available space
    tofrom_heap[tospace]->avail += size;

    return fwdptr;
  }
}

/* This is just copying a c_string into a Java char array */
void load_string(int n, int * length, char * src, char * dst) {
  *length = n;
  strncpy(dst, src, n);
}

void printc(char c) {
  printf("%c", c);
}

int main() {
  //    printf("Starting:\n");
  MJCStatic_roots();
  MJCStatic_init();
  Main_main();
  //    printf("Finishing (%d words allocated).\n",freeHeap);
  return 0;
}

