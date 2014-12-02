
package aah;

import java.sql.*;

import javafx.stage.*;

/**
 * Contains some static methods for connecting to the DB, adding tables, and
 * dropping tables.
 *
 * @author Justin
 */
public class Tables {

    //DB credentials
    private static final String username = "cs4400_Group_16";
    private static final String pword = "bigADQY9";

    //
    private static String curUser = null;
    private static String curName = null;
    private static String curNewResident;
    private static Connection conn = null;

    //maximum number of times to try and connect/disconnect
    //  from the DB
    private static final int MAX_CONN_OPEN_ATTEMPTS = 3;
    private static final int MAX_CONN_CLOSE_ATTEMPTS = 3;

    //stage object
    private static Stage stage = null;

    /**
     * Initialize the connection to the DB using the username and password
     * defined in the final instance variables of this class.
     *
     * @return boolean True if DB connection successful, false otherwise.
     */
    public static boolean initConnection() {

        int i = 0;
        boolean isConnected = false;

        System.out.println("Attempting to establish database connection...");

        //sanity check- make sure the connection object is isn't already initialized
        if(conn != null){
            System.out.println("ERROR: cannot open database connection; connection object already initialized");
            System.out.println("\tClose the connection first");
        }//end if
        //by this point we know the database connection hasn't already been initialized
        else{
            //try to connect for the specified number of times
            for(i = 0; i < MAX_CONN_OPEN_ATTEMPTS; i++){
                System.out.print("[" + (i+1) + "/" + MAX_CONN_OPEN_ATTEMPTS + "]: ");
                try {
                    //create new DB connection instance
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conn = DriverManager.getConnection("jdbc:mysql://"
                            + "academic-mysql.cc.gatech.edu/cs4400_Group_16",
                            username, pword);
                    //we got a good connection.  We need to set the return flag
                    //  and stop trying to connect
                    if (conn != null && !conn.isClosed()) {
                        isConnected = true;
                        break;
                    }
                }//end try
                //oh God no...
                catch (Exception ex) {
                    System.out.println("ERROR: cannot open connection");
                    System.out.println("\t" + ex.getMessage());
                }//end catch
            }//end for
        }//end else

        //connection was opened successfully
        if(isConnected){
            System.out.println("Connection opened successfully.");
        }//end if
        //we didn't make it...
        else{
            System.out.println("FAILED to open connection after [" + MAX_CONN_OPEN_ATTEMPTS + "] attempts; timed out.");
        }//end else

        return isConnected;

    }//end method initConnection

    /**
     * This method closes the active DB connection
     *
     * @return True on successful database close; false otherwise
     */
    static boolean closeConnection() {

        int i = 0;
        boolean isClosed = false;

        System.out.println("Attempting to close database connection...");

        //sanity check
        if(conn == null){
            System.out.println("ERROR: cannot close connection; connection object is [null]");
        }//end if
        //at this point, we know the connection object is not null and therefore
        //  must have been initialized by the call to initConnection()
        else{
            //try to close the connection a few times
            for(i = 0; i < MAX_CONN_CLOSE_ATTEMPTS; i++){
                try{
                    System.out.print("[" + (i+1) + "/" + MAX_CONN_CLOSE_ATTEMPTS + "]: ");
                    //attempt to close the connection
                    conn.close();
                    //if we got to here, then we know the connection
                    //  was closed successfully.  otherwise, it would
                    //  have thrown an exception and jumped to the catch
                    //  clause.  Break out of the for loop- stop trying
                    //  to close it.
                    //Make sure we destroy the old reference too
                    conn = null;
                    isClosed = true;
                    break;
                }//end try
                //uh-oh...
                catch(Exception ex){
                    System.out.println("ERROR: cannot close connection");
                    System.out.println("\t" + ex.getMessage());
                }//end catch
            }//end for
        }//end else

        //connection was closed successfully
        if(isClosed){
            System.out.println("Connection closed successfully.");
        }//end if
        //we didn't make it...
        else{
            System.out.println("FAILED to close connection after [" + MAX_CONN_CLOSE_ATTEMPTS + "] attempts; timed out.");
        }//end else

        return isClosed;

    }//end method closeConnection

    /**
     * Create tables in the DB. Also adds some entries to the created tables
     * for testing purposes.
     *
     * @throws SQLException
     */
    public static void createTables() throws SQLException {
        // create a new table User
        Statement sttable = conn.createStatement();
        try {
            sttable.executeUpdate("CREATE TABLE User("
                + "Username VARCHAR(15) NOT NULL, "
                + "Password VARCHAR(20) NOT NULL, "
                + "PRIMARY KEY(Username))");
            sttable.close();
        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // create Manager table
        try {
            sttable = conn.createStatement();
            sttable.executeUpdate("CREATE TABLE Management("
                + "Username VARCHAR(15) NOT NULL, "
                + "PRIMARY KEY(Username))");
            sttable.close();
        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
        }

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
            + "Card_No CHAR(16) NOT NULL, "
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
            + "Card_No CHAR(16) NOT NULL, "
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
        sttable1.executeUpdate("DROP TABLE USER");
        System.out.println("Dropping TABLE User...");
        sttable1.close();

        sttable1 = conn.createStatement();
        sttable1.executeUpdate("DROP TABLE MANAGEMENT");
        System.out.println("Dropping TABLE Management...");
        sttable1.close();

        sttable1 = conn.createStatement();
        sttable1.executeUpdate("DROP TABLE RESIDENT");
        System.out.println("Dropping TABLE Resident...");
        sttable1.close();

        sttable1 = conn.createStatement();
        sttable1.executeUpdate("DROP TABLE PROSPECTIVE_RESIDENT");
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
    }

    /**
     * This method saves the Stage object from the call to start(Stage stage)
     * in AAH.java
     *
     * @param s The stage object to save for later
     */
    public static void setStage(Stage s){
        stage = s;
    }//end method setStage

    /**
     * This method returns the root stage object for use in other
     * GUI parts of the program
     *
     * @return The stage object that was given to us from start(Stage stage)
     *          in AAH.java
     */
    public static Stage getStage(){
        return stage;
    }//end method getStage

    public static void setCurName(String n) {
        curName = n;
    }

    public static String getCurName() {
        return curName;
    }

    public static void setNewResident(String r) {
        curNewResident = r;
    }

    public static String getNewResident() {
        return curNewResident;
    }

}
