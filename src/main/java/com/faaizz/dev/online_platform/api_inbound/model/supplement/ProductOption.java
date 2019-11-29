package com.faaizz.dev.online_platform.api_inbound.model.supplement;

public class ProductOption {

    /**
     * Implements individual Product Options
     */

    private String size;
    private int quantity;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */
    public String getSize() {
        return size;
    }

    public int getQuantity() {
        return quantity;
    }

    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    /**
     * Constructor
     * @param size Size of option
     * @param quantity Quantity of option
     */
    public ProductOption(String size, int quantity){

        this.size= size;
        this.quantity= quantity;

    }

    /*========================================================================================*/
    @Override
    public String toString(){

        return "Size: " + this.getSize() + "\n" + "Quantity: " + this.getQuantity();

    }

}
