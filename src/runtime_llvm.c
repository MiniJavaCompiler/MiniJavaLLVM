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
#include <stdint.h>
#include <string.h>
#include <stdarg.h>

//#define DEBUG_HEAP
//#define DEBUG_GC
//#define DEBUG_ALLOC
//#define DEBUG_VERIFY
#define VERIFY_HEAP

#define WORDSIZE(_bytes_) (((_bytes_ + sizeof(uintptr_t) - 1) / sizeof(uintptr_t)))

extern void Main_main();
extern void MJCStatic_init();
extern void MJCStatic_roots();


#ifndef bool
typedef int bool;
enum bool { false, true };
#endif

typedef int GCGenHeap;
enum GCGenHeap { none, nursery, old, reserve};

/* forward declaration(s) */
void print_heap_region();
void print_heap();
void gc_minor();
void gc_major();
void gc_copy(GCGenHeap fromHeap, GCGenHeap toHeap);
uintptr_t *forward(uintptr_t *p, GCGenHeap fromHeap, GCGenHeap toHeap);
GCGenHeap which_heap(uintptr_t * p);
void die_w_msg(char *m, ...) {
  va_list argptr;
  va_start(argptr, m);
  printf("FATAL: ");
  vprintf(m, argptr);
  printf("\n");
  va_end(argptr);
  exit(-1);
}
void MJC_die() {
  exit(-1);
}

/* Notes (Mark Anderson Smith)
  Modify array and object structures
    [type] <- points to global space defn of object type
    [length or fwd location]
    [data] <- alloc pointer 'a' points to data
*/
#define DEF_HEAP_SIZE   3000     // heap size in words (4k bytes)
#define MAX_OLD_TO_HEAP_RATIO    3
#define OBJ_HEADER_SIZE          2
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

GlobalRootEntry *MJC_gc_assignment_chain = 0;

/*
 * End of LLVM references
 */



/*             Heap allocation
*     [**old**|**reserve**|***nursery***|****free*****]
* start       end_old     end_reserve   end_nursery   end_space
*/
typedef struct space {          /* allocation space data              */
  uintptr_t *start;             /* first word of space                */
  uintptr_t *old_avail;         /* one past last pointer of old avail space - only used during major copy collection */
  uintptr_t *end_old;           /* one past last pointer of old space */
  uintptr_t *end_reserve;       /* one past last pointer of reserve space */
  uintptr_t *reserve_avail;     /* pointer to next word into which to copy into reserve space */
  uintptr_t *nursery_avail;     /* pointer to next word to copy into nursery for new objects */
  uintptr_t *end;               /* one past last word of space        */
} space;

/* set heap pointer */
space *heap = 0;             // pointer to current heap


/* allocate and initialize descriptor structure and storage for a space */
space *initspace(size_t words) {
  space *s = calloc(1, sizeof(space));
  if (!s) {
    die_w_msg("insufficient memory to init heap");
  }

  s->start = calloc(words, sizeof(uintptr_t));
  s->end_old = s->start;
  s->old_avail = s->start;
  s->reserve_avail = s->end_old;
  s->end_reserve = s->start + (words / 2);
  s->nursery_avail = s->end_reserve;
  s->end = s->start + words;
  return s;
}

/* initialize the heap
 * heap_size is total desired size (in words)
 */
void initialize_heap(size_t heap_words) {
  heap = initspace(heap_words);

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

  if (heap->end - heap->nursery_avail < words) {
    // perform minor collection
    gc_minor();
    if ((heap->end_old - heap->start) > ((heap->end - heap->start) /
                                         MAX_OLD_TO_HEAP_RATIO)) {
      // old generation has become too large - time to collect
      gc_major();
    }
    // verify heap space is sufficient to continue
    // after major collection - if old space is larger than reserve than cannot continue
    // if nursery is not large enough to allocate the word - cannot continue
    if ((heap->end_old - heap->start) > (heap->end_reserve - heap->end_old)
        || (heap->end - heap->nursery_avail < words)) {

      print_heap_region();
      printf("heap nursery avail: %zu\n", (uintptr_t)heap->nursery_avail);

      die_w_msg("out of heap space");
    }
  }
  p = heap->nursery_avail;
  heap->nursery_avail += words;

#ifdef DEBUG_ALLOC
  printf("allocated from nursery at %zu\n", (uintptr_t)p);
#endif

  return p;
}

