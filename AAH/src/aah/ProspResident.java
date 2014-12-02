/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

import java.sql.Date;

/**
 *
 * @author Justin
 */
public class ProspResident {
    private String name, gender, cat, lease, user;
    private int min, max, income;
    private Date dob, move;

    public int getIncome() {
        return income;
    }

    public void setIncome(int i) {
        income = i;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getCat() {
        return cat;
    }

    public String getLease() {
        return lease;
    }

    public String getUser() {
        return user;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public Date getDob() {
        return dob;
    }

    public Date getMove() {
        return move;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public void setLease(String lease) {
        this.lease = lease;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setMove(Date move) {
        this.move = move;
    }



}
