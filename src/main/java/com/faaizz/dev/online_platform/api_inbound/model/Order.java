package com.faaizz.dev.online_platform.api_inbound.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Order {

    /**
     * Java implementation of Order
     */

    private int id;
    private List<Map<String,String>> products;
    private String reference;
    private double amount;
    private double amount_paid;
    private String customer_email;
    private String staff_email;
    private String status;
    private LocalDateTime est_del_date;
    private LocalDateTime failure_date;
    private String failure_cause;
    private LocalDateTime delivery_date;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public int getId() {
        return id;
    }

    public List<Map<String,String>> getProducts() {
        return products;
    }


    public String getReference() {
        return reference;
    }

    public double getAmount() {
        return amount;
    }

    public double getAmount_paid() {
        return amount_paid;
    }


    public String getCustomer_email() {
        return customer_email;
    }

    public String getStaff_email() {
        return staff_email;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getEst_del_date() {
        return est_del_date;
    }

    public LocalDateTime getFailure_date() {
        return failure_date;
    }

    public String getFailure_cause() {
        return failure_cause;
    }

    public LocalDateTime getDelivery_date() {
        return delivery_date;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }
    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public Order(
            int id, List<Map<String,String>> products, String customer_email, String reference, double amount, double amount_paid, 
            String staff_email, String status, LocalDateTime est_del_date, LocalDateTime failure_date,
            String failure_cause, LocalDateTime delivery_date, LocalDateTime created_at, LocalDateTime updated_at
    ) {
        this.id = id;
        this.products = products;
        this.reference = reference;
        this.amount = amount;
        this.amount_paid = amount_paid;
        this.customer_email = customer_email;
        this.staff_email = staff_email;
        this.status = status;
        this.est_del_date = est_del_date;
        this.failure_date = failure_date;
        this.failure_cause = failure_cause;
        this.delivery_date = delivery_date;
        this.created_at= created_at;
        this.updated_at= updated_at;
    }

    /*========================================================================================*/
    @Override
    public String toString(){

        StringBuilder tempSB= new StringBuilder();

        tempSB.append("ID: " + this.getId() + "\n");
        tempSB.append("Products: " + this.getProducts() + "\n");
        tempSB.append("Customer Email: " + this.getCustomer_email() + "\n");
        tempSB.append("Staff Email: " + this.getStaff_email() + "\n");
        tempSB.append("Status " + this.getStatus() + "\n");
        tempSB.append("Est Delivery Date: " + this.getEst_del_date() + "\n");
        tempSB.append("Failure Date: " + this.getFailure_date() + "\n");
        tempSB.append("Failure Cause: " + this.getFailure_cause() + "\n");
        tempSB.append("Delivery Date: " + this.getDelivery_date() + "\n");
        tempSB.append("Created at: " + this.getCreated_at() + "\n");
        tempSB.append("Updates at: " + this.getUpdated_at() + "\n");


        return tempSB.toString();

    }

    @Override
    public boolean equals(Object to_compare){

        Order to_compare_order= (Order) to_compare;

        if( this.id == to_compare_order.getId() ){

            return true;

        }
        else{
            return false;
        }

    }
}
