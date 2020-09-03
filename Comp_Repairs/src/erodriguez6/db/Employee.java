/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erodriguez6.db;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author esteban
 */
public class Employee {
    private final SimpleStringProperty fName, mInit, lName, 
            jobTitle, username, password;
    private final SimpleIntegerProperty empID, superID, ticketCount;
    
    public Employee() {
        this.empID = new SimpleIntegerProperty(0);
        this.superID = new SimpleIntegerProperty(0);
        this.fName = new SimpleStringProperty("");
        this.mInit = new SimpleStringProperty("");
        this.lName = new SimpleStringProperty("");
        this.jobTitle = new SimpleStringProperty("");
        this.username = new SimpleStringProperty("");
        this.password = new SimpleStringProperty("");
        this.ticketCount = new SimpleIntegerProperty(0);
    }
    
    public Employee(int empID, int superID, String fName, String mInit, 
            String lName, String jobTitle, String username, String password, int ticketCount) {
        this.empID = new SimpleIntegerProperty(empID);
        this.superID = new SimpleIntegerProperty(superID);
        this.fName = new SimpleStringProperty(fName);
        this.mInit = new SimpleStringProperty(mInit);
        this.lName = new SimpleStringProperty(lName);
        this.jobTitle = new SimpleStringProperty(jobTitle);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.ticketCount = new SimpleIntegerProperty(ticketCount);
    }

    public int getEmpID() { return this.empID.get(); }
    public int getSuperID() { return this.superID.get(); }
    public String getFName() { return this.fName.get(); }
    public String getMInit() { return this.mInit.get(); }
    public String getLName() { return this.lName.get(); }
    public String getJobTitle() { return this.jobTitle.get(); }
    public String getUsername() { return this.username.get(); }
    public String getPassword() { return this.password.get(); }
    public int getTicketCount() { return this.ticketCount.get(); }
    
    public void setEmpID(int empID) { this.empID.set(empID); }
    public void setSuperID(int superID) { this.superID.set(superID); }
    public void setFName(String fName) { this.fName.set(fName); }
    public void setMInit(String mInit) { this.mInit.set(mInit); }
    public void setLName(String lName) { this.lName.set(lName); }
    public void setJobTitle(String jobTitle) { this.jobTitle.set(jobTitle); }
    public void setUsername(String username) { this.username.set(username); }
    public void setPassword(String password) { this.password.set(password); }
    public void setTicketCount(int ticketCount) { this.ticketCount.set(ticketCount); }
}
