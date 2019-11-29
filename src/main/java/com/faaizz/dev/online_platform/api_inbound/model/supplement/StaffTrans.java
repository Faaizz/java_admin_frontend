package com.faaizz.dev.online_platform.api_inbound.model.supplement;

public class StaffTrans {

    /**
     * An intermediate implementation of Staff to enable proper parsing of JSON API response
     */

    private String first_name;
    private String last_name;
    private String email;
    private String address;
    private String gender;
    private String phone_numbers;
    private String privilege_level;
    private int pending_orders;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone_numbers() {
        return phone_numbers;
    }

    public String getPrivilege_level() {
        return privilege_level;
    }

    public int getPending_orders() {
        return pending_orders;
    }
    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public StaffTrans(
            String first_name, String last_name, String email, String address, String gender,
            String phone_numbers, String privilege_level, int pending_orders
    ) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.phone_numbers = phone_numbers;
        this.privilege_level = privilege_level;
        this.pending_orders= pending_orders;
    }
}
