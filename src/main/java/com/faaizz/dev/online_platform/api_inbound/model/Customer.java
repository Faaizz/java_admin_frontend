package com.faaizz.dev.online_platform.api_inbound.model;

import java.util.List;

public class Customer {

    /**
     * This class is an implementation of <b>Customer</b>
     */

    private String first_name;
    private String last_name;
    private String email;
    private String address;
    private String gender;
    private List<String> phone_numbers;
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

    public List<String> getPhone_numbers() {
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

    public Customer(
            String first_name, String last_name, String email, String address, String gender,
            List<String> phone_numbers, int pending_orders, String activation_status, String newsletters
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

        temp.append("Activation Status: " + this.activation_status + "\n");
        temp.append("Newsletters: " + this.newsletters + "\n");
        temp.append("Pending Orders: " + this.pending_orders + "\n");

        return temp.toString();

    }

    /**
     * Compare two customer object. Two customer objects are said to be equal if they have the same <b>email</b>
     * @param to_compare
     * @return
     */
    @Override
    public boolean equals(Object to_compare){

        Customer staff_to_compare= (Customer) to_compare;

        if( this.email.equals(staff_to_compare.getEmail()) ){
            return true;
        }
        else {
            return false;
        }

    }


}
