
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

    private static String curUser;
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
            + "PRIMARY KEY(Username), "
            + "FOREIGN KEY(Apt_No) REFERENCES APARTMENT(Apt_No) "
            + "ON DELETE CASCADE ON UPDATE CASCADE, "
            + "FOREIGN KEY(Username) REFERENCES PROSPECTIVE_RESIDENT(Username) "
            + "ON DELETE CASCADE ON UPDATE CASCADE)");
        sttable.close();

        // create Prospective Resident table
        sttable = conn.createStatement();
        sttable.executeUpdate("CREATE TABLE Prospective_Resident("
            + "Username VARCHAR(15) NOT NULL, "
            + "Name VARCHAR(20) NOT NULL, "
            + "DOB DATE NOT NULL, "
            + "Gender CHAR NOT NULL, "
            + "Pref_Lease INT NOT NULL, "
            + "Category CHAR(9) NOT NULL, "
            + "Move_Date DATE NOT NULL, "
            + "Income INT NOT NULL, "
            + "Prev_Addr VARCHAR(50) NOT NULL, "
            + "Min_Rent INT NOT NULL, "
            + "Max_Rent INT NOT NULL, "
            + "PRIMARY KEY(Username), "
            + "CONSTRAINT name_dob UNIQUE(Name, DOB), "
            + "FOREIGN KEY(Username)REFERENCES USER(Username) "
            + "ON DELETE CASCADE ON UPDATE CASCADE)");
        sttable.close();

        // create Payment_Info table
        sttable = conn.createStatement();
        sttable.executeUpdate("CREATE TABLE Payment_Info("
            + "Card_No INT NOT NULL, "
            + "Cvv INT NOT NULL, "
            + "Name_On_Card VARCHAR(25) NOT NULL, "
            + "Exp_Date DATE NOT NULL, "
            + "Username VARCHAR(15) NOT NULL, "
            + "PRIMARY KEY(Card_No), "
            + "FOREIGN KEY(Username) REFERENCES RESIDENT(Username) "
            + "ON DELETE CASCADE ON UPDATE CASCADE)");
        sttable.close();

        // create Apartment table
        sttable = conn.createStatement();
        sttable.executeUpdate("CREATE TABLE Apartment("
            + "Apt_No INT NOT NULL, "
            + "Rent INT NOT NULL, "
            + "Category CHAR(9) NOT NULL, "
            + "Lease_Term INT, "
            + "Sq_Ft INT NOT NULL, "
            + "Available_On DATE, "
            + "PRIMARY KEY(Apt_No))");
        sttable.close();

        // create Maintenance_Request table
        sttable = conn.createStatement();
        sttable.executeUpdate("CREATE TABLE Maintenance_Request("
            + "Request_Date DATE NOT NULL, "
            + "Resolved_Date DATE, "
            + "Apt_No INT NOT NULL, "
            + "Issue_Type VARCHAR(50) NOT NULL, "
            + "PRIMARY KEY(Request_Date, Apt_No, Issue_Type), "
            + "FOREIGN KEY(Apt_No)REFERENCES APARTMENT(Apt_No) "
            + "ON DELETE CASCADE ON UPDATE CASCADE, "
            + "FOREIGN KEY(Issue_Type)REFERENCES ISSUE(Issue_Type) "
            + "ON DELETE CASCADE ON UPDATE CASCADE)");
        sttable.close();

        // create Issue table
        sttable = conn.createStatement();
        sttable.executeUpdate("CREATE TABLE Issue("
            + "Issue_Type VARCHAR(50) NOT NULL, "
            + "PRIMARY KEY(Issue_Type))");
        sttable.close();

        // create Reminder table
        sttable = conn.createStatement();
        sttable.executeUpdate("CREATE TABLE Reminder("
            + "Date DATE NOT NULL, "
            + "Apt_No INT NOT NULL, "
            + "Message VARCHAR(100) NOT NULL, "
            + "PRIMARY KEY(Date, Apt_No), "
            + "FOREIGN KEY(Apt_No)REFERENCES APARTMENT(Apt_No) "
            + "ON DELETE CASCADE ON UPDATE CASCADE)");
        sttable.close();

        // create Pays_Rent table
        sttable = conn.createStatement();
        sttable.executeUpdate("CREATE TABLE Pays_Rent("
            + "Card_No INT NOT NULL, "
            + "Month VARCHAR(10) NOT NULL, "
            + "Year INT NOT NULL, "
            + "Apt_No INT NOT NULL, "
            + "Amt INT NOT NULL, "
            + "Pay_Date DATE NOT NULL, "
            + "PRIMARY KEY(Card_No, Month, Year), "
            + "FOREIGN KEY(Card_No) REFERENCES PAYMENT_INFO(CardNo) "
            + "ON DELETE CASCADE  ON UPDATE CASCADE, "
            + "FOREIGN KEY(Month) REFERENCES DATE(Month) "
            + "ON DELETE CASCADE ON UPDATE CASCADE, "
            + "FOREIGN KEY(Year) REFERENCES Date(Year) "
            + "ON DELETE CASCADE ON UPDATE CASCADE)");
        sttable.close();

        // create Date table
        sttable = conn.createStatement();
        sttable.executeUpdate("CREATE TABLE Date("
            + "Month INT NOT NULL, "
            + "Year INT NOT NULL, "
            + "PRIMARY KEY(Month, Year))");
        sttable.close();





        System.out.println("Tables created.");

        sttable = conn.createStatement();
        sttable.executeUpdate("INSERT INTO USER "
            + "VALUES('jcoates8', '1234')");
        sttable.executeUpdate("INSERT INTO USER "
            + "VALUES('jtrimm3', '1234')");
        sttable.executeUpdate("INSERT INTO USER "
            + "VALUES('wli', '1234')");
        sttable.executeUpdate("INSERT INTO MANAGEMENT "
            + "VALUES('jcoates8')");
        sttable.executeUpdate("INSERT INTO RESIDENT "
            + "VALUES('jtrimm3', 12)");
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

    public static String getCurrentUser() {
        return curUser;
    }

    public static void setCurrentUser(String u) {
        curUser = u;
    } // aa

}
