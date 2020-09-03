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
public class Customer {
    private SimpleIntegerProperty custID;
    private SimpleStringProperty fName, mInit, lName, email, phone, address, city, state, zip;
    
    public Customer() {
        this.custID = new SimpleIntegerProperty(0);
        this.fName = new SimpleStringProperty("");
        this.mInit = new SimpleStringProperty("");
        this.lName = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.phone = new SimpleStringProperty("");
        this.address = new SimpleStringProperty("");
        this.city = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("");
        this.zip = new SimpleStringProperty("");
    }
    
    public Customer(int custID, String fName, String mInit, String lName) {
        this.custID = new SimpleIntegerProperty(custID);
        this.fName = new SimpleStringProperty(fName);
        this.mInit = new SimpleStringProperty(mInit);
        this.lName = new SimpleStringProperty(lName);
    }
    
    public int getCustID() { return this.custID.get(); }
    public String getFName() { return this.fName.get(); }
    public String getMInit() { return this.mInit.get(); }
    public String getLName() { return this.lName.get(); }
    public String getEmail() { return this.email.get(); }
    public String getPhone() { return this.phone.get(); }
    public String getAddress() { return this.address.get(); }
    public String getCity() { return this.city.get(); }
    public String getState() { return this.state.get(); }
    public String getZip() { return this.zip.get(); }
    
    public void setCustID(int custID) { this.custID.set(custID); }
    public void setFName(String fName) { this.fName.set(fName); }
    public void setMInit(String mInit) { this.mInit.set(mInit); }
    public void setLName(String lName) { this.lName.set(lName); }
    public void setEmail(String email) { this.email.set(email); }
    public void setPhone(String phone) { this.phone.set(phone); }
    public void setAddress(String address) { this.address.set(address); }
    public void setCity(String city) { this.city.set(city); }
    public void setState(String state) { this.state.set(state); }
    public void setZip(String zip) { this.zip.set(zip); }
}
