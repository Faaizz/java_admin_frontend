package com.faaizz.dev.online_platform.api_outbound.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of an uploadable staff or customer.
 */
public class UploadablePerson implements Cloneable{

    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String new_password;
    private String address;
    private String gender;
    private List<String> phone_numbers;


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

    public String getPassword(){
        return this.password;
    }

    public String getNew_password(){ return this.new_password; }


    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public UploadablePerson(
            String first_name, String last_name, String email, String password, String address,
            String gender, List<String> phone_numbers
    ){

        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password= password;
        this.address = address;
        this.gender = gender;
        this.phone_numbers = phone_numbers;

        //SET NEW PASSWORD TO A BLACK STRING BY DEFAULT, TO SET USE THE PROVIDED SETTER
        this.new_password= "";

    }

    /*========================================================================================*/
    /*  S   E   T   T   E   R   S   */
    public void setNew_password(String new_password){ this.new_password= new_password; }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone_numbers(List<String> phone_numbers) {
        this.phone_numbers = phone_numbers;
    }


    /*========================================================================================*/

    public Object clone() throws CloneNotSupportedException{

        UploadablePerson to_return= (UploadablePerson)super.clone();

        to_return.setFirst_name(new String(this.first_name));
        to_return.setLast_name(new String(this.last_name));
        to_return.setEmail(new String(this.email));
        to_return.setPassword(new String(this.password));
        to_return.setNew_password(new String(this.new_password));
        to_return.setAddress(new String(this.address));
        to_return.setGender(new String(this.gender));

        List<String> new_phone_numbers= new ArrayList<>();

        this.getPhone_numbers().forEach( phone_number->{

            new_phone_numbers.add(phone_number);

        } );

        to_return.setPhone_numbers(new_phone_numbers);

        return to_return;
    }

}
