#!/usr/bin/env python3

# MiniJava Compiler - X86, LLVM Compiler/Interpreter for MiniJava.
# Copyright (C) 2014, 2008 Mitch Souders, Mark A. Smith, Mark P. Jones
#
# MiniJava Compiler is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation; either version 2
# of the License, or (at your option) any later version.
#
# MiniJava Compiler is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with MiniJava Compiler; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

import os
import glob
import multiprocessing
import re
import subprocess
import sys
import shutil
import argparse
import filecmp

BUILDDIR = "build/test/"
RUNTIMEFILE = "build/runtime/runtime_llvm.o"
TESTDIR = "unitTests/"

class CompareFile:
    def __init__(self, out, ref):
        self.__outFiles = [BUILDDIR + o for o in out]
        self.__refFile = TESTDIR + ref

    def compare(self):
        if self.fileCheck():
            result = True
            for o in self.__outFiles:
                if os.path.isfile(o):
                    result = result and filecmp.cmp(self.__refFile, o, False)
                else:
                    result = False
            return result
        else:
            return False
    
    def showDiff(self):
        if self.fileCheck():
            for o in self.__outFiles:
                cmd = ["diff", self.__refFile, o]
                try:
                    result = subprocess.check_output(cmd, shell=False, stderr=subprocess.STDOUT)
                except subprocess.CalledProcessError as c:
                    print(" ".join(cmd))
                    print(c.output.decode("utf-8"))
        else:
            print("Missing ref file: %s" % (self.__refFile))

    def fileCheck(self):
        if os.path.isfile(self.__refFile):
            return True
        else:
            #print("Missing ref files: %s" % (self.__refFile))
            return False    

    def copyOut(self):
        all_match = True
        prev_file = self.__outFiles[0]
        result = True
        for o in self.__outFiles:
            all_match = all_match and subprocess.call(
                ["diff", prev_file, o]) == 0
            prev_file = o

        if all_match:
            for o in self.__outFiles:
                if os.path.isfile(o):
                    shutil.copyfile(o, self.__refFile)
                    return
        else:
            print("Cannot determine canonical version of %s\n" % self.__refFile)

class RunTest:
    def __init__(self, output, cmd):
        self.__cmd = cmd;
        if output[0] == '/':
            self.__output = output
        else:
            self.__output = BUILDDIR + output

    def run(self, verbose):
        tmp_file = open(self.__output, 'w');
        if verbose:
            print("Running Test: %s > %s" % (" ".join(self.__cmd), self.__output))
        
        try:
            test_result = subprocess.call(self.__cmd,
                                          stdout=tmp_file,
                                          stderr=tmp_file)
        except subprocess.CalledProcessError as c:
            print(" ".join(self.__cmd))
            print(c.output.decode("utf-8"))
            test_result = -1
        except OSError as e:
            print(" ".join(self.__cmd))
            print(e.strerror)
            test_result = -1
        
        tmp_file.close()
        if verbose:
            print("Process Pass? Code:%d Pass:%s" % (test_result, test_result == 0))
        return test_result == 0
       

