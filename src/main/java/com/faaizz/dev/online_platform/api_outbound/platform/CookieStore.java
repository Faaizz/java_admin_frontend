package com.faaizz.dev.online_platform.api_outbound.platform;

import com.faaizz.dev.online_platform.api_outbound.model.Cookie;

import java.util.HashMap;
import java.util.Map;

public class CookieStore {

    /**
     * ALL INCOMING COOKIES FOR AN HTTP SESSION ARE ADDED TO THIS MAP SUCH THAT AUTHENTICATION CAN BE PERSISTED.
     * A MAP IMPLEMENTATION IS USED SUCH THAT COOKIES WITH THE SAME NAME ARE OVERWRITTEN (i.e. 2 USERS CANNOT
     * BE LOGGED IN AT THE SAME TIME.
     */
    private static Map<String, Cookie> cookies;

    /*========================================================================================*/
    /*  S   T   A   T   I   C      I   N   I   T   I   A   L   I   Z   E   R   */

    static{

        cookies= new HashMap<>();

    }

    public static Map<String, Cookie> getCookies(){

        return CookieStore.cookies;

    }

    public static boolean removeCookie(String cookie_name){

        //IF A COOKIE WITH THE SPECIFIED NAME WAS MATCHED AND REMOVED, RETURN TRUE
        if(cookies.remove(cookie_name) != null){
            return true;
        }

        //OTHERWISE, RETURN FALSE
        else{
            return false;
        }

    }

    public static boolean clearCookies(){

        // CLEAR COOKIES
        cookies= new HashMap<>();

        return true;

    }

}
