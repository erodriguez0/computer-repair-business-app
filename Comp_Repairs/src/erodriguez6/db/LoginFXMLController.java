/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erodriguez6.db;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author esteban
 */
public class LoginFXMLController implements Initializable {
    private DBConnection dbConnect;
    private Connection conn;
    private CallableStatement cStmnt;
    private ResultSet rs;
    static protected int emp;
    
    @FXML Button loginBtn;
    @FXML TextField usrField;
    @FXML PasswordField pwdField;
    @FXML Label errLabel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginBtn.setOnAction((ActionEvent e) -> {
            dbConnect = new DBConnection();
            String username = usrField.getText().trim();
            String password = pwdField.getText().trim();
            String sql = "SELECT * FROM super_emp WHERE username = ? AND password = ?";
            if(username.isEmpty() || password.isEmpty()) {
                errLabel.setVisible(true);
            } else {
                try {
                    conn = dbConnect.getConnection();
                    cStmnt = conn.prepareCall(sql);
                    cStmnt.setString(1, username);
                    cStmnt.setString(2, password);
                    rs = cStmnt.executeQuery();
                    if (!rs.next()) {
                        errLabel.setVisible(true);
                    } else {
                        emp = rs.getInt("emp_id");
//                        int i = rs.getInt("super_id");
//                        if (!rs.wasNull() || i != 0) {
//                            Stage stage;
//                            Parent root;
//                            stage = (Stage) ((Button)(e.getSource())).getScene().getWindow();
//                            root = FXMLLoader.load(getClass().getResource("ManagerWindowFXML.fxml"));
//                            Scene scene = new Scene(root);
//                            stage.setScene(scene);
//                            stage.show();
//                            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
//                            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
//                            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
//                        } else 
                        if (rs.getString("jobtitle").equalsIgnoreCase("Customer Support")) {
                            Stage stage;
                            Parent root;
                            stage = (Stage) ((Button)(e.getSource())).getScene().getWindow();
                            root = FXMLLoader.load(getClass().getResource("CSWindowFXML.fxml"));
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                        } else {
                            errLabel.setVisible(true);
                        }
                    }
                } catch (SQLException | IOException | ClassNotFoundException ex) {
                    Logger.getLogger(LoginFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        usrField.textProperty().addListener((ObservableValue<? extends String> 
                observable, String oldValue, String newValue) -> {
            errLabel.setVisible(false);
        });
        
        pwdField.textProperty().addListener((ObservableValue<? extends String> 
                observable, String oldValue, String newValue) -> {
            errLabel.setVisible(false);
        });
    } 
    
    protected int getEmpID() {
        return LoginFXMLController.emp;
    }
}