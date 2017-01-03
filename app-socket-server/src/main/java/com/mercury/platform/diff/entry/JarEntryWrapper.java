package com.mercury.platform.diff.entry;

import org.apache.commons.lang3.ObjectUtils;

import java.util.jar.JarEntry;

/**
 * Created by Frost on 03.01.2017.
 */
public class JarEntryWrapper extends JarEntry{

    public JarEntryWrapper(JarEntry jarEntry) {
        super(jarEntry);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JarEntry) {

            JarEntry entry = (JarEntry) obj;

            return ObjectUtils.equals(this.getName() , entry.getName()) &&
                    ObjectUtils.equals(this.getSize() , entry.getSize()) &&
                        ObjectUtils.equals(this.getTime() , entry.getTime());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCodeMulti(this.getName(), this.getSize(), this.getTime());
    }
}
