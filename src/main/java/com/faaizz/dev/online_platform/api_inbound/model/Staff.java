package com.faaizz.dev.online_platform.api_inbound.model;

import com.faaizz.dev.online_platform.api_inbound.model.supplement.StaffPrivilegeLevel;

import java.util.List;

public class Staff {

    /**
     * This calss is an implementation of <b>Staff</b>
     */

    private String first_name;
    private String last_name;
    private String email;
    private String address;
    private String gender;
    private List<String> phone_numbers;
    private StaffPrivilegeLevel privilege_level;
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

    public List<String> getPhone_numbers() {
        return phone_numbers;
    }

    public StaffPrivilegeLevel getPrivilege_level() {
        return privilege_level;
    }

    public int getPending_orders() {
        return pending_orders;
    }

    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public Staff(
            String first_name, String last_name, String email, String address, String gender,
            List<String> phone_numbers, StaffPrivilegeLevel privilege_level, int pending_orders
    ) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.phone_numbers = phone_numbers;
        this.privilege_level= privilege_level;
        this.pending_orders= pending_orders;
    }

    /*========================================================================================*/
    @Override
    public String toString(){

        StringBuilder temp= new StringBuilder();

        temp.append("First Name: " + this.first_name + "\n");
        temp.append("Last Name: " + this.last_name + "\n");
        temp.append("Email: " + this.email + "\n");
        temp.append("Address: " + this.address + "\n");
        temp.append("Gender: " + this.gender + "\n");
        temp.append("Phone numbers: ");
        //Loop through phone numbers and attach in a comma-seperated format except the last phone number
        List<String> phone_numbers_exclude_last= this.phone_numbers.subList(0, (this.phone_numbers.size() - 1) );
        phone_numbers_exclude_last.forEach( phone_number -> {
            temp.append(phone_number + ", ");
        } );

        //Last Phone number
        temp.append(this.phone_numbers.get(this.phone_numbers.size() - 1) + "\n");

        temp.append("Privilege Level: " + this.privilege_level.getPrivilege_level() + "\n");
        temp.append("Pending Orders: " + this.pending_orders + "\n");

        return temp.toString();

    }

    /**
     * Compare two staff object. Two staff objects are said to be equal if they have the same <b>email</b>
     * @param to_compare
     * @return
     */
    @Override
    public boolean equals(Object to_compare){

        Staff staff_to_compare= (Staff) to_compare;

        if( this.email.equals(staff_to_compare.getEmail()) ){
            return true;
        }
        else {
            return false;
        }

    }

}
