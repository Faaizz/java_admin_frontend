package com.faaizz.dev.online_platform.api_inbound.model.supplement;

public class ProductTrans {

    /**
     * An intermediate implementation of Product to enable proper parsing of JSON API response
     */

    private int id;
    private String name;
    private String brand;
    private String description;
    private String section;
    private String sub_section;
    private String category;
    private double price;
    private String color;
    private String material;
    private String images;
    private String options;

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

    public String getColor() {
        return color;
    }

    public String getMaterial() {
        return material;
    }

    public String getImages() {
        return images;
    }

    public String getOptions() {
        return options;
    }

    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public ProductTrans(
            int id, String name, String brand, String description, String section, String sub_section,
            String category, double price, String color, String material, String images, String options
    ) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.section = section;
        this.sub_section = sub_section;
        this.category = category;
        this.price = price;
        this.color = color;
        this.material = material;
        this.images = images;
        this.options = options;
    }
}
