package com.mercury.platform;

import java.io.File;

/**
 * Created by Константин on 08.01.2017.
 */
public class UpdaterMain {
    public static void main(String[] args) {
        File jar = new File("D:\\Test.txt");
        System.out.println(jar.delete());
    }
}
