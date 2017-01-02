package com.mercury.platform.diff.utils;

import java.util.jar.JarEntry;

/**
 * Created by Frost on 02.01.2017.
 */
public class JarEntryBuilder {

    private JarEntry jarEntry;

    public JarEntryBuilder(String entryName) {
        this.jarEntry = new JarEntry(entryName);
    }

    public JarEntryBuilder setTime(long time) {
        this.jarEntry.setTime(time);
        return this;
    }


    public JarEntryBuilder setSize(long size) {
        this.jarEntry.setSize(size);
        return  this;
    }


    public JarEntry build() {
        return jarEntry;
    }
}