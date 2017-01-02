package com.mercury.platform.diff.utils;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by Frost on 02.01.2017.
 */
public class JarFileMocker {
    public static JarFile mockJar(List<JarEntry> entries) {
        JarFile jarFile = mock(JarFile.class);
        Enumeration<JarEntry> entryEnumeration = Collections.enumeration(entries);
        when(jarFile.entries()).thenReturn(entryEnumeration);
        return jarFile;
    }
}
