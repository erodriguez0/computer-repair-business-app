/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erodriguez6.db;

import static erodriguez6.db.CSWindowFXMLController.pID;
import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author esteban
 */
public class ProblemLookUpController extends CompleteRequestRecordsController implements Initializable {
    private int problemID;
    private int empID;
    static Problem problem;
    DBConnection dbConn;
    Connection conn;
    ResultSet rs;
    CallableStatement cStmnt;
    private Optional <ButtonType> info;
    static protected BorderPane bp;

    @FXML Label nameLabel2, usernameLabel2, rdateLabel2, compIDLabel2;
    @FXML TextArea descTA;
    @FXML Button acceptBtn;
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        problemID = super.getPID();
        empID = super.getEmpID();
        System.out.println(problemID);
        problem = new Problem();
        descTA.setWrapText(true);
        try {
            dbConn = new DBConnection();
            conn = dbConn.getConnection();
            String sql = "SELECT * FROM probleminfo WHERE problem_id = ?";
            cStmnt = conn.prepareCall(sql);
            cStmnt.setInt(1, problemID);
            rs = cStmnt.executeQuery();
            while(rs.next()) {
                problem.setProblemID(rs.getInt("problem_id"));
                problem.setCompID(rs.getInt("comp_id"));
                problem.setFname(rs.getString("fname"));
                problem.setLname(rs.getString("lname"));
                problem.setUsername(rs.getString("email"));
                problem.setSubmitDate(rs.getDate("submit_date").toLocalDate());
                if (rs.getDate("review_date") == null) {
                    
                } else {
                    problem.setExamineDate(rs.getDate("review_date").toLocalDate());
                }
                problem.setDescription(rs.getString("description"));
            }
            System.out.print(problem);
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex);
        }
       
        nameLabel2.setText(problem.getFname().toUpperCase() + " " + problem.getLname().toUpperCase());
        usernameLabel2.setText(problem.getUsername().toUpperCase());
        rdateLabel2.setText(problem.getSubmitDate().toString());
        compIDLabel2.setText(String.valueOf(problem.getCompID()));
        descTA.setText(problem.getDescription());
        
        acceptBtn.setOnAction((ActionEvent event) -> {
            acceptBtn.getScene().getWindow().hide();
//            final Stage dialog = new Stage();
//            dialog.setResizable(false);
//            dialog.setOnCloseRequest((WindowEvent e) -> {
//                Platform.runLater(() -> {
//                    Optional<ButtonType> window = new Alert(Alert.AlertType.CONFIRMATION,
//                        "Do you wish to cancel creating the ticket?").showAndWait();
//                    if (!window.isPresent() || window.get() == ButtonType.CANCEL) {
//                        
//                    } else {
//                        try {
//                            revertToOriginalProblem(this.getPID());
//                            Optional<ButtonType> inf = new Alert(Alert.AlertType.INFORMATION,
//                        "Ticket reverted to original state.").showAndWait();
//                            dialog.getScene().getWindow().hide();
//                            acceptBtn.getScene().getWindow().hide();
//                        } catch (ClassNotFoundException ex) {
//                            Logger.getLogger(ProblemFormFXMLController.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                });
//            });
//            
//            dialog.setOnHiding((WindowEvent e) -> {
//                Platform.runLater(() -> {
//                    dialog.getScene().getWindow().hide();
//                    acceptBtn.getScene().getWindow().hide();
//                });
//            });
//            dialog.initModality(Modality.APPLICATION_MODAL);
//            dialog.initOwner((Stage) acceptBtn.getScene().getWindow());
//            String title = String.format("Problem Overview: %d", pID);
//            dialog.setTitle(title);
//            problem_update(pID, empID);
//            try {
//                bp = FXMLLoader.load(getClass().getResource("CreateTicketWindowFXML.fxml"));
//                Scene dialogScene = new Scene(bp);
//                dialog.setScene(dialogScene);
//                dialog.show();
//            } catch (IOException ex) {
//                Logger.getLogger(CSWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
//            }
        });
//        
//        declineBtn.setOnAction((ActionEvent event) -> {
//            Optional<ButtonType> warn = new Alert(Alert.AlertType.CONFIRMATION,
//                "Are you sure you want to decline the customer's request?").showAndWait();
//            if(!warn.isPresent() || warn.get() == ButtonType.CANCEL) {
//                
//            } else if (warn.get() == ButtonType.OK) {
//                try {
//                    declineProblem(problemID, empID);
//                    info = new Alert(Alert.AlertType.INFORMATION,
//                        "Customer's request declined.").showAndWait();
//                    declineBtn.getScene().getWindow().hide();
//                    
//                } catch (ClassNotFoundException | SQLException ex) {
//                    System.err.print(ex);
//                    info = new Alert(Alert.AlertType.WARNING,
//                        "Request was not declined, error encountered.").showAndWait();
//                }
//            }
//        });
    }   
    
    private void declineProblem(int pid, int eid) throws ClassNotFoundException, SQLException {
        dbConn = new DBConnection();
        conn = dbConn.getConnection();
        cStmnt = conn.prepareCall("{call decline_problem(?)}");
        cStmnt.setInt(1, pid);
        cStmnt.setInt(2, eid);
        cStmnt.executeQuery();
    }
    
    private void revertToOriginalProblem(int pid) throws ClassNotFoundException {
        try {
            dbConn = new DBConnection();
            conn = dbConn.getConnection();
            cStmnt = conn.prepareCall("{call revert_original_problem(?)}");
            cStmnt.setInt(1, pid);
            cStmnt.executeQuery();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    
    public BorderPane getBP() {
        return ProblemFormFXMLController.bp;
    }
    
    protected int getPID() {
        return pID;
    }
    
    private void problem_update(int pid, int eid) {
        try {
            dbConn = new DBConnection();
            conn = dbConn.getConnection();
            cStmnt = conn.prepareCall("{call problem_update(?, ?)}");
            cStmnt.setInt(1, pid);
            cStmnt.setInt(2, eid);
            cStmnt.executeQuery();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex);
        }
    }
}
