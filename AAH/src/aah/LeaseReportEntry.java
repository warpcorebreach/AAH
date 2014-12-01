/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

/**
 * Encapsulates a data row of the Lease Report
 *
 * @author Brendan
 */
public class LeaseReportEntry {

    private final String month;
    private final String category;
    private final String apt_count;

    public LeaseReportEntry(String month, String category, String apt_count){
        this.month = month;
        this.category = category;
        this.apt_count = apt_count;
    }//end constructor

    @Override
    public String toString(){
        return (
                "{Month=" + month + ", " +
                "Category=" + category + ", " +
                "apt_number=" + apt_count + "}"
               );
    }//end method toString

    public String getMonth(){
        return month;
    }//end method getMonth

    public String getCategory(){
        return category;
    }//end method getCategory

    public String getApt_count(){
        return apt_count;
    }//end method getApt_count
    
}//end class
