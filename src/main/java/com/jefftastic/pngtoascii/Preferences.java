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
    public enum PreferenceCharSet    { Generic, Block, Kanji }
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
     * Holds all PreferencesListeners in order to inform
     * them of a preference update
     */
    public static List<PreferencesListener> listeners = new ArrayList<>();

    /**
     * Initializes preference map
     */
    public static void initialize() {
        try {
            assignDefaults();
            if (!loadPreferences()) {
                savePreferences();
            }
        } catch (IOException e) {
            /*
             Since we check if the preference file exists,
             it can be assumed the error likely involves the
             new default file not being saved properly.
             */
            e.printStackTrace(System.out);
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

        // Inform listeners
        for (PreferencesListener listener : listeners)
            listener.preferencesChanged();
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

        // Otherwise, lets load from file and check size
        prop.load(new FileReader(path.getAbsolutePath()));
        if (prop.isEmpty())
            return false;

        // Iterate and put in map
        for (String property : prop.stringPropertyNames()) {
            /*
             * If the property doesn't already exist in
             * the preference map, it should be discarded
             */
            if (!preference.containsKey(property))
                continue;
            preference.put(property, prop.getProperty(property));
        }

        // Inform listeners
        for (PreferencesListener listener : listeners)
            listener.preferencesChanged();
        return true;
    }

    /**
     * Adds a preference listener.
     * @param pL The listener to add
     */
    public static void addListener(PreferencesListener pL) {
        listeners.add(pL);
    }

    /**
     * Initializes preference HashMap with
     * default values.
     */
    private static void assignDefaults() {
        preference.put("charSet", PreferenceCharSet.Generic.toString());
        preference.put("imageLimit", PreferenceImageLimit.sNone.toString());
        preference.put("font", "Courier");
        preference.put("grayscale", "true");
    }
}
