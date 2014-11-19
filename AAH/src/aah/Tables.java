/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aah;

import java.sql.*;

/**
 * Contains some static methods for connecting to the DB, adding tables, and
 * dropping tables.
 *
 * @author Justin
 */
public class Tables {
    private static final String username = "cs4400_Group_16";
    private static final String pword = "bigADQY9";

    private static Connection conn;

    /**
     * Initialize the connection to the DB using the username and password
     * defined in the final instance variables of this class.
     *
     * @return boolean True if DB connection successful, false otherwise.
     */
    public static boolean initConnection() {
        /* intialize DB connection */
        conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://"
                    + "academic-mysql.cc.gatech.edu/cs4400_Group_16",
                    username, pword);
            if (!conn.isClosed()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Create tables in the DB. Also adds some entries to the created tables
     * for testing purposes.
     *
     * @throws SQLException
     */
    public static void createTables() throws SQLException {
        // create a new table User
        Statement sttable = conn.createStatement();
        sttable.executeUpdate("CREATE TABLE User("
            + "Username VARCHAR(15) NOT NULL, "
            + "Password VARCHAR(20) NOT NULL, "
            + "PRIMARY KEY(Username))");
        sttable.close();

        // create Manager table
        sttable = conn.createStatement();
        sttable.executeUpdate("CREATE TABLE Management("
            + "Username VARCHAR(15) NOT NULL, "
            + "PRIMARY KEY(Username))");
        sttable.close();

        // create Resident table
        sttable = conn.createStatement();
        sttable.executeUpdate("CREATE TABLE Resident("
            + "Username VARCHAR(15) NOT NULL, "
            + "Apt_No INT NOT NULL, "
            + "PRIMARY KEY(Username))");
        sttable.close();

        // create Prospective Resident table
        sttable = conn.createStatement();
        sttable.executeUpdate("CREATE TABLE Prospective_Resident("
            + "Username VARCHAR(15) NOT NULL, "
            + "PRIMARY KEY(Username))");
        sttable.close();

        System.out.println("Tables created.");

        sttable = conn.createStatement();
        sttable.executeUpdate("INSERT INTO User "
            + "VALUES('jcoates8', '1234')");
        sttable.executeUpdate("INSERT INTO User "
            + "VALUES('jtrimm3', '1234')");
        sttable.executeUpdate("INSERT INTO User "
            + "VALUES('wli', '1234')");
        sttable.executeUpdate("INSERT INTO Management "
            + "VALUES('jcoates8')");
        sttable.executeUpdate("INSERT INTO Resident "
            + "VALUES('jtrimm3', 12)");
        sttable.executeUpdate("INSERT INTO Prospective_Resident "
            + "VALUES('wli')");
        sttable.close();

        System.out.println("Values inserted.");
    }

    /**
     * Drop all the tables from the DB.
     * 
     * @throws SQLException
     */
    public static void dropTables() throws SQLException {
        // drop User table
        Statement sttable1 = conn.createStatement();
        sttable1.executeUpdate("DROP TABLE User");
        System.out.println("Dropping TABLE User...");
        sttable1.close();

        sttable1 = conn.createStatement();
        sttable1.executeUpdate("DROP TABLE Management");
        System.out.println("Dropping TABLE Management...");
        sttable1.close();

        sttable1 = conn.createStatement();
        sttable1.executeUpdate("DROP TABLE Resident");
        System.out.println("Dropping TABLE Resident...");
        sttable1.close();

        sttable1 = conn.createStatement();
        sttable1.executeUpdate("DROP TABLE Prospective_Resident");
        System.out.println("Dropping TABLE Prospective_Resident...");
        sttable1.close();
    }

    public static Connection getConnection() {
        return conn;
    }

}
