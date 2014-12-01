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
class ServiceRequestReportEntry {
    
    //data values
    private String month = null;
    private String category = null;
    private String num = null;
    
    /**
     * Creates a service request report entry
     * 
     * @param month The month that is associated with this query
     * @param category The category of apartment
     * @param num How many apartments of this type were rented
     */
    public ServiceRequestReportEntry(String month, String category, String num){
        this.month = month;
        this.category = category;
        this.num = num;
    }//end cosntructor
    
    @Override
    public String toString(){
        return (
                "{res_month=" + month + ", " +
                "Issue_Type=" + category + ", " +
                "resolved_time=" + num + "}"
               );
    }//end method toString
    
    public String getMonth(){
        return month;
    }//end method getMonth
    
    public String getCategory(){
        return category;
    }//end method getCategory
    
    public String getNum(){
        return num;
    }//end method getNum
    
}//end class ServiceRequestReportEntry
