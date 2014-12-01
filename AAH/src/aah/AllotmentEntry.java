/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

import java.sql.Date;

/**
 * Encapsulates data used to populate the Apartment Allotment Table
 * @author Justin
 */
public class AllotmentEntry {
    private final int apt, rent, feet;
    private final String cat;
    private final Date avail;

    public AllotmentEntry(int apt, String cat, int rent, int feet, Date avail) {
        this.apt = apt;
        this.rent = rent;
        this.feet = feet;
        this.cat = cat;
        this.avail = avail;
    }

    public int getApt() {
        return apt;
    }

    public int getRent() {
        return rent;
    }

    public int getFeet() {
        return feet;
    }

    public String getCat() {
        return cat;
    }

    public Date getAvail() {
        return avail;
    }

    public String toString() {
        return apt + " " + rent + " " + feet + " " + cat + " " + avail;
    }
}
