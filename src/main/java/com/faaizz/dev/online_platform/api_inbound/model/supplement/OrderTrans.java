package com.faaizz.dev.online_platform.api_inbound.model.supplement;

public class OrderTrans {

    /**
     * An intermediate implementation of Order to enable proper parsing of JSON API response
     */
    private int id;
    private String products;
    private String reference;
    private double amount;
    private double amount_paid;
    private String customer_email;
    private String staff_email;
    private String status;
    private String est_del_date;
    private String failure_date;
    private String failure_cause;
    private String delivery_date;
    private String created_at;
    private String updated_at;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public int getId() {
        return id;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public String getStaff_email() {
        return staff_email;
    }
    
    public String getProducts() {
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

    public String getStatus() {
        return status;
    }

    public String getEst_del_date() {
        return est_del_date;
    }

    public String getFailure_date() {
        return failure_date;
    }

    public String getFailure_cause() {
        return failure_cause;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public OrderTrans(
            int id, String products, String customer_email,
            String staff_email, String reference, double amount, double amount_paid, String status, String est_del_date, String failure_date,
            String failure_cause, String delivery_date, String created_at, String updated_at
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

}
