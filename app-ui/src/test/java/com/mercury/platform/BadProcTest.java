package com.mercury.platform;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.*;
import com.sun.jna.win32.StdCallLibrary;

import java.util.Arrays;
import java.util.List;

public class BadProcTest {
    public static void main(String[] args) {
        Kernel32 lib = Kernel32.INSTANCE;
        SYSTEMTIME time = new SYSTEMTIME();
        lib.GetSystemTime(time);
        System.out.println("Today's integer value is " + time.wDay);
    }

    public interface Kernel32 extends StdCallLibrary {
        Kernel32 INSTANCE = (Kernel32)Native.loadLibrary("kernel32", Kernel32.class);
        void GetSystemTime(SYSTEMTIME result);
    }
    public static class SYSTEMTIME extends Structure {
        public short wYear;
        public short wMonth;
        public short wDayOfWeek;
        public short wDay;
        public short wHour;
        public short wMinute;
        public short wSecond;
        public short wMilliseconds;
        protected List getFieldOrder() {
            return Arrays.asList("wYear", "wMonth", "wDayOfWeek", "wDay", "wHour", "wMinute", "wSecond", "wMilliseconds");
        }
    }

}
