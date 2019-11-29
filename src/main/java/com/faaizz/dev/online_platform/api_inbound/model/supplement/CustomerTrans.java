package com.faaizz.dev.online_platform.api_inbound.model.supplement;

public class CustomerTrans {

    /**
     * An intermediate implementation of Staff to enable proper parsing of JSON API response
     */

    private String first_name;
    private String last_name;
    private String email;
    private String address;
    private String gender;
    private String phone_numbers;
    private String activation_status;
    private String newsletters;
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

    public int getPending_orders() {
        return pending_orders;
    }

    public String getActivation_status() {
        return activation_status;
    }

    public String getNewsletters() {
        return newsletters;
    }
    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public CustomerTrans(
            String first_name, String last_name, String email, String address, String gender,
            String phone_numbers, int pending_orders, String activation_status, String newsletters
    ) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.phone_numbers = phone_numbers;
        this.activation_status= activation_status;
        this.newsletters= newsletters;
        this.pending_orders = pending_orders;
    }
}
