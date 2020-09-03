package erodriguez6.db;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
//import oracle.jdbc.driver.OracleDriver;

/**
 *
 * @author esteban
 */
public class DBConnection {
//    Connection cnn;
//    Statement stmt;
//    ResultSet res;
//    String url = "jdbc:postgresql://localhost:5432/comp_repairs";
//    String userName, dbUserName, password;
//    Scanner scan;
//    
//    public DBConnection(String userName, String password) {
//        scan = new Scanner(System.in);
//        this.userName = System.getProperty("user.name");
//        this.dbUserName = userName;
//        this.password = password;
//        try{
//            //This comes from ojdbc6.jar you put on your class path.
//            Driver driver = new org.postgresql.Driver();
//            DriverManager.registerDriver(driver);
//            //connect to the database on delphi
//            this.cnn = DriverManager.getConnection(url,
//                    userName,
//                    password);
//            //Create a satement from the connection
//            stmt = cnn.createStatement();
//
//        } catch( SQLException ex){
//            System.err.print(ex);
//            System.exit(-1);
//        }      
//    }
    
    public Connection getConnection() throws ClassNotFoundException, SQLException{       
      Driver driver = new org.postgresql.Driver();
      DriverManager.registerDriver(driver);
      return DriverManager.getConnection("jdbc:postgresql://localhost:5432/comp_repairs","repairs","c3m4p2s"); 
    }
    
//    private void closeCnn(){
//        try{
//            stmt.close();
//            res.close();
//            cnn.close();
//        } catch (SQLException ex){
//            System.err.format("Err closing connection: %s\n", ex);
//        }
//    } 

//    private void getEmployeeInfo() throws SQLException {
//        try {
//            String query = String.format("select * from employee_info");
//            this.res = this.stmt.executeQuery(query);
//            while(this.res.next()) {
//                System.out.format("%02d    %-10s %-15s %25s    %-15s %5s %5s %-12s %15s\n", 
//                        this.res.getInt(1), this.res.getString(2), this.res.getString(4), 
//                        this.res.getString(5), this.res.getString(6), this.res.getString(7),
//                        this.res.getString(8), this.res.getString(9), this.res.getString(10));
//            }
//        } catch (SQLException e) {
//            System.err.print(e);
//        }
//    }
    
//    private void getEmployeeInfo(int emp_id) throws SQLException {
//        try {
//            String query = String.format("select * from employee_info where emp_id = %d", emp_id);
//            this.res = this.stmt.executeQuery(query);
//            while(this.res.next()) {
//                System.out.format("%02d    %-10s %-15s %25s    %-15s %5s %5s %-12s %15s\n", 
//                        this.res.getInt(1), this.res.getString(2), this.res.getString(4), 
//                        this.res.getString(5), this.res.getString(6), this.res.getString(7),
//                        this.res.getString(8), this.res.getString(9), this.res.getString(10));
//            }
//        } catch (SQLException e) {
//            System.err.print(e);
//        }
//    }
    
//    /**
//     * @param args the command line arguments
//     * @throws java.sql.SQLException
//     */
//    public static void main(String[] args) throws SQLException {
//    //Create a new AssignmentReview object -- Don't ever put credentials in code like this! --
//        DBConnection db = new DBConnection("repairs", "c3m4p2s");
//        String menuSel = "";
//        while(!menuSel.equalsIgnoreCase("Q")) {
//            System.out.print("Do you want to [Q]uit, [A]ll Employees, or [V]iew Specficic Employee: ");
//            menuSel = db.scan.nextLine();
//            if(menuSel.equalsIgnoreCase("V")) {
//                System.out.print("Enter an Employee ID: ");
//                int emp = db.scan.nextInt();
//                db.getEmployeeInfo(emp);
//            }
//            else if(menuSel.equalsIgnoreCase("A")) {
//                    db.getEmployeeInfo();
//            }
//        }
//        db.closeCnn();
//    }
}
