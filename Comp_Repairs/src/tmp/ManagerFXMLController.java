/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erodriguez6.db;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 *
 * @author esteban
 */
public class ManagerFXMLController implements Initializable {
    private DBConnection dbConnect;
    private Connection conn;
    private ResultSet rs;
    private CallableStatement cStmnt;
    private DateTimeFormatter df;
    
    @FXML TableView<Employee> empTable;
    @FXML TableView<Ticket> empTicketTable;
    @FXML Label numOfTickets, generatedAmount, empNumOfTickets, moneyLabel, empGeneratedAmount, empNameLabel;
    @FXML Button applyDate;
    @FXML DatePicker beginDate, endDate;
    @FXML ImageView empImg;
    @FXML TextField empSearch;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Try to build employee table when logged in
        try {
            buildEmpTable();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ManagerFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Default values for datepickers
        beginDate.getEditor().setEditable(false);
        endDate.getEditor().setEditable(false);
        beginDate.setValue(LocalDate.now().minusWeeks(2));
        endDate.setValue(LocalDate.now());
        
        //Display employee information when selected
        empTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
           if(newSel != null) {
                int i = empTable.getSelectionModel().getSelectedItem().getEmpID();
                try {
                    beginDate.setValue(null);
                    endDate.setValue(null);
                    buildEmpTicketTable(i);
                    setEmpLabels(i);
                    empTicketTable.setVisible(true);
                    empImg.setVisible(true);
                    beginDate.setVisible(true);
                    endDate.setVisible(true);
                    applyDate.setVisible(true);
                    empNumOfTickets.setVisible(true);
                    generatedAmount.setVisible(true);
                    moneyLabel.setVisible(true);
                    empGeneratedAmount.setVisible(true);
                    numOfTickets.setVisible(true);
                    empNameLabel.setVisible(true);
                } catch (ClassNotFoundException | SQLException ex) {
                   Logger.getLogger(ManagerFXMLController.class.getName()).log(Level.SEVERE, null, ex);
               }
           } 
        });
        
        //Select first employee upon logging in
        empTable.getSelectionModel().selectFirst();
        
        //Listener for employee search bar
        ObservableList<Employee> data = FXCollections.observableList(empTable.getItems());
        FilteredList<Employee> filteredData = new FilteredList<>(data, e -> true);
        empSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Employee -> {
                // If filter text is empty, display all employees
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name field in your object with filter
                String lowerCaseFilter = newValue.toLowerCase();

                // Filter matches first name
                if (String.valueOf(Employee.getFName()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                    
                // Filter matches last name
                } else if (String.valueOf(Employee.getLName()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                    
                // Filter matches id
                } else if (Integer.toString(Employee.getEmpID()).contains(newValue)) {
                    return true;
                }
                
                // Does not match any
                return false; 
            });
        });
        //Wrap the FilteredList in a SortedList
        SortedList<Employee> sortedData = new SortedList<>(filteredData);
        //Bind the SortedList comparator to the TableView comparator
        sortedData.comparatorProperty().bind(empTable.comparatorProperty());
        //Add sorted (and filtered) data to the table
        empTable.setItems(sortedData);
        
//        ObservableList<Ticket> tData = FXCollections.observableList(empTicketTable.getItems());
//        FilteredList<Ticket> filteredTData = new FilteredList<>(tData, e -> true);
//        // bind predicate based on datepicker choices
//        filteredTData.predicateProperty().bind(Bindings.createObjectBinding(() -> {
//        LocalDate minDate = beginDate.getValue();
//        LocalDate maxDate = endDate.getValue();
//
//        // get final values != null
//        final LocalDate finalMin = minDate == null ? LocalDate.MIN : minDate;
//        final LocalDate finalMax = maxDate == null ? LocalDate.MAX : maxDate;
//
//        // values for openDate need to be in the interval [finalMin, finalMax]
//        return ti -> !finalMin.isAfter(ti.getBeginDate()) && !finalMax.isBefore(ti.getBeginDate());
//        },
//        beginDate.valueProperty(),
//        endDate.valueProperty()));
//
//        empTicketTable.setItems(filteredTData);

        
        
