package com.faaizz.dev.online_platform.api_inbound.model.supplement;

public class TrendTrans {

    /**
     * An intermediate implementation of Trend to enable parsing of JSON API response
     */

    private int id;
    private String name;
    private String description;
    private String gender;
    private int products_number;
    private String images;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender(){
        return gender;
    }

    public String getDescription(){
        return description;
    }

    public int getProductsNumber(){
        return products_number;
    }

    public String getImages() {
        return images;
    }

}