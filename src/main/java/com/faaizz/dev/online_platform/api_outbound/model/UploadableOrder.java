package com.faaizz.dev.online_platform.api_outbound.model;

public class UploadableOrder{

    private String products;
    private String customer_email;
    private String staff_email;
    private String status;
    private String est_del_date;
    private String failure_cause;
    private String failure_date;
    private String delivery_date;


    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public String getProducts() {
        return products;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public String getStaff_email() {
        return staff_email;
    }
    
    public String getDelivery_date() {
        return delivery_date;
    }

    public String getFailure_date() {
        return failure_date;
    }

    public String getFailure_cause() {
        return failure_cause;
    }

    public String getEst_del_date() {
        return est_del_date;
    }

    public String getStatus() {
        return status;
    }


    /*========================================================================================*/
    /*  S   E   T   T   E   R   S   */

    public void setProducts(String products) {
        this.products = products;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public void setStaff_email(String staff_email) {
        this.staff_email = staff_email;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEst_del_date(String est_del_date) {
        this.est_del_date = est_del_date;
    }

    public void setFailure_cause(String failure_cause) {
        this.failure_cause = failure_cause;
    }

    public void setFailure_date(String failure_date) {
        this.failure_date = failure_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }
    


    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public UploadableOrder(String products, String customer_email) {
        this.products = products;
        this.customer_email = customer_email;
        
        this.staff_email = "";
        this.status= "";
        this.est_del_date= "";
        this.failure_cause= "";
        this.failure_date= "";
        this.delivery_date= "";
    }



}