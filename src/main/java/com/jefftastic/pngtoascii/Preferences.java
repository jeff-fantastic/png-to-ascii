package com.jefftastic.pngtoascii;

import java.io.*;
import java.util.*;

/**
 * Handles loading, reading, and saving of
 * user preference data at runtime.
 * @see PreferencesController
 */

public class Preferences {
    // Declare enums / finals
    public enum PreferenceCharSet   { Generic, Block, Kanji }
    public enum PreferenceImageLimit { s512, s1024, sNone }
    private static final String PROPERTIES_PATH = "." + File.separator + "user-config.p";

    /**
     * Holds all data related to user preference.
     * Stored as strings, since the Properties
     * class can only write and parse string data.
     * Data conversion must be handled by other parts
     * of the program that wish to use this data.
     * @see Properties
     */
    public static Map<String, String> preference = new HashMap<>();

    /**
     * Initializes preference map
     */
    public Preferences() {
        try {
            if (!loadPreferences())
                savePreferences();
        } catch (IOException e) {
            /*
             Since we check if the preference file exists,
             it can be assumed the error likely involves the
             new default file not being saved properly. In the
             case of this event, just load default values.
             */
            e.printStackTrace(System.out);
            assignDefaults();
        }
    }

    /**
     * Saves user preferences to a properties file
     */
    public static void savePreferences() throws IOException {
        // Create properties
        Properties prop = new Properties();

        // Assign all values
        for (String key : preference.keySet())
            prop.setProperty(key, preference.get(key));

        // Save to file
        prop.store(new FileWriter(PROPERTIES_PATH), "User Preferences Data");
    }

    /**
     * Loads user preferences from a properties file,
     * but only if one exists. Returns false if the
     * preference file could not be located.
     */
    public static boolean loadPreferences() throws IOException {
        // Determine if properties file exists
        Properties prop = new Properties();
        File path = new File(PROPERTIES_PATH);
        if (!path.exists())
            return false;

        // Otherwise, lets load from file
        preference.clear();
        prop.load(new FileReader(path.getAbsolutePath()));
        for (String property : prop.stringPropertyNames()) {
            /*
             * If the property doesn't already exist in
             * the preference map, it should be discarded
             */
            if (!preference.containsKey(property))
                continue;
            preference.put(property, prop.getProperty(property));
        }
        return true;
    }

    /**
     * Initializes preference HashMap with
     * default values.
     */
    private static void assignDefaults() {
        preference.clear();
        preference.put("charSet", PreferenceCharSet.Generic.toString());
        preference.put("imageLimit", PreferenceImageLimit.sNone.toString());
        preference.put("font", "Courier");
        preference.put("grayscale", "true");
    }
}
