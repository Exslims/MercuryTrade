package com.home.clicker.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Exslims
 * 07.12.2016
 */
public class LogedMessagesUtils {
    public class FileFinder{
        public File getLoggedMessagesFile(){
            return null;
        }
    }

    public void execute(){
        FileFinder fileFinder = new FileFinder();
        fileFinder.getLoggedMessagesFile();
    }
}
