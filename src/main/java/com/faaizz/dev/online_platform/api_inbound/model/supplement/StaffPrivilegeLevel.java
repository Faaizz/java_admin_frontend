package com.faaizz.dev.online_platform.api_inbound.model.supplement;

public enum StaffPrivilegeLevel {

    STAFF("staff"),
    ADMIN("admin");

    private String privilege_level;

    StaffPrivilegeLevel(String privilege_level){
        this.privilege_level= privilege_level;
    }

    public String getPrivilege_level(){
        return this.privilege_level;
    }

}
