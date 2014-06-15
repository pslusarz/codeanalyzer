package net.simsa.codeanalyzer;

import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.java.truevfs.access.TFile;
import net.simsa.codeanalyzer.analyzers.Analyzer;
import net.simsa.codeanalyzer.analyzers.AnalyzerFactory;
import net.simsa.codeanalyzer.analyzers.directory.DirectoryWalker;
import net.simsa.codeanalyzer.model.DebugStats;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.jboss.weld.environment.se.events.ContainerInitialized;

/**
 * Destination entry point after CDI initializes, and the application's main
 * logic should run.
 * 
 * Injection and hibernate are working, including JClass entity persistence.
 * Next, need to find a way to stream my object updates to hibernate instead
 * of hanging on until the entire tree recursion is finished.
 * 
 * This might be a job for an event producer and an event consumer.
 * 
 * @author jenny
 * 
 */
public class ApplicationMain {
    Logger log = LogManager.getLogger();

    String datadirPath;
    TFile datadir;
    
    @Inject
    AnalyzerFactory analyzerFactory;

    public ApplicationMain() {
    }

    public void onStartup(@Observes ContainerInitialized event, @Parameters List<String> parameters) {
	this.datadirPath = Main.getParameters()[0];
	this.datadir = new TFile(datadirPath);

	try {
	    run();
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    public void run() throws Exception {
	log.warn("Beginning");
	if (!datadir.isDirectory()) {
	    throw new IllegalArgumentException("Data dir must be a valid directory path.");
	}
	if (datadir.listFiles().length < 1) {
	    throw new IllegalArgumentException("There are no files in the directory to process.");
	}

	Analyzer dirWalker = analyzerFactory.get(null, true);
	dirWalker.setSource(datadir);
	try {
	    dirWalker.process();
	    analyzerFactory.commit();
	} finally {
	    DebugStats.display();
	}

    }


}
