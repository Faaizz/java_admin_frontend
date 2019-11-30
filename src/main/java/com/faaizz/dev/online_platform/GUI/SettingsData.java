package com.faaizz.dev.online_platform.GUI;

/**
 * This class holds current application settings
 */
public class SettingsData {

    private static String api_token;
    private static String base_URL;
    private static String api_path;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public static String getApi_token() {
        return api_token;
    }

    public static String getBase_URL() {
        return base_URL;
    }

    public static String getApi_path() {
        return api_path;
    }
    /*========================================================================================*/
    /*  S   E   T   T   E   R   S   */

    public static void setApi_token(String api_token) {
        SettingsData.api_token = api_token;
    }

    public static void setBase_URL(String base_URL) {
        SettingsData.base_URL = base_URL;
    }

    public static void setApi_path(String api_path) {
        SettingsData.api_path = api_path;
    }
}
