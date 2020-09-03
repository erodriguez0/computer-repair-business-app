package erodriguez6.db;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

/**
 *
 * @author esteban
 */
public class CompRepairFXMLController implements Initializable {
    private DBConnection dbConnect;
    private Connection conn;
    @FXML private TableView<Ticket> tTable;
    @FXML private TableView<Part> pTable;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbConnect = new DBConnection();
        
        try {
            conn = dbConnect.getConnection();
            buildTTable();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(CompRepairFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
           if(newSel != null) {
                int i = tTable.getSelectionModel().getSelectedItem().getTNum();
                try {
                    buildPTable(i);
                } catch (SQLException ex) {
                    Logger.getLogger(CompRepairFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
           } 
        });
    }    
    
    private void buildTTable() {
        ObservableList<Ticket> data = tTable.getItems();
        try{      
            String SQL = "select ticket_id, status, total_cost from ticket";            
            ResultSet rs = conn.createStatement().executeQuery(SQL);  
            while(rs.next()){                   
                Ticket tk = new Ticket();
                tk.setTNum(rs.getInt("ticket_id"));
                tk.setTStatus(rs.getString("status"));
//                tk.setTTotalCost(rs.getBigDecimal("total_cost"));
                data.add(tk);                  
            }
            tTable.setItems(data);
        } catch(SQLException e){
              System.out.println("Error on Building Data");            
        }
    }
    private void buildPTable(int i) throws SQLException {
        pTable.getItems().clear();
        ObservableList<Part> data = pTable.getItems();
        try {
            String SQL = String.format("select part_id,model_id,serial_num,part_price from part where ticket_id = %d", i);
            ResultSet rs = conn.createStatement().executeQuery(SQL);
            while(rs.next()) {
               Part pt = new Part();
               pt.setPnum(rs.getInt("part_id"));
//               pt.setPTnum(rs.getInt("ticket_id"));
               pt.setMNum(rs.getInt("model_id"));
               pt.setSNum(rs.getString("serial_num"));
               pt.setPPrice(rs.getBigDecimal("part_price"));
               data.add(pt);
            }
            pTable.setItems(data);
        } catch (SQLException e) {
            System.err.print(e);
        }
    }
}