//        ObservableList<Ticket> tData = FXCollections.observableList(empTicketTable.getItems());
//        FilteredList<Ticket> filteredTData =  new FilteredList<>(tData, d -> true);
        beginDate.setOnAction((ActionEvent event) -> {
            LocalDate date = beginDate.getValue();
            if (date != null && endDate.getValue() != null) {
                if (beginDate.getValue().isAfter(endDate.getValue())) {
                    beginDate.setValue(endDate.getValue());
                }
            }
        });
        
        endDate.setOnAction((ActionEvent event) -> {
            LocalDate date = beginDate.getValue();
            if (date != null && endDate.getValue() != null) {
                if (beginDate.getValue().isAfter(endDate.getValue())) {
                    beginDate.setValue(endDate.getValue());
                }
            }
        });
        
        applyDate.setOnAction((ActionEvent event) -> {
            try {
                rebuildEmpTicketTable(empTable.getSelectionModel().getSelectedItem().getEmpID(), beginDate, endDate);
            } catch (ClassNotFoundException | SQLException | ParseException ex) {
                Logger.getLogger(ManagerFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
    }
    
    private LocalDate convertDate(Date date) {
        df = DateTimeFormatter.ofPattern("yyyy-mm-dd");
        LocalDate ld = LocalDate.parse(df.format(date.toLocalDate()));
        return ld;
    }
    
    
    private void buildEmpTable() throws ClassNotFoundException {
        ObservableList<Employee> data = empTable.getItems();
        try{
            dbConnect = new DBConnection();
            conn = dbConnect.getConnection();
            String SQL = "select emp_id, fname, minit, lname from employee";            
            rs = conn.createStatement().executeQuery(SQL);  
            while(rs.next()){                   
                Employee emp = new Employee();
                emp.setEmpID(rs.getInt("emp_id"));
                emp.setFName(rs.getString("fname"));
                emp.setMInit(rs.getString("minit"));
                emp.setLName(rs.getString("lname"));
//                tk.setTTotalCost(rs.getBigDecimal("total_cost"));
                data.add(emp);                  
            }
            empTable.setItems(data);
        } catch(SQLException e){
              System.out.println("Error on Building Data");            
        }
    }
    
    private void rebuildEmpTicketTable(int i, DatePicker b, DatePicker e) 
            throws ClassNotFoundException, SQLException, ParseException {
        empTicketTable.getItems().clear();
        df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        ObservableList<Ticket> data = empTicketTable.getItems();
        try {
            dbConnect = new DBConnection();
            conn = dbConnect.getConnection();
            String sql;
            java.sql.Date d = java.sql.Date.valueOf(b.getValue().format(df));
            if (b.getValue() == null && e.getValue() == null) {
                buildEmpTicketTable(i);
            } else if(b.getValue() == null) {
                sql = "select * from emptickets where emp_id = ? "
                    + "and start_date <= ? order by ticket_id";
                cStmnt = conn.prepareCall(sql);
                cStmnt.setInt(1, i);
                java.sql.Date date = java.sql.Date.valueOf(e.getValue().format(df));
                cStmnt.setDate(2, date);
            } else if (e.getValue() == null) {
                sql = "select * from emptickets where emp_id = ? "
                    + "and start_date >= ? order by ticket_id";
                cStmnt = conn.prepareCall(sql);
                cStmnt.setInt(1, i);
                java.sql.Date date = java.sql.Date.valueOf(b.getValue().format(df));
                cStmnt.setDate(2, date);
            } else {
                sql = "select * from emptickets where emp_id = ? and start_date >= ? and start_date <= ? order by ticket_id";
                cStmnt = conn.prepareCall(sql);
                cStmnt.setInt(1, i);
                java.sql.Date date1 = java.sql.Date.valueOf(b.getValue().format(df));
                java.sql.Date date2 = java.sql.Date.valueOf(e.getValue().format(df));
                cStmnt.setDate(2, date1);
                cStmnt.setDate(3, date2);
            }
            rs = cStmnt.executeQuery();
            while(rs.next()) {
                    Ticket tk = new Ticket();
                    tk.setTNum(rs.getInt("ticket_id"));
                    tk.setTStatus(rs.getString("status"));
                    tk.setBeginDate(rs.getDate("start_date").toLocalDate());
                    tk.setEndDate(rs.getDate("end_date").toLocalDate());
                    data.add(tk);  
            }
            empTicketTable.setItems(data);
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.print(ex);
        }
    }
    
    private void buildEmpTicketTable(int i) throws ClassNotFoundException {
        empTicketTable.getItems().clear();
        ObservableList<Ticket> data = empTicketTable.getItems();
        try{
            dbConnect = new DBConnection();
            conn = dbConnect.getConnection();
            String sql = "select * from emptickets where emp_id = ?";
            cStmnt = conn.prepareCall(sql);
            cStmnt.setInt(1, i);
            rs = cStmnt.executeQuery(); 
            while(rs.next()){                   
                Ticket tk = new Ticket();
                tk.setTNum(rs.getInt("ticket_id"));
                tk.setTStatus(rs.getString("status"));
                tk.setBeginDate(rs.getDate("start_date").toLocalDate());
                tk.setEndDate(rs.getDate("end_date").toLocalDate());
                data.add(tk);                  
            }
            empTicketTable.setItems(data);
        } catch(SQLException e){
              System.out.println("Error on Building Data");            
        }
    }
    
    private void setEmpLabels(int i) throws ClassNotFoundException, SQLException {
        try {
            dbConnect = new DBConnection();
            conn = dbConnect.getConnection();
            String str1, str2, str3, str4, str5;
            String sql = "select * from empInfo where emp_id = ?";
            cStmnt = conn.prepareCall(sql);
            cStmnt.setInt(1, i);
            rs = cStmnt.executeQuery();
            if (!rs.next()) {
                sql = "select * from employee where emp_id = ?";
                cStmnt = conn.prepareCall(sql);
                cStmnt.setInt(1, i);
                rs = cStmnt.executeQuery();
                rs.next();
                str1 = rs.getString("fname");
                str2 = rs.getString("minit");
                str3 = rs.getString("lname");
                str4 = "0";
                str5 = "0.00";
            } else {
                str1 = rs.getString("fname");
                str2 = rs.getString("minit");
                str3 = rs.getString("lname");
                str4 = String.valueOf(rs.getInt("ticket_amount"));
                str5 = String.valueOf(rs.getBigDecimal("total_amount"));
            }
            if (str2 == null) {
                str2 = "";
            }
            empNameLabel.setText(str1 + " " + str2 + " " + str3);
            empNumOfTickets.setText(str4);
            empGeneratedAmount.setText(str5);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
