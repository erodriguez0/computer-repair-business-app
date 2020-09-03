/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erodriguez6.db;

import java.time.LocalDate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author esteban
 */
public class Problem {
    private final SimpleIntegerProperty problemID, compID;
    private final SimpleStringProperty fname, lname, username, description, status;
    private LocalDate examineDate, submitDate;
    
    public Problem() {
        problemID = new SimpleIntegerProperty(0);
        fname = new SimpleStringProperty("");
//        minit = new SimpleStringProperty("");
        lname = new SimpleStringProperty("");
        username = new SimpleStringProperty("");
        examineDate = null;
        submitDate = null;
        description = new SimpleStringProperty("");
        compID = new SimpleIntegerProperty(0);
        status = new SimpleStringProperty("");
    }
    
    public void setProblemID(int problemID) { this.problemID.set(problemID); }
    public void setFname(String fname) { this.fname.set(fname); }
//    public void setMinit(String minit) { this.minit.set(minit); }
    public void setLname(String lname) { this.lname.set(lname); }
    public void setUsername(String username) { this.username.set(username); }
    public void setExamineDate(LocalDate examineDate) { this.examineDate = examineDate; }
    public void setSubmitDate(LocalDate submitDate) { this.submitDate = submitDate; }
    public void setDescription(String description) { this.description.set(description); }
    public void setCompID(int compID) { this.compID.set(compID); }
    public void setStatus(String status) { this.status.set(status); }
    
    public int getProblemID() { return this.problemID.get(); }
    public String getFname() { return this.fname.get(); }
//    public String getMinit() { return this.minit.get(); }
    public String getLname() { return this.lname.get(); }
    public String getUsername() { return this.username.get(); }
    public LocalDate getExamineDate() { return this.examineDate; }
    public LocalDate getSubmitDate() { return this.submitDate; }
    public String getDescription() { return this.description.get(); }
    public int getCompID() { return this.compID.get(); }
    public String getStatus() { return this.status.get(); }
}
