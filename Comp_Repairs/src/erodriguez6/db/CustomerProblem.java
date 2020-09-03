/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erodriguez6.db;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author esteban
 */

public class CustomerProblem {
    private final SimpleIntegerProperty  custID, equipID, problemID;
    private final SimpleStringProperty fName, mInit, lName, address, city, state, zip, phone, 
            username, eaddress, ecity, estate, ezip, description;
    public CustomerProblem() {
        this.custID = new SimpleIntegerProperty(0);
        this.equipID = new SimpleIntegerProperty(0);
        this.problemID = new SimpleIntegerProperty(0);
        this.fName = new SimpleStringProperty("");
        this.mInit = new SimpleStringProperty("");
        this.lName = new SimpleStringProperty("");
        this.address = new SimpleStringProperty("");
        this.city = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("");
        this.zip = new SimpleStringProperty("");
        this.phone = new SimpleStringProperty("");
        this.username = new SimpleStringProperty("");
        this.eaddress = new SimpleStringProperty("");
        this.ecity = new SimpleStringProperty("");
        this.estate = new SimpleStringProperty("");
        this.ezip = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
    }
    
    public void setCustID(int custID) { this.custID.set(custID); }
    public void setEquipID(int equipID) { this.custID.set(equipID); }
    public void setProblemID(int problemID) { this.problemID.set(problemID); }
    public void setFName(String fName) { this.fName.set(fName); }
    public void setMInit(String mInit) { this.mInit.set(mInit); }
    public void setLName(String lName) { this.lName.set(lName); }
    public void setAddress(String address) { this.address.set(address); }
    public void setCity(String city) { this.city.set(city); }
    public void setState(String state) { this.state.set(state); }
    public void setZip(String zip) { this.zip.set(zip); }
    public void setPhone(String phone) { this.phone.set(phone); }
    public void setUsername(String username) { this.username.set(username); }
    public void setEAddress(String eaddress) { this.eaddress.set(eaddress); }
    public void setECity(String ecity) { this.ecity.set(ecity); }
    public void setEState(String estate) { this.estate.set(estate); }
    public void setEZip(String ezip) { this.ezip.set(ezip); }
    public void setDescription(String description) { this.description.set(description); }
    
    public int getCustID() { return this.custID.get(); }
    public int getEquipID() { return this.equipID.get(); }
    public int getProblemID() { return this.problemID.get(); }
    public String getFName() { return this.fName.get(); }
    public String getMInit() { return this.mInit.get(); }
    public String getLName() { return this.lName.get(); }
    public String getAddress() { return this.address.get(); }
    public String getCity() { return this.city.get(); }
    public String getState() { return this.state.get(); }
    public String getZip() { return this.zip.get(); }
    public String getPhone() { return this.phone.get(); }
    public String getUsername() { return this.username.get(); }
    public String getEAddress() { return this.eaddress.get(); }
    public String getECity() { return this.ecity.get(); }
    public String getEState() { return this.estate.get(); }
    public String getEZip() { return this.ezip.get(); }
    public String getDescription() { return this.description.get(); }
}
