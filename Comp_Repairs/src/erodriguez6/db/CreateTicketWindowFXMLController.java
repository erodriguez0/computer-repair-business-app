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
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author esteban
 */
public class CreateTicketWindowFXMLController extends ProblemFormFXMLController implements Initializable {
    private int problemID, empID;
    DBConnection dbConnect;
    Connection connect;
    ResultSet r_s;
    CallableStatement c_Stmnt;
    Statement stmnt;
    ObservableList<Employee> emps, selEmps;
    private Optional <ButtonType> info;
    
    @FXML TableView<Employee> selectEmp, selectedEmp;
    @FXML ComboBox combo;
    @FXML Button createBtn, cancelBtn;
    @FXML TextArea descArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.empID = super.getEmpID();
        emps = selectEmp.getItems();
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
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Error loading employees");
        }
        
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
                selectedEmp.getItems().remove(selectedEmp.getSelectionModel().getSelectedItem());
            }
        });
        
        createBtn.setOnAction((ActionEvent event) -> {
            //Save the ticket type selection
            String t_type;
            int tid;
            
            t_type = combo.getSelectionModel().getSelectedItem().toString();
            
            info = new Alert(Alert.AlertType.CONFIRMATION, "Create ticket and assign the selected employee(s)?").showAndWait();
            if(!info.isPresent() || info.get() == ButtonType.CANCEL) {
            } else if (info.get() == ButtonType.OK) {      
                //Check which ticket type was chosen and convert to ticket_type enum
                if (t_type == null) {
                    info = new Alert(Alert.AlertType.WARNING, 
                        "Missing ticket type, ticket not created").showAndWait();
                    return;
                } else if(t_type.equalsIgnoreCase("HARDWARE")) {
                    t_type = "HW";
                } else if (t_type.equalsIgnoreCase("SOFTWARE")) {
                    t_type = "SW";
                } else {
                    t_type = "HW/SW";
                }
                
                if (descArea.getText().trim().isEmpty()) {
                    info = new Alert(Alert.AlertType.WARNING,
                        "Missing description, ticket not created.").showAndWait();
                } else if (selectedEmp.getItems().size() < 1) {
                    info = new Alert(Alert.AlertType.WARNING,
                        "Missing assigned employee(s), ticket not created").showAndWait();
                } else {
                    //Create ticket and get the new ticket's ID
                    tid = createTicket(this.getPID(), t_type, descArea.getText().trim());
                    completeProblem(this.getPID());

                    //Assign the employee(s) that will work on the ticket
                    for(int i = 0; i < selectedEmp.getItems().size(); i++) {
                        createWorkOn(selectedEmp.getItems().get(i).getEmpID(), tid);
                    }
                info = new Alert(Alert.AlertType.INFORMATION,
                    "Ticket created and employee(s) assigned.").showAndWait();
                    createBtn.getScene().getWindow().hide();
//                    super.getBP().getScene().getWindow().hide(); //Doesn't work
                }
            }
        });
        
        cancelBtn.setOnAction((ActionEvent event) -> {
            info = new Alert(Alert.AlertType.CONFIRMATION,
                    "Abort creating the ticket?").showAndWait();
            if(!info.isPresent() || info.get() == ButtonType.CANCEL) {
                
            } else {
                try {
                    revertProblem(problemID);
                    createBtn.getScene().getWindow().hide();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(CreateTicketWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
    }    
    
    private int createTicket(int pid, String type, String desc) {
        try {
            dbConnect = new DBConnection();
            connect = dbConnect.getConnection();
            c_Stmnt = connect.prepareCall("{call crt_ticket(?, ?, ?)}");
            c_Stmnt.setInt(1, pid);
            c_Stmnt.setString(2, type);
            c_Stmnt.setString(3, desc);
            c_Stmnt.executeQuery();
            stmnt = connect.createStatement();
            r_s = stmnt.executeQuery("select last_value from ticket_ticket_id_seq");
            if(r_s.next()) {
                return r_s.getInt(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex);
        }
        return -1;
    }
    
    private void createWorkOn(int eid, int tid) {
        try {
            dbConnect = new DBConnection();
            connect = dbConnect.getConnection();
            c_Stmnt = connect.prepareCall("{call crt_work(?, ?)}");
            c_Stmnt.setInt(1, eid);
            c_Stmnt.setInt(2, tid);
            c_Stmnt.executeQuery();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.print(ex);
        }
    }
    
    private void revertProblem(int pid) throws ClassNotFoundException {
        try {
            dbConnect = new DBConnection();
            connect = dbConnect.getConnection();
            c_Stmnt = connect.prepareCall("{call revert_problem(?)}");
            c_Stmnt.setInt(1, pid);
            c_Stmnt.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(CreateTicketWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void completeProblem(int pid) {
        try {
            dbConnect = new DBConnection();
            connect = dbConnect.getConnection();
            c_Stmnt = connect.prepareCall("{call complete_problem(?)}");
            c_Stmnt.setInt(1, pid);
            c_Stmnt.executeQuery();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex);
        }
    }
}
