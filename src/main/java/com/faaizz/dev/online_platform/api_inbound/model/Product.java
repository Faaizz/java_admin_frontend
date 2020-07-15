package com.faaizz.dev.online_platform.api_inbound.model;

import com.faaizz.dev.online_platform.api_inbound.model.supplement.ProductOption;

import java.util.List;

public class Product {

    /**
     * Java implementation of Product
     */

    private int id;
    private String name;
    private String brand;
    private String description;
    private String section;
    private String sub_section;
    private String category;
    private double price;
    private String colors;
    private String material;
    private List<String> images;
    private List<ProductOption> options;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public String getSection() {
        return section;
    }

    public String getSub_section() {
        return sub_section;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String getColors() {
        return colors;
    }

    public String getMaterial() {
        return material;
    }

    public List<String> getImages() {
        return images;
    }

    public List<ProductOption> getOptions() {
        return options;
    }


    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    /**
     * Constructor
     *
     * @param id
     * @param name
     * @param brand
     * @param description
     * @param section
     * @param sub_section
     * @param category
     * @param price
     * @param colors
     * @param material
     * @param images
     * @param options
     */
    public Product(
            int id, String name, String brand, String description, String section, String sub_section,
            String category, double price, String colors, String material, List<String> images,
            List<ProductOption> options
    ) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.section = section;
        this.sub_section = sub_section;
        this.category = category;
        this.price = price;
        this.colors = colors;
        this.material = material;
        this.images = images;
        this.options = options;
    }

    /*========================================================================================*/
    @Override
    public String toString(){

        StringBuilder tempSB= new StringBuilder();
        tempSB.append("ID: " + this.getId() + "\n");
        tempSB.append("Name: " + this.getName() + "\n");
        tempSB.append("Brand: " + this.getBrand() + "\n");
        tempSB.append("Description: " + this.getDescription() + "\n");
        tempSB.append("Section: " + this.getSection() + "\n");
        tempSB.append("Sub-section: " + this.getSub_section() + "\n");
        tempSB.append("Category: " + this.getCategory() + "\n");
        tempSB.append("Price: " + this.getPrice() + "\n");
        tempSB.append("Colors: " + this.getColors() + "\n");
        tempSB.append("Material: " + this.getMaterial() + "\n");

        this.getImages().forEach( image-> {

            tempSB.append("Image: " + image);

        });

        this.getOptions().forEach( option-> {

            tempSB.append(option.toString());

        });


        return tempSB.toString();

    }

    @Override
    public boolean equals(Object to_compare){

        Product product_to_compare= (Product) to_compare;

        if(
                this.id == product_to_compare.getId()
        ){

            return true;

        }
        else{
            return false;
        }

    }
}
