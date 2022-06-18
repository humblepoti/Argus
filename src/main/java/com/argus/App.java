package com.argus;

import soot.util.Chain;
import soot.*;
import soot.SootMethod;
import soot.options.Options;
import soot.jimple.JimpleBody;
import org.apache.commons.cli.*;
import soot.jimple.internal.JIfStmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.graph.ClassicCompleteUnitGraph;




public class App 
{
    
    static { System.out.println("Argus: Framework for static code analysis."); }

    

    public static void setupSoot( String classpath, String className) {
        G.reset();
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_soot_classpath(classpath);
        SootClass sc = Scene.v().loadClassAndSupport(className);
        Options.v().set_whole_program(true);
        Options.v().set_validate(true); // Validate Jimple bodies in each transofrmation pack
        sc.setApplicationClass();
        Scene.v().loadNecessaryClasses();
     


    }

    public static void printMethods(Chain<SootClass> classes) {
        for (SootClass classy : classes) {
            for (SootMethod Method : classy.getMethods()) {
                System.out.println("Method: "+ Method.getName());

            }
        }
    }
    public static void printMethods(SootClass classy) {
       for (SootMethod Method : classy.getMethods()) {
                System.out.println("Method: "+ Method.getName());
                printStmt(Method.getName(), classy.getName());

            }
        
    }

    public static void printStmt(String methodName, String className){
        SootClass mainClass = Scene.v().getSootClass(className);

        SootMethod sm = mainClass.getMethodByName(methodName);
        JimpleBody body = (JimpleBody) sm.retrieveActiveBody();

        // Print some information about printFizzBuzz
        System.out.println("Method Signature: " + sm.getSignature());
        System.out.println("--------------");
        System.out.println("Argument(s):");
        for (Local l : body.getParameterLocals()) {
            System.out.println(l.getName() + " : " + l.getType());
        }
        System.out.println("--------------");
        System.out.println("This: " + body.getThisLocal());
        System.out.println("--------------");
        System.out.println("Units:");
        int c = 1;
        for (Unit u : body.getUnits()) {
            System.out.println("(" + c + ") " + u.toString());
            c++;
        }
        System.out.println("--------------");

        // Print statements that have branch conditions
        System.out.println("Branch Statements:");
        for (Unit u : body.getUnits()) {
            if (u instanceof JIfStmt)
                System.out.println(u.toString());
        }
           
        UnitGraph ug = new ClassicCompleteUnitGraph(sm.getActiveBody());
        Visualizer.v().addUnitGraph(ug);
        Visualizer.v().draw();
             
        
    }


    public static void main( String[] args )
    {           


        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();

        Option classpath = new Option("cp", "class-path", true, "input class-path");
        classpath.setRequired(true);
        options.addOption(classpath);

        Option classname = new Option("cn", "class-name", true, "class name to print methods");
        classname.setRequired(true);
        options.addOption(classname);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null; // not a good practice, it serves its purpose

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Argus", options);

            System.exit(1);
        }

        
        // Soot required setup
        String classPath = cmd.getOptionValue("cp") ;
        String className = cmd.getOptionValue("cn") ;
        
        
        setupSoot(classPath, className);

        Chain<SootClass> classes = Scene.v().getClasses();
        SootClass mainClass = Scene.v().getSootClass(className);

        printMethods(mainClass);

        // SootClass mainClass = Scene.v().getSootClass(className);

        // SootMethod sm = mainClass.getMethodByName(methodName);
        // JimpleBody body = (JimpleBody) sm.retrieveActiveBody();

        // // Print some information about printFizzBuzz
        // System.out.println("Method Signature: " + sm.getSignature());
        // System.out.println("--------------");
        // System.out.println("Argument(s):");
        // for (Local l : body.getParameterLocals()) {
        //     System.out.println(l.getName() + " : " + l.getType());
        // }
        // System.out.println("--------------");
        // System.out.println("This: " + body.getThisLocal());
        // System.out.println("--------------");
        // System.out.println("Units:");
        // int c = 1;
        // for (Unit u : body.getUnits()) {
        //     System.out.println("(" + c + ") " + u.toString());
        //     c++;
        // }
        // System.out.println("--------------");

        // // Print statements that have branch conditions
        // System.out.println("Branch Statements:");
        // for (Unit u : body.getUnits()) {
        //     if (u instanceof JIfStmt)
        //         System.out.println(u.toString());
        // }

        // // Draw the control-flow graph of the method if 'draw' is provided in arguments
        // boolean drawGraph = false;
        // if (args.length > 0 && args[0].equals("draw"))
        //     drawGraph = true;
        // if (drawGraph) {
        //     UnitGraph ug = new ClassicCompleteUnitGraph(sm.getActiveBody());
        //     Visualizer.v().addUnitGraph(ug);
        //     Visualizer.v().draw();
        // }
    

    }
}
