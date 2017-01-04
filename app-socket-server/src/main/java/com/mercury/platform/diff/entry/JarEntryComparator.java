package com.mercury.platform.diff.entry;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Comparator;
import java.util.jar.JarEntry;

/**
 * Created by Frost on 02.01.2017.
 */
public class JarEntryComparator implements Comparator<JarEntry> {
    public int compare(JarEntry first, JarEntry second) {
        return ObjectUtils.compare(first.getName() , second.getName());
    }
}
