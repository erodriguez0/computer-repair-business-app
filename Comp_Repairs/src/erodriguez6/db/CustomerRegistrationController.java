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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author esteban
 */
public class CustomerRegistrationController extends CSWindowFXMLController implements Initializable {
    private DBConnection dbConnect;
    private Connection conn;
    private CallableStatement cStmnt;
    private Statement stmnt;
    private ResultSet rs;
    private ArrayList<TextField> tf, tf1;
    static protected int problem;
    private Optional<ButtonType> alert;
    
    @FXML CheckBox cb;
    @FXML TextField fname, minit, lname, caddress, ccity, cstate, czip, email, phone;
    @FXML TextField qaddress, qcity, qstate, qzip;
    @FXML TextArea descArea;
    @FXML Button submitBtn, cancelBtn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tf = new ArrayList<>();
        tf1 = new ArrayList<>();
        tf.add(fname);
        tf.add(lname);
        tf.add(caddress);
        tf.add(ccity);
        tf.add(cstate);
        tf.add(czip);
        tf.add(email);
        tf.add(phone);
        tf1.add(qaddress);
        tf1.add(qcity);
        tf1.add(qstate);
        tf1.add(qzip);
        descArea.setWrapText(true);
        cb.selectedProperty().addListener((obs, o, n)->{
            if(n == true) {
                qaddress.setDisable(true);
                qcity.setDisable(true);
                qstate.setDisable(true);
                qzip.setDisable(true);
            } else {
                qaddress.setDisable(false);
                qcity.setDisable(false);
                qstate.setDisable(false);
                qzip.setDisable(false);            
            }
        });

        cancelBtn.setOnAction((ActionEvent e) -> {
            cancelBtn.getScene().getWindow().hide();
        });
        
