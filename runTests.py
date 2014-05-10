#!/usr/bin/env python3
import os;
import glob;
import multiprocessing;
import re;
import subprocess;
import sys
import shutil

class CompareFile:
    def __init__(self, out, ref):
        self.__outFile = out
        self.__refFile = ref

    def compare(self):
        if self.fileCheck():
            return subprocess.call(
                ["diff", self.__refFile, self.__outFile]) == 0
        else:
            return False
    
    def fileCheck(self):
        if os.path.isfile(self.__refFile):
            return True
        else:
            print("Missing ref files: %s" % (self.__refFile))
            return False    

    def copyOut(self):
        shutil.copyfile(self.__outFile, self.__refFile)

def runTest(testfile, ignore_ref, create_ref):
    testname = "".join(os.path.splitext(testfile)[:-1]);
    bitcode_file = testname + ".bc"
    mjc_file = testname + ".mjc.out"
    llvm_file = testname + ".llvm.out"
    out_files = [mjc_file, llvm_file]
    
    compare_files = [CompareFile(out, "".join(os.path.splitext(out)[:-1]) + ".ref") for out in out_files]

    error = 0;
    for c in compare_files:
        if not ignore_ref and not c.fileCheck():
            error += 1;

    if error > 0:
        return False;

    mjc_output = open(mjc_file, 'w');
    print("Running MJC on %s" % testfile)
    mjc_succeed = subprocess.call(
        ["java", "-jar", "build/jar/mjc.jar", "-i", testfile, "--LLVM", bitcode_file],
        stdout=mjc_output,
        stderr=mjc_output);

    if mjc_succeed != 0:
        return False;

    print("Running LLVM on %s" % bitcode_file)

    llvm_output = open(llvm_file, 'w');
    subprocess.call(["lli", bitcode_file],
                    stdout=llvm_output,
                    stderr=llvm_output)
    
    compare_result = True
    for c in compare_files:
        if create_ref:
            c.copyOut()
        compare_result = compare_result and c.compare()

    return compare_result

def doTest(filename, ignore_ref, create_ref):
    return (filename, runTest(filename, ignore_ref, create_ref))

if __name__ == '__main__':
    ignore_ref = False
    create_ref = False
    for a in sys.argv[1:]:
        if (a == "ignore_ref"):
            ignore_ref = True
        elif (a == "create_ref"):
            create_ref = True
        else:
            print("Unknown Argument: " + a)
            sys.exit(1)

    pool = multiprocessing.Pool(processes=8);
    testFiles = glob.glob("unitTests/*.j");
    mapped_args = [[t, ignore_ref, create_ref] for t in testFiles]
    testResults = pool.starmap(doTest, mapped_args)
    failed = list(filter(lambda p : p[1] == False, testResults))
    if (len(failed) > 0):
        print("Failing Tests:")
        for t in testResults:
            print(t[0])
        sys.exit(1)
    else:
        print("Tests Passed")
        sys.exit(0)
