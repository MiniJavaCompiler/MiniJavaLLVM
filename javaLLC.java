// Mark Anderson Smith, Portland State University
// Apr 27, 2014
// 
// Provides a wrapper service around mjc compiler
// Accept input arguments and calls mjc compiler with appropriate
// arguments

import java.util.ArrayList;
import java.io.Reader;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/** A top-top-level driver for the mini Java compiler.
 */
public class javaLLC {

    public static String UsageInfo = "usage: \n"
	                                + "\tjavaLL -interp inputFile\n"
	                                + "\tjavaLL -compilex86 inputFile outputFile\n"
                                        + "\tjavaLL -compileLLVM inputFile outputFile\n"
	                                + "\tjavaLL -compileLLx86 inputFile outputFile";

    /** A command line entry point to the mini Java compiler.
     */
    public static void main(String[] args) {
        if (args.length != 2 && args.length != 3) {
	    //System.err.println("usage: java -jar mjc.jar inputFile outputFile");
	    System.err.println("usage: ");
	    System.err.println("  javaLL -interp inputFile");
            System.err.println("  javaLL -compilex86 inputFile outputFile");
            System.err.println("  javaLL -compileLLVM inputFile outputFile");
	    System.err.println("  javaLL -compileLLx86 inputFile outputFile");
            return;
        }
       
        String option = args[0];
        String inputFile  = args[1];
        String outputFile = "";

        if (args.length == 3) {
	    outputFile = args[2];
	}
	
	ArrayList<String> commands = new ArrayList<String>();
        if (option.equals("-interp")) {
            // just run the interpreter
	    commands.add("java -classpath mjc interp.Interp " + inputFile);
	}
	else if (option.equals("-compileX86")) {
	    if (outputFile.isEmpty()) {
		System.out.println("Missing outputfile");
	    }

	    String assmName = inputFile + ".s";

	    // compile source to assembly
	    commands.add("java -classpath mjc Compiler " + inputFile + " " + assmName);
	    // link assembly with the runtime
	    commands.add("gcc -m32 -o " + outputFile + " " + assmName + " mjc/runtime.c");
	}
	else if (option.equals("-compileLLVM")) {
	    if (outputFile.isEmpty()) {
		System.out.println("Missing outputfile");
	    }	    
            System.out.println("Not implemented");
	    return;
	}
	else if (option.equals("-compileLLx86")) {
	    if (outputFile.isEmpty()) {
		System.out.println("Missing outputfile");
	    }	    
            System.out.println("Not implemented");
	    return;
	}
	else { 
	    System.out.println("Illegal option");
	    System.out.println(UsageInfo);
	    return;
	}

    
        for (String command : commands) {
	
        StringBuilder sb = new StringBuilder();

	System.out.println("Executing: " + command);

	Process rtProc;
	try {
	    rtProc = Runtime.getRuntime().exec(command);
	    rtProc.waitFor();
	}
	catch(Exception ex) {
	    System.err.println("Panic!");
	    System.err.println(ex.toString());
	    return;
	}

	BufferedReader reader = 
	    new BufferedReader(new InputStreamReader(rtProc.getInputStream()));
 
	try {
	    String line;			
	    while ((line = reader.readLine())!= null) {
		sb.append(line + "\n");
	    }
	}
	catch (Exception ex) {
	    // proceed
	}

        System.out.print(sb.toString());
	}

    }
}
