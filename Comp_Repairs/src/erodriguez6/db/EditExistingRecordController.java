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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author esteban
 */
public class EditExistingRecordController extends CompleteRequestRecordsController implements Initializable {
    private int problemID, empID;
    DBConnection dbConnect;
    Connection connect;
    ResultSet r_s;
    CallableStatement c_Stmnt;
    Statement stmnt;
    ObservableList<Employee> emps, selEmps;
    static ArrayList<Employee> delEmps, exEmps;
    private Optional <ButtonType> info;
    
    @FXML TableView<Employee> selectEmp, selectedEmp;
    @FXML ComboBox combo;
    @FXML Button createBtn, cancelBtn;
    @FXML TextArea descArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.empID = super.getEmpID();
//        if(!delEmps.isEmpty()) {
//            delEmps.clear();
//        }
        emps = selectEmp.getItems();
        selEmps = selectedEmp.getItems();
//        delEmps.clear();
        delEmps = new ArrayList<>();
        exEmps = new ArrayList<>();
        descArea.setWrapText(true);
        problemID = super.getPID();
        selectedEmp.setPlaceholder(new Label("Select employees"));
        combo.setItems(FXCollections.observableArrayList("HARDWARE", "SOFTWARE", 
                "SOFTWARE / HARDWARE"));
        combo.setValue("SOFTWARE/HARDWARE");
        try {
            dbConnect = new DBConnection();
            connect = dbConnect.getConnection();
            String sql = "SELECT * FROM select_emp_table";
            c_Stmnt = connect.prepareCall(sql);
            r_s = c_Stmnt.executeQuery();
            while(r_s.next()) {
                Employee employee = new Employee();
                employee.setEmpID(r_s.getInt("emp_id"));
                employee.setFName(r_s.getString("fname") + " " + r_s.getString("lname"));
                if(r_s.getString("jobtitle").equalsIgnoreCase("Hardware Specialist")) {
                    employee.setJobTitle("HW");
                } else {
                    employee.setJobTitle("SW");
                }
                employee.setTicketCount(r_s.getInt("ticket_count"));
                emps.add(employee);
            }
            selectEmp.setItems(emps);
            
            sql = "SELECT * FROM work_on_emp_tickets where problem_id = ?";
            c_Stmnt = connect.prepareCall(sql);
            c_Stmnt.setInt(1, problemID);
            r_s = c_Stmnt.executeQuery();
            while(r_s.next()) {
                Employee employee = new Employee();
                employee.setEmpID(r_s.getInt("emp_id"));
                employee.setFName(r_s.getString("name"));
                if(r_s.getString("jobtitle").equalsIgnoreCase("Hardware Specialist")) {
                    employee.setJobTitle("HW");
                } else {
                    employee.setJobTitle("SW");
                }
//                tmpEmps.add(employee);
                selectedEmp.getItems().add(employee);
//                tmpEmps.add(employee);
            }
            for(int i = 0; i < selectedEmp.getItems().size(); i++) {
                exEmps.add(selectedEmp.getItems().get(i));
            }
            
            sql = "SELECT * FROM ticket where problem_id = ?";
            c_Stmnt = connect.prepareCall(sql);
            c_Stmnt.setInt(1, problemID);
            r_s = c_Stmnt.executeQuery();
            while(r_s.next()) {
                Ticket t = new Ticket();
                t.setTDetails(r_s.getString("description"));
                t.setTNum(r_s.getInt("ticket_id"));
                t.setType(r_s.getString("type"));
                if(t.getType().equalsIgnoreCase("SW")) {
                    combo.setValue("SOFTWARE");
                } else if(t.getType().equalsIgnoreCase("HW")) {
                    combo.setValue("HARDWARE");
                } else {
                    combo.setValue("SOFTWARE/HARDWARE");
                }
//                combo.setValue(t.getType());
                descArea.setText(t.getTDetails());
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Error loading employees");
        }
        
//        try {
//            dbConnect = new DBConnection();
//            connect = dbConnect.getConnection();
//        } catch (ClassNotFoundException | SQLException ex) {
//            System.err.print(ex);
//        }
        
        selectEmp.setOnMouseClicked( event -> {
            if( event.getClickCount() == 2 && !selectEmp.getSelectionModel().isEmpty() ) {
                Employee newEmp = selectEmp.getSelectionModel().getSelectedItem();
                Employee tmp = new Employee();
                tmp.setEmpID(newEmp.getEmpID());
                tmp.setFName(newEmp.getFName());
                tmp.setJobTitle(newEmp.getJobTitle());
                selectedEmp.getItems().add(tmp);
            }
        });
        
        selectedEmp.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent t) -> {
            
            if((t.getButton() == MouseButton.SECONDARY || t.getClickCount() == 2) && 
                    selectedEmp.getSelectionModel().getSelectedItem() != null) {
                delEmps.add(selectedEmp.getSelectionModel().getSelectedItem());
                selectedEmp.getItems().remove(selectedEmp.getSelectionModel().getSelectedItem());
            }
        });
        
        createBtn.setOnAction((ActionEvent event) -> {
            //Save the ticket type selection
            String t_type = combo.getSelectionModel().getSelectedItem().toString();
//            for(int i = 0; i < selectedEmp.getItems().size(); i++) {
//                delEmps.add(selectedEmp.getItems().get(i));
//            }
            info = new Alert(Alert.AlertType.CONFIRMATION, "Submit changes to ticket?").showAndWait();
            if(!info.isPresent() || info.get() == ButtonType.CANCEL) {
                
            } else if (info.get() == ButtonType.OK) {      
                //Check which ticket type was chosen and convert to ticket_type enum
                if (t_type == null) {
                    info = new Alert(Alert.AlertType.WARNING, 
                        "Missing ticket type, ticket not edited").showAndWait();
                    return;
                } else if(t_type.equalsIgnoreCase("HARDWARE")) {
                    t_type = "HW";
                } else if (t_type.equalsIgnoreCase("SOFTWARE")) {
                    t_type = "SW";
                } else {
                    t_type = "HW/SW";
                }
                
                if(!delEmps.isEmpty()) {
                    for(int i = 0; i < delEmps.size(); i++) {
                        try {
                            delWorkOn(delEmps.get(i).getEmpID(), this.getPID());

                        } catch (SQLException ex) {
                            Logger.getLogger(EditExistingRecordController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                if (descArea.getText().trim().isEmpty()) {
                    info = new Alert(Alert.AlertType.WARNING,
                        "Missing description, ticket not edited.").showAndWait();
                } else if (selectedEmp.getItems().size() < 1) {
                    info = new Alert(Alert.AlertType.WARNING,
                        "Missing assigned employee(s), ticket not created").showAndWait();
                } else {
                    //Create ticket and get the new ticket's ID
                    editTicket(this.getPID(), t_type, descArea.getText().trim());
//                    completeProblem(this.getPID());

                    //Assign the employee(s) that will work on the ticket
                    for(int i = 0; i < selectedEmp.getItems().size(); i++) {
                        if(exEmps.contains(selectedEmp.getItems().get(i))) {
                            
                        } else {
                            createWorkOn(selectedEmp.getItems().get(i).getEmpID(), problemID);
                        }
                    }
                    info = new Alert(Alert.AlertType.INFORMATION,
                    "Ticket edited successfully").showAndWait();
                    createBtn.getScene().getWindow().hide();
//                    super.getBP().getScene().getWindow().hide(); //Doesn't work
                }
            }
        });
        
        cancelBtn.setOnAction((ActionEvent event) -> {
            info = new Alert(Alert.AlertType.CONFIRMATION,
                    "Abort editing the ticket?").showAndWait();
            if(!info.isPresent() || info.get() == ButtonType.CANCEL) {
                
            } else {
//                    revertProblem(this.getPID());
                createBtn.getScene().getWindow().hide();
            }
        });
        
    }    
    
    private void editTicket(int pid, String type, String description) {
        try {
            dbConnect = new DBConnection();
            connect = dbConnect.getConnection();
            c_Stmnt = connect.prepareCall("{call edit_created_tickets(?, ?, ?)}");
            c_Stmnt.setInt(1, pid);
            c_Stmnt.setString(2, type);
            c_Stmnt.setString(3, description);
            c_Stmnt.executeQuery();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.print(ex);
        }
    }
    
    private void createWorkOn(int eid, int tid) {
        try {
            dbConnect = new DBConnection();
            connect = dbConnect.getConnection();
            c_Stmnt = connect.prepareCall("{call edit_work(?, ?)}");
            c_Stmnt.setInt(1, eid);
            c_Stmnt.setInt(2, tid);
            c_Stmnt.executeQuery();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.print(ex);
        }
    }
    
    private void delWorkOn(int eid, int pid) throws SQLException {
        try {
            dbConnect = new DBConnection();
            c_Stmnt = connect.prepareCall("{call del_work_on(?, ?)}");
            c_Stmnt.setInt(1, eid);
            c_Stmnt.setInt(2, pid);
            c_Stmnt.executeQuery();
        } catch (SQLException ex) {
            System.err.print(ex);
        }
    }
}