        submitBtn.setOnAction((ActionEvent e) -> {
//            System.out.print(isAlphaNum(fname.getText().trim()));
            int customer, computer;
            if (descArea.getText().trim().length() > 500) {
                alert = new Alert(Alert.AlertType.ERROR, "Description too long.").showAndWait();
                if(!alert.isPresent() || alert.get() == ButtonType.OK) {
                    return;
                }
            }
            for(int i = 0; i < tf.size(); i++) {
                if(tf.get(i).getText().trim().isEmpty()) {
                    alert = new Alert(Alert.AlertType.ERROR, "Missing customer information.").showAndWait();
                    if(!alert.isPresent() || alert.get() == ButtonType.OK) {
                        return;
                    }
                } else {
                    for (TextField tf11 : tf1) {
                        if (tf.get(i).getText().trim().isEmpty()) {
                            alert = new Alert(Alert.AlertType.ERROR, "Missing equipment information.").showAndWait();
                            if(!alert.isPresent() || alert.get() == ButtonType.OK) {
                                return;
                            }
                        }
                    }
                }
            }
            if(cb.isSelected()) {
                    if(validate(fname.getText().trim(), minit.getText().trim(), lname.getText().trim(),
                                    caddress.getText().trim(), ccity.getText().trim(), cstate.getText().trim(),
                                    czip.getText().trim(), email.getText().trim(), phone.getText().trim()) == true) {
                        try {
                            customer = registerCustomer(fname.getText().trim(), minit.getText().trim(), lname.getText().trim(),
                                    caddress.getText().trim(), ccity.getText().trim(), cstate.getText().trim(),
                                    czip.getText().trim(), email.getText().trim(), phone.getText().trim());
                            if (customer != -1) {
                                computer = registerEquipment(customer, caddress.getText().trim(), ccity.getText().trim(), cstate.getText().trim(),
                                        czip.getText().trim());
                                if (computer != -1) {
                                    problem = registerProblem(computer, descArea.getText().trim());
                                    if (problem != -1) {
                                        alert = new Alert(Alert.AlertType.INFORMATION, "Registered customer and equipment.").showAndWait();
                                        if(!alert.isPresent() || alert.get() == ButtonType.OK) {
                                            //new window
                                            submitBtn.getScene().getWindow().hide();
                                        }
                                    } else {
                                       alert = new Alert(Alert.AlertType.ERROR, "Error when adding problem.").showAndWait();
                                        if(!alert.isPresent() || alert.get() == ButtonType.OK) {
                                        } 
                                    }
                                } else {
                                    alert = new Alert(Alert.AlertType.ERROR, "Error when adding computer.").showAndWait();
                                    if(!alert.isPresent() || alert.get() == ButtonType.OK) {
                                    }
                                }
                            } else {
                                alert = new Alert(Alert.AlertType.ERROR, "Error when adding customer.").showAndWait();
                                if(!alert.isPresent() || alert.get() == ButtonType.OK) {
                                }
                            }
                        } catch (SQLException | ClassNotFoundException ex) {
                            System.err.print(ex);
                            alert = new Alert(Alert.AlertType.ERROR, "Invalid information.").showAndWait();
                            if(!alert.isPresent() || alert.get() == ButtonType.OK) {
                            }
                        }
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR, "Invalid information.").showAndWait();
                        if(!alert.isPresent() || alert.get() == ButtonType.OK) {
                        }
                    }
            } else {
                if(validate(fname.getText().trim(), minit.getText().trim(), lname.getText().trim(),
                                caddress.getText().trim(), ccity.getText().trim(), cstate.getText().trim(),
                                czip.getText().trim(), email.getText().trim(), phone.getText().trim()) == true) {
                    try {
                        customer = registerCustomer(fname.getText().trim(), minit.getText().trim(), lname.getText().trim(),
                                caddress.getText().trim(), ccity.getText().trim(), cstate.getText().trim(),
                                czip.getText().trim(), email.getText().trim(), phone.getText().trim());
                        if (customer != -1) {
                            computer = registerEquipment(customer, qaddress.getText().trim(), qcity.getText().trim(), qstate.getText().trim(),
                                    qzip.getText());
                            if (computer != -1) {
                                problem = registerProblem(computer, descArea.getText().trim());
                                if (problem != -1) {
                                    alert = new Alert(Alert.AlertType.INFORMATION, "Registered customer and equipment.").showAndWait();
                                    if(!alert.isPresent() || alert.get() == ButtonType.OK) {
                                        //new window
                                        submitBtn.getScene().getWindow().hide();
                                    }
                                } else {
                                    alert = new Alert(Alert.AlertType.ERROR, "Error when adding problem.").showAndWait();
                                    if(!alert.isPresent() || alert.get() == ButtonType.OK) {
                                    }
                                }
                            }
                        } else {
                            alert = new Alert(Alert.AlertType.ERROR, "Error when adding customer.").showAndWait();
                            if(!alert.isPresent() || alert.get() == ButtonType.OK) {
                            }
                        }
                    } catch (SQLException | ClassNotFoundException ex) {
                        alert = new Alert(Alert.AlertType.ERROR, "Invalid information.").showAndWait();
                        if(!alert.isPresent() || alert.get() == ButtonType.OK) {
                        }
                    }
                } else {
                    alert = new Alert(Alert.AlertType.ERROR, "Invalid information.").showAndWait();
                    if(!alert.isPresent() || alert.get() == ButtonType.OK) {
                    }
                }
            }
        });
    }    
    
    private int registerCustomer(String fname, String minit, String lname, String caddress,
            String ccity, String cstate, String czip, String email, String phone) throws SQLException, ClassNotFoundException {
        try {
            dbConnect = new DBConnection();
            conn = dbConnect.getConnection();
            String sql = "{call new_customer(?,?,?,?,?,?,?,?,?)}";
            cStmnt = conn.prepareCall(sql);
            cStmnt.setString(1, fname);
            cStmnt.setString(2, minit);
            cStmnt.setString(3, lname);
            cStmnt.setString(4, caddress);
            cStmnt.setString(5, ccity);
            cStmnt.setString(6, cstate);
            cStmnt.setString(7, czip);
            cStmnt.setString(8, phone);
            cStmnt.setString(9, email);
            cStmnt.executeQuery();
            stmnt = conn.createStatement();
            rs = stmnt.executeQuery("select last_value from customer_cust_id_seq");
            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex);
        }
        return -1;
    }
    
    private int registerEquipment(int cust_id, String qaddress, String qcity, String qstate, String qzip) {
        try {
            dbConnect = new DBConnection();
            conn = dbConnect.getConnection();
            String sql = "{call new_equipment(?,?,?,?,?)}";
            cStmnt = conn.prepareCall(sql);
            cStmnt.setInt(1, cust_id);
            cStmnt.setString(2, qaddress);
            cStmnt.setString(3, qcity);
            cStmnt.setString(4, qstate);
            cStmnt.setString(5, qzip);
            cStmnt.executeQuery();
            stmnt = conn.createStatement();
            rs = stmnt.executeQuery("select last_value from equipment_comp_id_seq");
            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex);
        }
        return -1;
    }
    
    private int registerProblem(int comp_id, String description) {
        try {
            dbConnect = new DBConnection();
            conn = dbConnect.getConnection();
            String sql = "{call new_problem(?,?)}";
            cStmnt = conn.prepareCall(sql);
            cStmnt.setInt(1, comp_id);
            cStmnt.setString(2, description);
            cStmnt.executeQuery();
            stmnt = conn.createStatement();
            rs = stmnt.executeQuery("select last_value from customer_problem_problem_id_seq");
            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex);
        }
        return -1;
    }
    
    private boolean validate(String fname, String minit, String lname, String caddress,
            String ccity, String cstate, String czip, String email, String phone) {
        if (!isAlphaNum(fname) || !isAlphaNum(minit) || !isAlphaNum(lname) || !isAlphaNum(caddress)
                || !isAlphaNum(ccity) || !isAlphaNum(cstate) || !isAlphaNum(czip) ||
                !isAlphaNum(email) || !isAlphaNum(phone) || minit.length() > 1 ||
                cstate.length() != 2 || czip.length() != 5) {
            return false;
        } else if (!email.contains("@") || !phone.contains("-")) {
            return false;
        } else {
            return true;
        }
    }
    
    private boolean validate(String qaddress, String qcity, String qstate, String qzip) {
        if (!isAlphaNum(qaddress) || !isAlphaNum(qcity) || !isAlphaNum(qstate) || 
                !isAlphaNum(qzip) || qstate.length() != 2 || qzip.length() != 5) {
            return false;
        } else {
            return true;
        }
    }
    
    private boolean isAlphaNum(String text) {
        return !text.matches("^.*[^a-zA-Z-0-9@. ].*$");
    }
    
    public int getProblem() {
        return problem;
    }
}
