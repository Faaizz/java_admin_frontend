package com.faaizz.dev.online_platform.api_outbound.model;

public class UploadableOrder{

    private int product_id;
    private String product_size;
    private int product_quantity;
    private String customer_email;
    private String staff_email;
    private String status;
    private String est_del_date;
    private String failure_cause;
    private String failure_date;
    private String delivery_date;


    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public int getProduct_id() {
        return product_id;
    }

    public String getProduct_size() {
        return product_size;
    }

    public int getProduct_quantity() {
        return product_quantity;
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


    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
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

    public UploadableOrder(int product_id, String product_size, int product_quantity, String customer_email) {
        this.product_id = product_id;
        this.product_size = product_size;
        this.product_quantity = product_quantity;
        this.customer_email = customer_email;
        
        this.staff_email = "";
        this.status= "";
        this.est_del_date= "";
        this.failure_cause= "";
        this.failure_date= "";
        this.delivery_date= "";
    }


}