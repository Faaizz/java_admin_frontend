package com.faaizz.dev.online_platform.GUI;

import com.faaizz.dev.online_platform.api_inbound.model.Staff;

/**
 * THIS CLASS STORES DETAILS OF THE RUNNING INSTANCE OF THE PROGRAM
 */
public class InstanceData {

    private static boolean isAuthenticated= false;
    private static Staff current_staff;


    /*========================================================================================*/
    /* G    E   T   T   E   R   S */

    public static boolean getAuthenticated(){
        return isAuthenticated;
    }

    public static Staff getCurrentStaff(){
        return current_staff;
    }

    /*========================================================================================*/
    /* S    E   T   T   E   R   S */
    public static void setAuthenticated(boolean isAuthenticated){
        InstanceData.isAuthenticated = isAuthenticated;
    }

    public static void setCurrentStaff(Staff current_staff){
        InstanceData.current_staff= current_staff;
    }

}

