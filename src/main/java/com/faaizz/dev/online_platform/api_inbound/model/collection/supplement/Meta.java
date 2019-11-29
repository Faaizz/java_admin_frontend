package com.faaizz.dev.online_platform.api_inbound.model.collection.supplement;

public class Meta {

    /**
     * This Class is an implementation of response <b>meta</b> field when multiple resources are returned from an API call.
     */

    private int current_page;
    private int from;
    private int last_page;
    private String path;
    private int per_page;
    private int to;
    private int total;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public int getCurrent_page() {
        return current_page;
    }

    public int getFrom() {
        return from;
    }

    public int getLast_page() {
        return last_page;
    }

    public String getPath() {
        return path;
    }

    public int getPer_page() {
        return per_page;
    }

    public int getTo() {
        return to;
    }

    public int getTotal() {
        return total;
    }

    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    /**
     * Contructor
     * @param current_page
     * @param from
     * @param last_page
     * @param path
     * @param per_page
     * @param to
     * @param total
     */
    public Meta(int current_page, int from, int last_page, String path, int per_page, int to, int total) {
        this.current_page = current_page;
        this.from = from;
        this.last_page = last_page;
        this.path = path;
        this.per_page = per_page;
        this.to = to;
        this.total = total;
    }

    /*========================================================================================*/

    @Override
    public String toString(){

        StringBuilder tempSB= new StringBuilder();

        tempSB.append("Current page: " + this.current_page + "\n");
        tempSB.append("from: " + this.from + "\n");
        tempSB.append("Last page: " + this.last_page + "\n");
        tempSB.append("Path: " + this.path + "\n");
        tempSB.append("Per page: " + this.per_page + "\n");
        tempSB.append("To: " + this.to + "\n");
        tempSB.append("Total: " + this.total + "\n");

        return tempSB.toString();

    }

}