/* debug function for listing heap content   */
void print_heap_region() {
  printf("    heap old     (%zu - %zu)\n", (uintptr_t)heap->start,
         (uintptr_t)heap->end_old);
  printf("    heap reserve (%zu - %zu)\n", (uintptr_t)heap->end_old,
         (uintptr_t)heap->end_reserve);
  printf("    heap nursery (%zu - %zu)\n", (uintptr_t)heap->end_reserve,
         (uintptr_t)heap->end);
}

void print_heap() {
  uintptr_t *pos = heap->start;
  printf("    old heap     (start: %zu, end:   %zu)\n", (uintptr_t)heap->start,
         (uintptr_t)heap->end_old);

  while (pos < heap->end_old) {
    if ((uintptr_t)OBJECT_HEADER_TYPE == (uintptr_t)(*pos)
        || (uintptr_t)ARRAY_HEADER_TYPE == (uintptr_t)(*pos)
        || (uintptr_t)OBJECT_FORWARDED == (uintptr_t)(*pos)) {
      printf("    heap @ %zu = %s\n", (uintptr_t)pos, (char*)*pos);
    } else {
      printf("    heap @ %zu = %zu\n", (uintptr_t)pos, *pos);
    }
    pos++;
  }

  printf("    reserve heap (start: %zu, end:   %zu)\n", (uintptr_t)heap->end_old,
         (uintptr_t)heap->end_reserve);

  while (pos < heap->reserve_avail) {
    if ((uintptr_t)OBJECT_HEADER_TYPE == (uintptr_t)(*pos)
        || (uintptr_t)ARRAY_HEADER_TYPE == (uintptr_t)(*pos)
        || (uintptr_t)OBJECT_FORWARDED == (uintptr_t)(*pos)) {
      printf("    heap @ %zu = %s\n", (uintptr_t)pos, (char*)*pos);
    } else {
      printf("    heap @ %zu = %zu\n", (uintptr_t)pos, *pos);
    }
    pos++;
  }
  pos = heap->end_reserve;

  printf("    nursery heap (start: %zu, avail: %zu)\n",
         (uintptr_t)heap->end_reserve,
         (uintptr_t)heap->nursery_avail);

  while (pos < heap->nursery_avail) {
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

/* perform heap verification after collection.  Die if inconsistencies exist within the heap*/
void verify_reserve_heap() {
  uintptr_t *end_block;
  uintptr_t *pos = heap->end_old;

#ifdef DEBUG_VERIFY
  printf("verify reserve heap (start: %zu, avail: %zu)\n",
         (uintptr_t)heap->end_old,
         (uintptr_t)heap->reserve_avail);
#endif

  while (pos < heap->reserve_avail) {
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
  printf("verify reserve heap: PASS\n");
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

void MJC_assign(uintptr_t **assign) {
#ifdef DEBUG_GC
  printf("MJC_assign called with assign %zu\n", (uintptr_t)*assign);
#endif
  if (!heap) {
    initialize_heap(DEF_HEAP_SIZE);
  }

  GCGenHeap heap = which_heap((uintptr_t *)assign);
  if (heap == nursery || heap == none) {
    /* if it's in the nursery, there has to be a gcroot */
    /* if it's not in the heap, who cares? */
  } else {
    GlobalRootEntry *nextAssign = (GlobalRootEntry*)(malloc(sizeof(
                                    GlobalRootEntry)));
    nextAssign->root = assign;
    nextAssign->next = (struct GlobalRootEntry *)MJC_gc_assignment_chain;
    MJC_gc_assignment_chain = nextAssign;
  }
  return;
}

void nuke_assigns() {
#ifdef DEBUG_GC
  printf("nuking assignments\n");
#endif
  GlobalRootEntry * assign = MJC_gc_assignment_chain;
  while (assign) {
    GlobalRootEntry * next = (GlobalRootEntry *)assign->next;
    free(assign);
    assign = next;
  }
  MJC_gc_assignment_chain = NULL;
#ifdef DEBUG_GC
  printf("finished nuking assignments\n");
#endif
}

void prune_assigns(GCGenHeap heap) {
#ifdef DEBUG_GC
  printf("pruning assignments\n");
#endif
  /* remove assigns in areas that have just been restructured */
  GlobalRootEntry ** assign = &MJC_gc_assignment_chain;
  while (*assign) {
    GlobalRootEntry ** next = (GlobalRootEntry **) & (*assign)->next;
    if (which_heap(*(*assign)->root) == heap) {
      free(*assign);
      *assign = *next;
    } else {
      assign = next;
    }
  }
#ifdef DEBUG_GC
  printf("finished pruning assignments\n");
#endif
}

/* Object and field operations */

uintptr_t *MJC_allocObject(size_t size) {

  // initialize heap on first use
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
  int32_t size = WORDSIZE(elements * element_size);

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

GCGenHeap which_heap(uintptr_t * p) {
  if (p >= heap->end_reserve && p < heap->nursery_avail) {
    return nursery;
  } else if (p >= heap->start && p < heap->end_old) {
    return old;
  } else if (p >= heap->end_old && p < heap->end_reserve) {
    return reserve;
  } else {
    return none;
  }
}

/* gc uses during scan phase to determine if this is pointer to be forwarded */
bool is_heap_pointer(uintptr_t *p, GCGenHeap region) {
  bool in_heap_range = false;

  if (which_heap(p) == region) {
    in_heap_range = true;
  }

  // and is the object pointed to a class obj?
  return (in_heap_range
          && ((uintptr_t)OBJECT_HEADER_TYPE == (uintptr_t)(*(p - OBJ_HEADER_TYPE_OFFSET))
              || (uintptr_t)ARRAY_HEADER_TYPE == (uintptr_t)(*(p - OBJ_HEADER_TYPE_OFFSET))
              || (uintptr_t)OBJECT_FORWARDED == (uintptr_t)(*(p - OBJ_HEADER_TYPE_OFFSET))));
}

/* gc uses during scan phase to determine if this pointer has already been forwarded */
bool is_fwd_pointer(uintptr_t *p) {
  return ((uintptr_t)OBJECT_FORWARDED == (uintptr_t) * (p -
          OBJ_HEADER_TYPE_OFFSET));
}

/* determine new midpoint for reserve & nursery. Bias to reserve as larger */
uintptr_t heap_reserve_nursery_midpoint(void) {
  return (uintptr_t)((heap->end - heap->end_old) / 2) + 1;
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
*  Simple generational collection (appel algorithm)
*
******************************************************************/
void gc_minor() {

#ifdef DEBUG_GC
  printf("gc: minor collection initiated with nursery start %zu\n",
         (uintptr_t)(heap->end_reserve));
#endif


#ifdef DEBUG_HEAP
  printf("gc: before heap copy\n");
  print_heap();
#endif


  // collect from nursery to reserve
  gc_copy(nursery, reserve);
  prune_assigns(reserve);

#ifdef VERIFY_HEAP
  verify_reserve_heap();
#endif

  // adjust heap pointers after collection
  // the old region is now redefined to the extent of the used reserve
  heap->end_old = heap->reserve_avail;

  // reset partition of the reserve and nursery to half of non-old region
  heap->end_reserve = heap->end_old + heap_reserve_nursery_midpoint();
  heap->nursery_avail = heap->end_reserve;

#ifdef DEBUG_HEAP
  printf("gc: after heap copy\n");
  print_heap_region();
  print_heap();
#endif


}


void gc_major() {

#ifdef DEBUG_GC
  printf("gc: major collection initiated with end old region %zu\n",
         (uintptr_t)(heap->end_old));
#endif

#ifdef DEBUG_HEAP
  printf("gc: before heap copy: stage 1 major collection\n");
  print_heap();
#endif

  // collect from old to reserve
  gc_copy(old, reserve);

  // adjust heap pointers after collection
  heap->end_reserve = heap->reserve_avail;

#ifdef VERIFY_HEAP
  verify_reserve_heap();
#endif


#ifdef DEBUG_HEAP
  printf("gc: before heap copy: stage 2 major collection\n");
  print_heap();
#endif

  // block move reserve region back to old heap
  heap->old_avail = heap->start;
  gc_copy(reserve, old);

  nuke_assigns();

  // adjust heap pointers after collection
  heap->end_old = heap->old_avail;
  heap->reserve_avail = heap->end_old;

  // reset partition of the reserve and nursery to half of non-old region
  heap->end_reserve = heap->end_old + heap_reserve_nursery_midpoint();
  heap->nursery_avail = heap->end_reserve;

#ifdef DEBUG_HEAP
  printf("gc: after heap copy\n");
  print_heap_region();
  print_heap();
#endif

}


/******************************************************************
*
*  Basic copying collection (modified cheney algorithm)
*
******************************************************************/
void gc_copy(GCGenHeap fromHeap, GCGenHeap toHeap) {

#ifdef DEBUG_GC
  printf("gc: llvm root chain=%zu\n", (uintptr_t)llvm_gc_root_chain);
  if (llvm_gc_root_chain) {
    printf("gc: llvm root chain base stack num roots = %d\n",
           llvm_gc_root_chain->Map->NumRoots);
  }
  print_heap_region();
#endif


  uintptr_t *scan = 0;
  if (reserve == toHeap) {
    scan = heap->end_old;
  } else {
    scan = heap->start;
  }


  // forward global roots
  for (GlobalRootEntry *grootpos =  MJC_gc_global_root_chain; grootpos != NULL;
       grootpos = (GlobalRootEntry*)grootpos->next) {

    if (is_heap_pointer(*grootpos->root, fromHeap)) {
#ifdef DEBUG_GC
      printf("gc: forwarding global root object at %zu\n",
             (uintptr_t)(*grootpos->root));
#endif

      *(grootpos->root) = forward(*grootpos->root, fromHeap, toHeap);
    } else {
#ifdef DEBUG_GC
      printf("gc:  global root not forwarded (wrong heap gen or type): %zu\n",
             (uintptr_t)(*grootpos->root));
#endif
    }

  }

  for (GlobalRootEntry *assignpos =  MJC_gc_assignment_chain; assignpos != NULL;
       assignpos = (GlobalRootEntry*)assignpos->next) {

    if (is_heap_pointer(*assignpos->root, fromHeap)) {
#ifdef DEBUG_GC
      printf("gc: forwarding assignment root object at %zu\n",
             (uintptr_t)(*assignpos->root));
#endif

      *(assignpos->root) = forward(*assignpos->root, fromHeap, toHeap);
    } else {
#ifdef DEBUG_GC
      printf("gc:  assignment not forwarded (wrong heap gen or type): %zu\n",
             (uintptr_t)(*assignpos->root));
#endif
    }
  }

  // forward roots
  for (StackEntry *rootpos =  llvm_gc_root_chain; rootpos != NULL;
       rootpos = (StackEntry*)rootpos->Next) {

    for (int i = 0; i < rootpos->Map->NumRoots; i++) {

      if (is_heap_pointer((uintptr_t *)(rootpos->Roots[i]), fromHeap)) {
#ifdef DEBUG_GC
        printf("gc: forwarding root object at %zu\n", (uintptr_t)rootpos->Roots[i]);
#endif

        rootpos->Roots[i] = forward((uintptr_t*)rootpos->Roots[i], fromHeap, toHeap);
      } else {
#ifdef DEBUG_GC
        printf("gc: root not forwarded (wrong heap gen or type): %zu\n",
               (uintptr_t)rootpos->Roots[i]);
#endif
      }
    }
  }

  //printf("heap before scan\n");
  //print_heap();

  uintptr_t *free = 0;
  if (reserve == toHeap) {
    free = heap->reserve_avail;
  } else {
    free = heap->old_avail;
  }


  // scan the tospace (make sure to increment by size)
  while (scan < free) {

    if (is_heap_pointer((uintptr_t*)*scan, fromHeap)) {

#ifdef DEBUG_GC
      printf("gc: forwarding scanned object at %zu\n", (uintptr_t)*scan);
#endif

      // forward the object pointed to by scan
      *scan = (uintptr_t)forward((void *)(*scan), fromHeap, toHeap);

      // update free
      if (reserve == toHeap) {
        free = heap->reserve_avail;
      } else {
        free = heap->old_avail;
      }
    }

    scan++;
  }
}

/*********************************************
 * forward copies from the space specified to
 * destination space.  This is usually the
 * reserve space except in the 2nd half of the
 * major collection where it is copied back
 * from the reserve region to the old region
 *********************************************/
uintptr_t *forward(uintptr_t *p, GCGenHeap fromHeap, GCGenHeap toHeap) {
  if (!p) {
    return NULL;
  }

  uintptr_t *heap_start = 0;
  uintptr_t *heap_end = 0;
  uintptr_t *heap_avail = 0;
  if (old == toHeap) {
    heap_start = heap->start;
    heap_end = heap->end_old;
    heap_avail = heap->old_avail;
  } else if (reserve == toHeap) {
    heap_start = heap->end_old;
    if (old == fromHeap) {
      heap_end = heap->end; // set to heap->end for copies from old to reserve
    } else {
      heap_end = heap->end_reserve;
    }
    heap_avail = heap->reserve_avail;
  }

  uintptr_t *fwdptr = (p) -
                      OBJ_HEADER_SIZE;  // forward pointer start at offset -2

  /* check if object/array is already in the proper heap space */
  if (fwdptr >= heap_start && fwdptr < heap_end) {
    return fwdptr;
  } else if (*fwdptr == (uintptr_t)OBJECT_FORWARDED) {
    uintptr_t * fwd_addr = (uintptr_t*) * ((p) -
                                           OBJ_HEADER_FWDPTR_OFFSET); // forwarded pointer location

#ifdef DEBUG_GC
    printf("gc: scanned object previously forwarded - updating to %zu\n",
           (uintptr_t)fwd_addr);
#endif

    return fwd_addr;

  } else {
    size_t size = *((p) - OBJ_HEADER_SIZE_OFFSET) +
                  OBJ_HEADER_SIZE; // size pointer is value at offset -1


    // this condition should never occur.  If it does it indicates a flaw
    // in the space allocation algorithm and should be considered a fatal
    // (unrecoverable) error
    if (heap_avail + size > heap_end) {
      if (old == toHeap) {
        die_w_msg("insufficient space for collection to 'old' heap region");
      } else {
        die_w_msg("insufficient space for collection to 'reserve' heap region");
      }
    }

    // copy data
#ifdef DEBUG_GC
    printf("  memcpy %zu to %zu, size=%zu\n", (uintptr_t)fwdptr,
           (uintptr_t)heap_avail, size);
#endif

    memcpy(heap_avail, fwdptr, size * sizeof(uintptr_t));

    // reset fwd pointer to it's new location
    fwdptr = heap_avail + OBJ_HEADER_SIZE;

    // mark the object as forwarded and it's new location
    *(p - OBJ_HEADER_TYPE_OFFSET) = (uintptr_t)OBJECT_FORWARDED;
    *(p - OBJ_HEADER_FWDPTR_OFFSET) = (uintptr_t)fwdptr;


    // reset global heap markers available space
    if (reserve == toHeap) {
      heap->reserve_avail += size;
    } else {
      heap->old_avail += size;
    }

    return fwdptr;
  }
}

/* This is just copying a c_string into a Java char array */
void load_string(int n, int * length, char * src, char * dst) {
  if (length == 0 || src == 0 || dst == 0) {
    die_w_msg("load_string called with invalid args");
  }

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

