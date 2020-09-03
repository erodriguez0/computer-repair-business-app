/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erodriguez6.db;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

/**
 *
 * @author esteban
 */
public class Ticket {
    private final SimpleIntegerProperty tNum, pNum;
    private BigDecimal tTotalCost;
    private final SimpleStringProperty tStatus, tDetails, tShipLabel, type;
    private LocalDate beginDate, endDate;
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Button button;

    public Ticket() {
        this.tNum = new SimpleIntegerProperty(0);
        this.pNum = new SimpleIntegerProperty(0);
        this.tStatus = new SimpleStringProperty("");
        this.tDetails = new SimpleStringProperty("");
        this.tShipLabel = new SimpleStringProperty("");
        this.tTotalCost = BigDecimal.ZERO;
        this.beginDate = null;
        this.endDate = null;
        this.type = new SimpleStringProperty("");
//        this.button = null;
    }
    public Ticket(int tNum, int pNum, String tStatus, String tDetails, 
            BigDecimal tTotalCost, String tShipLabel, LocalDate beginDate, LocalDate endDate, String type) {
        this.tNum = new SimpleIntegerProperty(tNum);
        this.pNum = new SimpleIntegerProperty(pNum);
        this.tStatus = new SimpleStringProperty(tStatus);
        this.tDetails = new SimpleStringProperty(tDetails);
        this.tShipLabel = new SimpleStringProperty(tShipLabel);
        this.tTotalCost = tTotalCost;
        this.beginDate = LocalDate.parse(df.format(beginDate));
        this.endDate = LocalDate.parse(df.format(endDate));
        this.type = new SimpleStringProperty(type);
//        this.button = button;
    }
    
    public int getTNum() { return this.tNum.get(); }
    public int getPNum() { return this.pNum.get(); }
    public BigDecimal getTTotalCost() { return this.tTotalCost; }
    public String getTStatus() { return this.tStatus.get(); }
    public String getTDetails() { return this.tDetails.get(); }
    public String getTShipLabel() { return this.tShipLabel.get(); }
    public LocalDate getBeginDate() { return this.beginDate; }
    public LocalDate getEndDate() { return this.endDate; }
    public String getType() { return this.type.get(); }
//    public Button getButton() { return this.button; }
    
    public void setTNum(int tNum) { this.tNum.set(tNum); }
    public void setPNum(int pNum) { this.pNum.set(pNum); }
    public void setTTotalCost(BigDecimal tTotalCost) { this.tTotalCost = tTotalCost; }
    public void setTStatus(String tStatus) { this.tStatus.set(tStatus); }
    public void setTDetails(String tDetails) { this.tDetails.set(tDetails); }
    public void setTShipLabel(String tShipLabel) { this.tShipLabel.set(tShipLabel); }
    public void setBeginDate(LocalDate beginDate) { this.beginDate = beginDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setType(String type) { this.type.set(type); }
//    public void setButton(Button button) { this.button = button; }
}
