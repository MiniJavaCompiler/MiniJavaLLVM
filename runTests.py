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
    
    def showDiff(self):
        cmd = ["diff", self.__refFile, self.__outFile]
        print(" ".join(cmd))
        try:
            proc = subprocess.check_output (cmd, shell=False, stderr=subprocess.STDOUT)
            proc.communicate()
        except subprocess.CalledProcessError as c:
            print(c.output.decode("utf-8"))

    def fileCheck(self):
        if os.path.isfile(self.__refFile):
            return True
        else:
            print("Missing ref files: %s" % (self.__refFile))
            return False    

    def copyOut(self):
        shutil.copyfile(self.__outFile, self.__refFile)

class Test:    
    def __init__(self, testfile, ignore_ref, create_ref):
        self.testfile = testfile
        self.create_ref = create_ref
        self.ignore_ref = ignore_ref
        self.compare_files = []
        self.passed = True

    def runTest(self):
        testname = "".join(os.path.splitext(self.testfile)[:-1]);
        bitcode_file = testname + ".bc"
        mjc_file = testname + ".mjc.out"
        llvm_file = testname + ".llvm.out"
        out_files = [mjc_file, llvm_file]

        for o in out_files:
            if os.path.exists(o):
                os.remove(o)

        self.compare_files = [CompareFile(out, "".join(os.path.splitext(out)[:-1]) + ".ref") for out in out_files]

        missing_refs = 0;
        for c in self.compare_files:
            if not self.ignore_ref and not c.fileCheck():
                missing_refs += 1;

        mjc_output = open(mjc_file, 'w');
        #print("Running MJC on %s" % self.testfile)
        mjc_succeed = subprocess.call(
            ["java", "-jar", "build/jar/mjc.jar", "-i", self.testfile, "--LLVM", bitcode_file],
            stdout=mjc_output,
            stderr=mjc_output);

        if mjc_succeed != 0:
            self.passed = False
            return

        #print("Running LLVM on %s" % bitcode_file)

        llvm_output = open(llvm_file, 'w');
        subprocess.call(["lli", bitcode_file],
                        stdout=llvm_output,
                        stderr=llvm_output)

        if missing_refs > 0:
            self.passed = False
            return

        compare_result = True
        for c in self.compare_files:
            if self.create_ref:
                c.copyOut()
            compare_result = compare_result and c.compare()

        self.passed = compare_result
        return

def doTest(test):
    test.runTest()
    return test

if __name__ == '__main__':
    ignore_ref = False
    create_ref = False
    show_diff = False
    for a in sys.argv[1:]:
        if (a == "ignore_ref"):
            ignore_ref = True
        elif (a == "create_ref"):
            create_ref = True
        elif (a == "show_diff"):
            show_diff = True
        else:
            print("Unknown Argument: " + a)
            sys.exit(1)

    pool = multiprocessing.Pool(processes=8);
    testFiles = glob.glob("unitTests/*.j");
    args = [Test(t, ignore_ref, create_ref) for t in testFiles]
    testResults = pool.map(doTest, args)
    failed = list(filter(lambda p : p.passed == False, testResults))
    if (len(failed) > 0):
        print("Failing Tests:")
        for t in failed:
            print(t.testfile)
            if show_diff:
                for c in t.compare_files:
                    c.showDiff()

        sys.exit(1)
    else:
        print("Tests Passed")
        sys.exit(0)
