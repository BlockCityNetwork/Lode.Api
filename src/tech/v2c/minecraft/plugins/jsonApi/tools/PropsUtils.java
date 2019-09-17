package tech.v2c.minecraft.plugins.jsonApi.tools;

import java.io.*;
import java.util.Properties;

public class PropsUtils {
    public static void Write(String pKey, String pValue) {
        Properties pps = new Properties();

        InputStream in = null;
        try {
            in = new FileInputStream("./server.properties");
            pps.load(in);
            OutputStream out = new FileOutputStream("./server.properties");
            pps.setProperty(pKey, pValue);
            pps.store(out, "Update " + pKey + " name");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
