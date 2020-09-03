/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erodriguez6.db;

import java.math.BigDecimal;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author esteban
 */
public class Part {
    private final SimpleIntegerProperty pNum, tNum, mNum;
    private BigDecimal pPrice;
    private final SimpleStringProperty sNum;
    
    public Part() {
        this.pNum = new SimpleIntegerProperty(0);
        this.tNum = new SimpleIntegerProperty(0);
        this.mNum = new SimpleIntegerProperty(0);
        this.pPrice = BigDecimal.ZERO;
        this.sNum = new SimpleStringProperty("");
    }
    
    public Part(int pNum, int tNum, int mNum, String sNum, BigDecimal pPrice) {
        this.pNum = new SimpleIntegerProperty(pNum);
        this.tNum = new SimpleIntegerProperty(tNum);
        this.mNum = new SimpleIntegerProperty(mNum);
        this.pPrice = pPrice;
        this.sNum = new SimpleStringProperty(sNum);
    }
    
    public int getPNum() { return this.pNum.get(); }
    public int getTNum() { return this.tNum.get(); }
    public int getMNum() { return this.mNum.get(); }
    public BigDecimal getPPrice() { return this.pPrice; }
    public String getSNum() { return this.sNum.get(); }
    
    public void setPnum(int pNum) { this.pNum.set(pNum); }
    public void setTNum(int tNum) { this.tNum.set(tNum); }
    public void setMNum(int mNum) { this.mNum.set(mNum); }
    public void setPPrice(BigDecimal pPrice) { this.pPrice = pPrice; }
    public void setSNum(String sNum) { this.sNum.set(sNum); }
}
