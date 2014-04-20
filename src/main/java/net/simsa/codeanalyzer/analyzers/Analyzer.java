package net.simsa.codeanalyzer.analyzers;

import java.io.IOException;
import java.io.InputStream;

import net.java.truevfs.access.TFile;

public interface Analyzer {

    public void setSource(TFile file) throws IOException;

    /** Which analyzer is it? Say your name */
    public String getIdentity();

    /**
     * Presumably any input streams, file to process, path to process, has been
     * handled separately ahead of time
     */
    public void process() throws IOException;

}