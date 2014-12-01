/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

import java.sql.Date;

/**
 * Represents an entry in the ApplicationReview TableView.
 * The TableView can read values from a List of ApartmentEntrys
 * @author Justin
 */
public class ApartmentEntry {


    private final String name;
    private final Date dob;
    private final String gen;
    private final int income;
    private final String cat;
    private final int min;
    private final int max;
    private final Date move;
    private final String term;
    private final String user;
    private final String status;

    public ApartmentEntry(String name, Date dob, String gen, int income,
            String cat, int min, int max, Date move, String term, String user,
            String status) {
        this.name = name;
        this.dob = dob;
        this.gen = gen;
        this.income = income;
        this.cat = cat;
        this.min = min;
        this.max = max;
        this.move = move;
        this.term = term + " months";
        this.user = user;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public Date getDob() {
        return dob;
    }

    public String getGen() {
        return gen;
    }

    public int getIncome() {
        return income;
    }

    public String getCat() {
        return cat;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public Date getMove() {
        return move;
    }

    public String getTerm() {
        return term;
    }

    public String getUser() {
        return user;
    }

}
