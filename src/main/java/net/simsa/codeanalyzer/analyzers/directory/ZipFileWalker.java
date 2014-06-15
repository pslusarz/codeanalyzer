package net.simsa.codeanalyzer.analyzers.directory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.java.truevfs.access.TFile;
import net.simsa.codeanalyzer.analyzers.Analyzer;
import net.simsa.codeanalyzer.analyzers.AnalyzerFactory;
import net.simsa.codeanalyzer.model.DebugStats;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZipFileWalker implements Analyzer {
    static Logger log = LogManager.getLogger();

    AnalyzerFactory analyzers;
    List<Object> entities;

    File file;

    public ZipFileWalker() {
	analyzers = new AnalyzerFactory();
	entities = new ArrayList<Object>();
    }

    public List<Object> getEntities() {
	return entities;
    }

    public void setSource(TFile file) throws IOException {
	this.file = file;
    }

    public String getIdentity() {
	return "ZipFileWalker";
    }

    public void process() throws IOException {
	log.info("Process zip file using TFile");
	TFile zipfile = new TFile(file);
	TFile[] files = zipfile.listFiles();

	for (TFile file : files) {
	    log.info("ZipFile entry isDirectory = " + file.isDirectory() + ", name=" + file.getAbsolutePath());
	    if (DebugStats.shouldEarlyExit()) { return; }

	    Analyzer analyzer = analyzers.get(analyzers.getFileExtension(file.getName()), file.isDirectory());
	    analyzer.setSource(file);
	    analyzer.process();
	    entities.addAll(analyzer.getEntities());
	}

    }

}