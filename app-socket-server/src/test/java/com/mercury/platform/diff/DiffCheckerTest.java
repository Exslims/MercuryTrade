package com.mercury.platform.diff;

import com.mercury.platform.diff.utils.JarEntryBuilder;
import com.mercury.platform.diff.utils.JarFileMocker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


import static org.junit.Assert.*;

/**
 * Created by Frost on 02.01.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DiffCheckerTest {

    public static final Logger LOGGER = LogManager.getLogger(DiffCheckerTest.class);

    private DiffChecker diffChecker;

    @Before
    public void setUp() {
        this.diffChecker = new DiffChecker();
    }

    @Test
    public void test_diff_by_size() throws IOException {
        JarEntry first = new JarEntryBuilder("mercury/framework/First.class").setSize(70).build();
        JarEntry second = new JarEntryBuilder("mercury/framework/Second.class").setSize(50).build();
        String updatedEntryName = "mercury/framework/Third.class";
        JarEntry third = new JarEntryBuilder(updatedEntryName).setSize(50).build();
        JarEntry updatedEntry = new JarEntryBuilder(updatedEntryName).setSize(70).build();

        List<JarEntry> sourceEntries = Arrays.asList(first, second, third);
        JarFile sourceJar = JarFileMocker.mockJar(sourceEntries);

        List<JarEntry> updatedEntries = Arrays.asList(first, second, updatedEntry);
        JarFile updatedJar = JarFileMocker.mockJar(updatedEntries);

        List<String> difference = diffChecker.calculateDifference(sourceJar, updatedJar);
        LOGGER.info("Difference = {}" , difference.toString());

        String format = String.format("difference must contain \"%s\"" , updatedEntryName);
        assertTrue(format, difference.size() == 1 && difference.contains(updatedEntryName));
    }

    @Test
    public void test_diff_by_time() {
        JarEntry first = new JarEntryBuilder("mercury/framework/First.class").setSize(70).build();
        JarEntry second = new JarEntryBuilder("mercury/framework/Second.class").setSize(50).build();
        String updatedEntryName = "mercury/framework/Third.class";
        JarEntry third = new JarEntryBuilder(updatedEntryName).setSize(50).setTime(70).build();
        JarEntry updatedEntry = new JarEntryBuilder(updatedEntryName).setSize(50).setTime(100).build();

        List<JarEntry> sourceEntries = Arrays.asList(first, second, third);
        JarFile sourceJar = JarFileMocker.mockJar(sourceEntries);


        List<JarEntry> updatedEntries = Arrays.asList(first, second, updatedEntry);
        JarFile updatedJar = JarFileMocker.mockJar(updatedEntries);

        List<String> difference = diffChecker.calculateDifference(sourceJar, updatedJar);
        LOGGER.info("Difference = {}" , difference.toString());

        String format = String.format("difference must contain \"%s\"" , updatedEntryName);
        assertTrue(format, difference.size() == 1 && difference.contains(updatedEntryName));
    }

    @Test
    public void test_diff_by_file_hash() {

    }

    @After
    public void tearDown() {


    }
}