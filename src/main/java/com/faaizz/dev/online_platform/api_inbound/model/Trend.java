package com.faaizz.dev.online_platform.api_inbound.model;

import java.util.List;

public class Trend {

    /**
     * Java implementation of Trend
     */

    private int id;
    private String name;
    private String description;
    private String gender;
    private int products_number;
    private List<String> images;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getGender() {
        return gender;
    }

    public int getProductsNumber() {
        return products_number;
    }

    public List<String> getImages(){
        return images;
    }


    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    /**
     * Constructor
     *
     * @param id
     * @param name
     * @param description
     * @param gender
     * @param products_number
     * @param images
     */
    public Trend(
            int id, String name, String description, String gender, int products_number, List<String> images
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.products_number= products_number;
        this.images = images;
    }    

    /*========================================================================================*/
    @Override
    public String toString(){

        StringBuilder tempSB= new StringBuilder();
        tempSB.append("ID: " + this.getId() + "\n");
        tempSB.append("Name: " + this.getName() + "\n");
        tempSB.append("Description: " + this.getDescription() + "\n");
        tempSB.append("Gender: " + this.getGender() + "\n");
        this.getImages().forEach( image-> {

            tempSB.append("Image: " + image);

        });
        tempSB.append("Products Number: " + this.getProductsNumber() + "\n");

        return tempSB.toString();

    }

    @Override
    public boolean equals(Object to_compare){

        Trend trend_to_compare= (Trend) to_compare;

        if(
                this.id == trend_to_compare.getId()
        ){

            return true;

        }
        else{
            return false;
        }

    }

}