class Test:    
    def __init__(self, testfile, ignore_ref, verbose):
        self.verbose = verbose;
        self.testfile = testfile
        self.testname = "".join(os.path.splitext(self.testfile)[:-1])
        self.testname = os.path.basename(self.testname)
        self.ignore_ref = ignore_ref
        self.compare_files = []
        self.passed = True
        badfile = re.compile("^bad")
        self.bad = not badfile.match(self.testname) == None

    def missingRefs(self):
        missing_refs = 0;
        for c in self.compare_files:
            if not self.ignore_ref and not c.fileCheck():
                missing_refs += 1;
        return missing_refs

    def compareRefs(self):
        compare_result = True
        for c in self.compare_files:
            compare_result = compare_result and c.compare()

        return compare_result

    def runTest(self):
        testdir = os.path.dirname(self.testfile)

        testname = self.testname
        llvm_object = testname + ".o"
        bitcode_file = testname + ".bc"        
        x86_asm_file = testname + ".s"
        x86_compiled = testname
        llvm_exec = testname + ".llvm"

        gcc_file = testname + ".gcc.out"
        mjc_file = testname + ".mjc.out"
        llvm_link_file = testname + ".llvmlink.out"

        ignored = "/dev/null"

        llvm_file = testname + ".llvm.out"
        x86_file = testname + ".x86.out"
        interp_file = testname + ".interp.out"
        
        #no comparisons, just clean them up
        intermediate_files = [
            llvm_object,
            bitcode_file,
            x86_asm_file,
            x86_compiled,
            llvm_exec,            
        ]
        #these will all be compared to each other
        run_output = [
            llvm_file,
            x86_file,
            interp_file
        ]
        #these are expected to have corresponding refs
        out_files = [gcc_file, mjc_file]

        for o in out_files + run_output + intermediate_files:
            if os.path.exists(o):
                os.remove(o)

        compile_test = RunTest(mjc_file, ["java", "-XX:-PrintWarnings",
                                          "-Xms512m", "-Xmx1024m",
                                          "-jar", "build/jar/mjc.jar",
                                           "-i", self.testfile,
                                           "--LLVM", BUILDDIR + bitcode_file,
                                           "--x86", BUILDDIR + x86_asm_file])

        if self.bad:
            self.compare_files = [CompareFile([mjc_file], testname + ".mjc.ref")]
             # we don't actually care if this passes, must match ref
            self.passed = (compile_test.run(self.verbose) != 0 
                           and self.missingRefs() == 0
                           and self.compareRefs())

            return
        
        self.compare_files = [CompareFile(run_output, testname + ".run.ref")]    
        self.compare_files += [
            CompareFile([out], "".join(os.path.splitext(out)[:-1]) + ".ref")
            for out in out_files]

        all_tests = [compile_test,
                     RunTest(llvm_link_file, ["llc", 
                                              BUILDDIR + bitcode_file, 
                                             "-filetype=obj",
                                              "-O0",
                                             "-o",
                                             BUILDDIR + llvm_object]),
                     RunTest(ignored, ["gcc",
                                       "-lc", 
                                       BUILDDIR + llvm_object,
                                       RUNTIMEFILE,
                                       "-o", BUILDDIR + llvm_exec]),
                     RunTest(llvm_file, ["./" + BUILDDIR + llvm_exec]),
                     RunTest(interp_file, ["java",
                                           "-jar", "build/jar/mjc.jar",
                                           "-I",
                                           "-i", self.testfile]),
                     RunTest(gcc_file, ["gcc", "-ggdb3", "-m32", "src/runtime_x86.c",
                                        BUILDDIR + x86_asm_file,
                                        "-o", BUILDDIR + x86_compiled]),
                     RunTest(x86_file, ["./" + BUILDDIR + x86_compiled])
        ]

        for test in all_tests:
            test.run(self.verbose)

        self.passed = (self.passed
                       and self.missingRefs() == 0
                       and self.compareRefs())
        return 
        
    def createRefs(self):
        for c in self.compare_files:
            c.copyOut()
        
def doTest(test):
    test.runTest()
    return test

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="Runs test suite");
    parser.add_argument("--ignore_ref", help="Ignores Ref Results", action='store_true');
    parser.add_argument("--create_ref", help="Creates References (if valid)", action='store_true');
    parser.add_argument("--show_diff", help="Shows the Diffs for failing tests", action='store_true');
    parser.add_argument("--verbose", help="verbose", action='store_true');
    parser.add_argument("--single", help="sequentially run tests (overrides jobs)", action='store_true');
    parser.add_argument("--jobs", help="specify number of jobs", type=int, default=8);

    parser.add_argument("--test", help="run only this test")

    args = parser.parse_args()

    if args.single:
        jobs = 1
    else:
        jobs = args.jobs;

    if not os.path.exists(os.path.dirname(BUILDDIR)):
        os.mkdir(os.path.dirname(BUILDDIR))


    pool = multiprocessing.Pool(processes=jobs);
    if args.test:
        testFiles = [TESTDIR +  args.test + ".j"]
    else:
        testFiles = glob.glob(TESTDIR + "/*.j");

    tests = [Test(t, args.ignore_ref, args.verbose) for t in testFiles]
    testResults = pool.map(doTest, tests)
    if args.create_ref:
        for t in testResults:
            t.createRefs()
        print("Refs created. Rerun to verify.\n")
        sys.exit(1)

    failed = list(filter(lambda p : p.passed == False, testResults))
    if (len(failed) > 0):
        print("Failing Tests (%s):" % len(failed))
        for t in failed:
            print("========== %s" % (t.testfile))
            if args.show_diff:
                for c in t.compare_files:
                    c.showDiff()

        sys.exit(1)
    else:
        print("Tests Passed")
        sys.exit(0)
