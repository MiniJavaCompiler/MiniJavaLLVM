#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>

/* forward declaration(s) */
void gc();
uintptr_t forward(uintptr_t p);

#ifndef bool
typedef int bool;
enum bool { false, true };
#endif

/* Notes (Mark Anderson Smith)
  Modify array and object structures
    [type] <- points to global space defn of object type
    [length]
    [data] <- alloc pointer 'a' points to data
*/
#define DEF_HEAP_SIZE   2000
#define OBJ_HEADER_SIZE 2
static const char *ARRAY_HEADER_TYPE = "ARRAY";
static const char *OBJECT_HEADER_TYPE = "CLASSOBJ";

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
  struct StackEntry *Next;    //< Link to next stack entry (the caller's).
  const FrameMap *Map; //< Pointer to constant FrameMap.
  void *Roots[0];      //< Stack roots (in-place array).
} StackEntry;

/// @brief The head of the singly-linked list of StackEntries.  Functions push
///        and pop onto this in their prologue and epilogue.
///
/// Since there is only a global list, this technique is not threadsafe.
StackEntry *llvm_gc_root_chain;


/* Heap allocation */
typedef struct space {          /* allocation space data              */
  uintptr_t *start;             /* first word of space                */
  uintptr_t *end;               /* one past last word of space        */
  uintptr_t *avail;             /* pointer to next word into which to create an object  */
} space;

/* allocate and initialize descriptor structure and storage for a space */
space *initspace(size_t words) {
  space *s = malloc(sizeof(space));
  // TODO: replace with appropriate exception
  if (!s) exit(-1);
  s->start = malloc(words*sizeof(uintptr_t));
  s->end = s->start + words;
  s->avail = s->start;
  return s;
}

/* initialize semi-space for copy-collection */
int tospace = 1;         // index of the copy-to space, from space is always 'heap'
space *tofrom_heap[2];   // storage of double heap space
uintptr_t *froot;        // TODO: replace with llvm_root objects
uintptr_t **lroot_ptr;
char **roottypes;

/* set heap pointer                           */
space *heap = 0;             // pointer to current half heap






/* initialize the heap 
 * heap_size is total desired size (in words) 
 * first_root points to first entry in array of roots
 * last_root_ptr points to pointer to last entry in array of roots
 * root_types points to first entry in parallel array of type descriptors (same length as root array)
 */
void initialize_heap(size_t heap_words) {
  tofrom_heap[0] = initspace(heap_words);
  tofrom_heap[1] = initspace(heap_words);
  heap = tofrom_heap[0];  // set initial heap location

  #ifdef DEBUG
  printf("initialize_heap called, size=%zu\n", heap_words);  
  #endif
}

/* allocate a heap record of specified number of words */
uintptr_t *heapalloc(size_t words) {
  uintptr_t *p;

  // printf during heap alloc expensive/slow for large heap sizes
  // only enable when testing small heaps
  //#ifdef DEBUG
  //printf("heapalloc request size: %zu\n", words);
  //#endif

  if (heap->end - heap->avail < words) {
    gc();  // garbage collect and switch heap to alt heap space
    if (heap->end - heap->avail < words) {
      #ifdef DEBUG
      printf("heap end: %zu, heap avail: %zu\n", (uintptr_t)heap->end, (uintptr_t)heap->avail);
      #endif
      
      // TODO: replace with appropriate exception 
      exit(-1);
    }
  }
  p = heap->avail;
  heap->avail += words;
  return p;
}

/* debug function for listing heap content   */
void printheap(space *h)
{
  uintptr_t *pos = h->start;
  printf("current heap (start: %zu, avail: %zu)\n", (uintptr_t)h->start, (uintptr_t)h->avail);
  while (pos < h->avail) {
    printf("    heap @ %zu = %zu\n", (uintptr_t)pos, *pos);
    pos++;
  }
}


/****************************************************************** 
*
*  Runtime operations for heap allocation and management
*
******************************************************************/
/* Object and field operations */

uintptr_t *new_object(size_t size) {

  // initialize heap on first use
  // TODO: allow user to dynamically set heap size
  if (!heap) {
    initialize_heap(DEF_HEAP_SIZE);
  }

  uintptr_t *obj = heapalloc(size+OBJ_HEADER_SIZE) + OBJ_HEADER_SIZE;

  // store a class type into the header for later use
  obj[-2] = (uintptr_t)(OBJECT_HEADER_TYPE);
  obj[-1] = size;
  int i;
  for (i = 0; i < size; i++)
    obj[i] = 0;

  return obj;
}

/* Array operations */

