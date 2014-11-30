/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aah;

/**
 *
 * @author Julianna
 */
public class AptDefaults {
    private final int apt;
    private final int extra;
    private final int days;
    
    public AptDefaults(int apt, int extra, int days) {
        this.apt = apt;
        this.extra = extra;
        this.days = days;
    }
    
    public int getApt() {
        return apt;
    }
    
    public int getExtra() {
        return extra;
    }
    
    public int getDays() {
        return days;
    }
}
