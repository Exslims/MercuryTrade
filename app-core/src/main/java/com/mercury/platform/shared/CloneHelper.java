package com.mercury.platform.shared;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class CloneHelper {
    private static Logger log = LogManager.getLogger("CloneHelper");

    public static <T> T cloneObject(T source) {
        T cloned = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream ous = new ObjectOutputStream(baos);

            ous.writeObject(source);
            ous.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);

            cloned = (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error while cloning object: ", e);
        }
        return cloned;
    }
}