uintptr_t *new_array(int32_t size) {

  if (size < 0)
    exit(-1);

  // initialize heap on first use
  // TODO: allow user to dynamically set heap size
  if (!heap) {
    initialize_heap(DEF_HEAP_SIZE);
  }

  // array header includes type and size
  uintptr_t *a = heapalloc(size+OBJ_HEADER_SIZE) + OBJ_HEADER_SIZE;
  // store the marker "ARRAY" to indicate an array object
  a[-2] = (uintptr_t)(ARRAY_HEADER_TYPE);
  a[-1] = size;
  int i;
  for (i = 0; i < size; i++)
    a[i] = 0;
  return a;
}

int32_t array_length(uintptr_t *a) {
  return (int32_t) (*(a-1));
}

void array_store(uintptr_t *a,int32_t index,int32_t v) {
  *(a+index) = (uintptr_t) v;
}

int32_t array_load(uintptr_t *a,int32_t index) {
  return (int32_t) (*(a + index));
}



/* gc uses during scan phase to determine if this is pointer to be forwarded */
bool is_heap_pointer(uintptr_t *p) {
  // is this pointing back into the old heap? 
  // and is the object pointed to a class obj?
  return ((p >= heap->start && p < heap->avail) 
	  && ((uintptr_t)OBJECT_HEADER_TYPE == (uintptr_t)*(p-2)));
}

bool is_object_type(char *type) {
  if (!type) return false;       // null
  if (*type == 'L') return true; // object type
  return (0 == strcmp(type, "[I"));     // int array type
}

/****************************************************************** 
*
*  Basic copying collection (cheney algorithm)
*
******************************************************************/
void gc() {

  #ifdef DEBUG
  printf("gc: initiated with tospace=%d\n", tospace);
  printf("    first root=%zu, last root=%zu\n", (uintptr_t)froot, (uintptr_t)*lroot_ptr);
  printf("    heap start=%zu, heap pos=%zu\n", (uintptr_t)heap->start, (uintptr_t)heap->avail);
  printf("    tospace=%zu\n", (uintptr_t)tofrom_heap[tospace]->start);
  #endif

  uintptr_t *scan = tofrom_heap[tospace]->start;

  //printheap(heap);

  // forward roots
  uintptr_t *rpos = froot; 
  char **rtypes = roottypes;
  while (rpos < *lroot_ptr) {
    if (is_object_type(*rtypes)) {

      #ifdef DEBUG
      printf("gc: forwarding root object %s at %zu\n", *rtypes, (uintptr_t)*rpos);
      #endif
      
      *rpos = forward(*rpos);
     
    }
    rpos++;
    rtypes++;
  }

  uintptr_t *free = tofrom_heap[tospace]->avail;

  // scan the tospace (make sure to increment by size)
  while (scan < free) {

    if (is_heap_pointer((uintptr_t*)*scan)) {
      #ifdef DEBUG
      printf("gc: forwarding scanned object at %zu\n", (uintptr_t)*scan);
      #endif 

      // forward the object pointed to by scan
      *scan = forward(*scan);

      // update free
      free = tofrom_heap[tospace]->avail;

    }

    scan++;
  }

  // reset the 'soon-to-be-old' heap position
  heap->avail = heap->start;
  // reset heap to other half
  heap = tofrom_heap[tospace];
 
  #ifdef DEBUG
  printheap(heap);
  #endif

  // reset tospace index
  tospace = 1 - tospace;
}

uintptr_t forward(uintptr_t p) {
  uintptr_t *fwdptr = (uintptr_t*)(p) - OBJ_HEADER_SIZE;  // forward pointer start at offset -2
  size_t size = *((uintptr_t*)(p) - 1) + OBJ_HEADER_SIZE; // size pointer is value at offset -1

  /* check if object/array is already in the tospace */
  if (fwdptr >= tofrom_heap[tospace]->start && fwdptr < tofrom_heap[tospace]->end) {
    return (uintptr_t)fwdptr;
  }
  else {
    // copy data
    #ifdef DEBUG
    printf("  memcpy %zu to %zu, size=%zu\n", (uintptr_t)fwdptr, (uintptr_t)tofrom_heap[tospace]->avail, size);
    #endif
    memcpy(tofrom_heap[tospace]->avail, fwdptr, size*sizeof(uintptr_t));

    // reset fwd pointer to it's new location
    fwdptr = tofrom_heap[tospace]->avail + OBJ_HEADER_SIZE;

    // reset available space
    tofrom_heap[tospace]->avail += size;

    return (uintptr_t)fwdptr;
  }
}


