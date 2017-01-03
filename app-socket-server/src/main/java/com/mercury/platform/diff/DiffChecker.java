package com.mercury.platform.diff;

import com.mercury.platform.diff.entry.JarEntryComparator;
import com.mercury.platform.diff.entry.JarEntryWrapper;

import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * Created by Frost on 02.01.2017.
 */
public class DiffChecker {
    public List<String> calculateDifference(JarFile first, JarFile second) {

        List<JarEntry> firstEntries = wrapEntries(Collections.list(first.entries()));
        List<JarEntry> secondEntries = wrapEntries(Collections.list(second.entries()));

        firstEntries.sort(new JarEntryComparator());
        secondEntries.sort(new JarEntryComparator());

        List<String> difference;
        if (firstEntries.size() > secondEntries.size()) {
            firstEntries.removeAll(secondEntries);
            difference = firstEntries.stream().map(ZipEntry::toString).collect(Collectors.toList());
        }
        else
        {
            secondEntries.removeAll(firstEntries);
            difference = secondEntries.stream().map(ZipEntry::toString).collect(Collectors.toList());
        }

        return difference;
    }

    private List<JarEntry> wrapEntries(List<JarEntry> entries) {
        return entries.stream().map(JarEntryWrapper::new).collect(Collectors.toList());
    }
}
