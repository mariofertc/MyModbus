package com.ambatosystem.mymodbus;

/**
 * Created by mario-ivonne on 11/20/2017.
 */

public class MMConstants {

    /**
     * You'll need to modify these values to suit your own app.
     */
    // TODO Turn off when deploying your app.
    public static boolean DEVELOPER_MODE = true;

    /**
     * These values are constants used for intents, extras, and shared preferences.
     * You shouldn't need to modify them.
     */
    public static String SHARED_PREFERENCE_FILE = "SHARED_PREFERENCE_FILE";
    public static String SP_KEY_RUN_ONCE = "SP_KEY_RUN_ONCE";

    public static boolean SUPPORTS_JELLYBEAN = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN;
    public static boolean SUPPORTS_ICECRAMSANDWICH = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    public static boolean SUPPORTS_GINGERBREAD = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD;
    public static boolean SUPPORTS_HONEYCOMB = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
    public static boolean SUPPORTS_FROYO = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO;
    public static boolean SUPPORTS_ECLAIR = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ECLAIR;

}
