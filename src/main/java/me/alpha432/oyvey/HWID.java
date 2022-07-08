/*
package me.alpha432.oyvey;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import oshi.SystemInfo;

public class HWID {
    public static final Logger LOGGER = LogManager.getLogger(HWID.class);
    private static final int RETRIES = 5;
    private static final String SALT = "l7FhV9aKSqPq734TOc3Enihmd2SnaHNhr21Au48TeqyjJMVaPJg6ESrIYCSTcFB7";
    private static final String LINK = "https://pastebin.com/raw/L2vqkkqV";
    private static final SystemInfo systemInfo = new SystemInfo();

    public static boolean isWhitelisted(String eHWID) {
        return HWID.isWhitelisted(eHWID, 5);
    }

    public static boolean isWhitelisted(String eHWID, int retries) {
        if (retries <= 0) {
            return false;
        }
        boolean result = false;
        try {
            Scanner scanner = new Scanner(new URL(LINK).openStream(), "UTF-8");
            while (scanner.hasNextLine()) {
                String d = scanner.next();
                System.out.println(d);
                if (!eHWID.equals(d)) continue;
                result = true;
            }
            scanner.close();
        }
        catch (FileNotFoundException fe) {
            return HWID.isWhitelisted(eHWID, retries - 1);
        }
        catch (NoSuchElementException fe) {
        }
        catch (Exception e) {
            LOGGER.error("FEHLER: " + e.toString());
            e.printStackTrace();
        }
        LOGGER.info("RESULT: " + Boolean.toString(result));
        return result;
    }

    public static String getHWID() {
        byte[] out;
        String hwstr = System.getProperty("file.separator") + System.getProperty("os.name") + System.getProperty("os.arch") + Runtime.getRuntime().availableProcessors() + System.getProperty("user.name") + System.getProperty("user.home") + System.getenv("os") + System.getProperty("os.name") + System.getProperty("os.arch") + System.getProperty("user.name") + System.getenv("SystemRoot") + System.getenv("HOMEDRIVE") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS") + System.getProperty("file.separator");
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec keySpec = new PBEKeySpec(hwstr.toCharArray(), SALT.getBytes(), 1000, 512);
            SecretKey secret = keyFactory.generateSecret(keySpec);
            out = secret.getEncoded();
        }
        catch (Exception e) {
            return e.toString();
        }
        return HWID.bytesToHex(out);
    }

    private static String bytesToHex(byte[] data) {
        StringBuilder result = new StringBuilder();
        for (byte sData : data) {
            result.append(String.format("%02x", sData));
        }
        return result.toString();
    }
}

 */
