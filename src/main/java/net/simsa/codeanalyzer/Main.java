package net.simsa.codeanalyzer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.weld.environment.se.StartMain;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * Wrapper entry point to enable CDI initialization 
 */
public class Main extends StartMain {

    public Main(String[] commandLineArgs) {
	super(commandLineArgs);
    }

    /** Transfers control to ApplicationMain */
    public void init(String[] args) {
	super.main(args);
    }

    public static void main(String[] args) {
	if (args.length < 1) {
	    System.out.println("Usage: net.simsa.codeanalyzer.Main path/to/directory/with/war");
	} else {
	    Main main = new Main(args);
	    main.init(args);
	    LogManager.getLogger().warn("Complete.");
	}
    }
}
