/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

/**
 * This class serves as a way to ecapsulate 1 row of data in the Service
 * Request Report.
 *
 * @author Brendan
 */
public class ServiceRequestReportEntry {

    //data values
    private final String month;
    private final String category;
    private final String num;

    /**
     * Creates a service request report entry
     *
     * @param month The month that is associated with this query
     * @param category The category of apartment
     * @param num How many apartments of this type were rented
     */
    public ServiceRequestReportEntry(String month, String category, String num) {
        this.month = month;
        this.category = category;
        this.num = num;
    }//end cosntructor

    public String getMonth() {
        return month;
    }

    public String getCategory() {
        return category;
    }

    public String getNum() {
        return num;
    }

    public String toString(){
        return (
                "{res_month=" + month + ", " +
                "Issue_Type=" + category + ", " +
                "resolved_time=" + num + "}"
               );
    }//end method toString

}//end class ServiceRequestReportEntry
