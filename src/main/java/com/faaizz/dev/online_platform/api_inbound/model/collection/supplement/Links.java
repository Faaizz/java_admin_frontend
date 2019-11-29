package com.faaizz.dev.online_platform.api_inbound.model.collection.supplement;

public class Links {

    /**
     * This Class is an implementation of response <b>links</b> field when multiple resources are returned from an API call.
     */

    private String first;
    private String last;
    private String prev;
    private String next;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    public String getPrev() {
        return prev;
    }

    public String getNext() {
        return next;
    }

    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public Links(String first, String last, String prev, String next) {
        this.first = first;
        this.last = last;
        this.prev = prev;
        this.next = next;
    }

    /*========================================================================================*/

    @Override
    public String toString(){

        StringBuilder tempSB= new StringBuilder();

        tempSB.append("First: " + this.first + "\n");
        tempSB.append("Last: " + this.last + "\n");
        tempSB.append("Prev: " + this.prev + "\n");
        tempSB.append("Next: " + this.next + "\n");

        return tempSB.toString();

    }

}
