/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erodriguez6.db;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author esteban
 */
public class CSWindowFXMLController extends LoginFXMLController implements Initializable {
    //Private 
    private DBConnection dbConnect;
    private Connection conn;
    private CallableStatement cStmnt;
    private ResultSet rs;
    private ObservableList<Problem> data;
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static protected int pID, emp_id;
    @FXML Button nbutton;
    Stage dialogStage;
    //FXML Elements
    @FXML TableView<Problem> CSTable;
    @FXML DatePicker startDate, endDate;
    @FXML Button applyBtn, resetBtn, refreshBtn, reportBtn, loadBtn, historyBtn, newCustomer;
    @FXML TextField searchField;
    private HashMap parametersMap;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emp_id = super.getEmpID();
        CSTable.setPlaceholder(new Label("Load New Customer Requests"));
        startDate.setPromptText("MM/DD/YYYY");
        endDate.setPromptText("MM/DD/YYYY");
        startDate.setValue(LocalDate.now().minusMonths(1));
        endDate.setValue(LocalDate.now());
        data = FXCollections.observableList(CSTable.getItems());
        try {
            buildCSTable(startDate, endDate);
        } catch (ClassNotFoundException | SQLException | ParseException ex) {
            Logger.getLogger(CSWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        //Listener for problem search bar
        FilteredList<Problem> filteredData = new FilteredList<>(data, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Problem -> {
                // If filter text is empty, display all problems
                if (newValue == null || newValue.isEmpty()) { return true; }
                // Compare first name and last name field in your object with filter
                String lowerCaseFilter = newValue.toLowerCase();
                // Filter matches first name
                if (String.valueOf(Problem.getFname()+ " " + Problem.getLname()).toLowerCase().contains(lowerCaseFilter)) { 
                    return true;
                // Filter matches last name
                } else if (String.valueOf(Problem.getUsername()).toLowerCase().contains(lowerCaseFilter)) { 
                    return true;
                // Filter matches id
                } else if (Integer.toString(Problem.getProblemID()).contains(newValue)) { 
                    return true; 
                }
                // Does not match any
                return false; 
            });
        });
        //Wrap the FilteredList in a SortedList
        SortedList<Problem> sortedData = new SortedList<>(filteredData);
        //Bind the SortedList comparator to the TableView comparator
        sortedData.comparatorProperty().bind(CSTable.comparatorProperty());
        //Add sorted (and filtered) data to the table
        CSTable.setItems(sortedData);
        
//        nbutton.setOnAction((ActionEvent event) -> {
//            nbutton.getScene().getWindow().hide();
//        });

        loadBtn.setOnAction((ActionEvent event) -> {
            JasperReport ra;
            try {
                ra = JasperCompileManager.compileReport(new File("").getAbsolutePath() 
                        + "/src/erodriguez6/db/CSDept_stats_report_1.jrxml");
            if (parametersMap == null) {
                parametersMap = new HashMap();
            } else {
                parametersMap.clear();
            }
            
            //Set dates when not specified in datepicker
            if (startDate.getValue().toString().isEmpty() && endDate.getValue().toString().isEmpty()) {
                startDate.setValue(LocalDate.of(startDate.getValue().getYear(), Calendar.JANUARY, 1));
                endDate.setValue(LocalDate.now());
            } else if (startDate.getValue().toString().isEmpty()) {
                startDate.setValue(LocalDate.of(startDate.getValue().getYear(), Calendar.JANUARY, 1));
            } else if (endDate.getValue().toString().isEmpty()) {
                endDate.setValue(LocalDate.now());
            }
            
            Date sDate = java.sql.Date.valueOf(startDate.getValue().format(df));
            Date eDate = java.sql.Date.valueOf(endDate.getValue().format(df));

            parametersMap.put("emp_id", CSWindowFXMLController.emp_id);
            parametersMap.put("start_date", sDate);
            parametersMap.put("end_date", eDate);
            JasperPrint jrPrintable;
            jrPrintable = JasperFillManager.fillReport(ra, parametersMap, dbConnect.getConnection());
//            JasperExportManager.exportReportToPdfFile(jrPrintable, ra + "CS_Report.pdf");
            JasperViewer.viewReport(jrPrintable, false);
            } catch (JRException | ClassNotFoundException | SQLException ex) {
                Logger.getLogger(CSWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        applyBtn.setOnAction((ActionEvent event) -> {
            try {
                buildCSTable(startDate, endDate);
                searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(Problem -> {
                        // If filter text is empty, display all problems
                        if (newValue == null || newValue.isEmpty()) { return true; }
                        // Compare first name and last name field in your object with filter
                        String lowerCaseFilter = newValue.toLowerCase();
                        // Filter matches first name
                        if (String.valueOf(Problem.getFname()+ " " + Problem.getLname()).toLowerCase().contains(lowerCaseFilter)) { 
                            return true;
                        // Filter matches last name
                        } else if (String.valueOf(Problem.getUsername()).toLowerCase().contains(lowerCaseFilter)) { 
                            return true;
                        // Filter matches id
                        } else if (Integer.toString(Problem.getProblemID()).contains(newValue)) { 
                            return true; 
                        }
                        // Does not match any
                        return false; 
                    });
                });
                //Bind the SortedList comparator to the TableView comparator
                sortedData.comparatorProperty().bind(CSTable.comparatorProperty());
                //Add sorted (and filtered) data to the table
                CSTable.setItems(sortedData);
            } catch (ClassNotFoundException | SQLException | ParseException ex) {
                Logger.getLogger(CSWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        resetBtn.setOnAction((ActionEvent event) -> {
            searchField.clear();
            startDate.setValue(LocalDate.now().minusMonths(1));
            endDate.setValue(LocalDate.now());
            buildCSTable();
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Problem -> {
                    // If filter text is empty, display all problems
                    if (newValue == null || newValue.isEmpty()) { return true; }
                    // Compare first name and last name field in your object with filter
                    String lowerCaseFilter = newValue.toLowerCase();
                    // Filter matches first name
                    if (String.valueOf(Problem.getFname()+ " " + Problem.getLname()).toLowerCase().contains(lowerCaseFilter)) { 
                        return true;
                    // Filter matches last name
                    } else if (String.valueOf(Problem.getUsername()).toLowerCase().contains(lowerCaseFilter)) { 
                        return true;
                    // Filter matches id
                    } else if (Integer.toString(Problem.getProblemID()).contains(newValue)) { 
                        return true; 
                    }
                    // Does not match any
                    return false; 
                });
            });
            //Bind the SortedList comparator to the TableView comparator
            sortedData.comparatorProperty().bind(CSTable.comparatorProperty());
            //Add sorted (and filtered) data to the table
            CSTable.setItems(sortedData);
        });
        
        refreshBtn.setOnAction((ActionEvent event) -> {
            try {
                buildCSTable(startDate, endDate);
            } catch (ClassNotFoundException | SQLException | ParseException ex) {
                Logger.getLogger(CSWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Problem -> {
                    // If filter text is empty, display all problems
                    if (newValue == null || newValue.isEmpty()) { return true; }
                    // Compare first name and last name field in your object with filter
                    String lowerCaseFilter = newValue.toLowerCase();
                    // Filter matches first name
                    if (String.valueOf(Problem.getFname()+ " " + Problem.getLname()).toLowerCase().contains(lowerCaseFilter)) { 
                        return true;
                    // Filter matches last name
                    } else if (String.valueOf(Problem.getUsername()).toLowerCase().contains(lowerCaseFilter)) { 
                        return true;
                    // Filter matches id
                    } else if (Integer.toString(Problem.getProblemID()).contains(newValue)) { 
                        return true; 
                    }
                    // Does not match any
                    return false; 
                });
            });
            //Bind the SortedList comparator to the TableView comparator
            sortedData.comparatorProperty().bind(CSTable.comparatorProperty());
            //Add sorted (and filtered) data to the table
            CSTable.setItems(sortedData);
        });
        
        reportBtn.setOnAction((ActionEvent event) -> {
            JasperReport ra;
            try {
                ra = JasperCompileManager.compileReport(new File("").getAbsolutePath() 
                        + "/src/erodriguez6/db/CSDeptReport_1.jrxml");
            if (parametersMap == null) {
                parametersMap = new HashMap();
            } else {
                parametersMap.clear();
            }
            
            //Set dates when not specified in datepicker
            if (startDate.getValue().toString().isEmpty() && endDate.getValue().toString().isEmpty()) {
                startDate.setValue(LocalDate.of(startDate.getValue().getYear(), Calendar.JANUARY, 1));
                endDate.setValue(LocalDate.now());
            } else if (startDate.getValue().toString().isEmpty()) {
                startDate.setValue(LocalDate.of(startDate.getValue().getYear(), Calendar.JANUARY, 1));
            } else if (endDate.getValue().toString().isEmpty()) {
                endDate.setValue(LocalDate.now());
            }
            
            Date sDate = java.sql.Date.valueOf(startDate.getValue().format(df));
            Date eDate = java.sql.Date.valueOf(endDate.getValue().format(df));

            parametersMap.put("emp_id", CSWindowFXMLController.emp_id);
            parametersMap.put("start_date", sDate);
            parametersMap.put("end_date", eDate);
            JasperPrint jrPrintable;
            jrPrintable = JasperFillManager.fillReport(ra, parametersMap, dbConnect.getConnection());
//            JasperExportManager.exportReportToPdfFile(jrPrintable, ra + "CS_Report.pdf");
            JasperViewer.viewReport(jrPrintable, false);
            } catch (JRException | ClassNotFoundException | SQLException ex) {
                Logger.getLogger(CSWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        newCustomer.setOnAction((ActionEvent e) -> {
            final Stage dialog = new Stage();
            dialog.setOnHiding((WindowEvent event) -> {
                Platform.runLater(() -> {
//                    dialog.getScene().getWindow().hide();
                        try {
                            buildCSTable(startDate, endDate);
                        } catch (ClassNotFoundException | SQLException | ParseException ex) {
                            Logger.getLogger(CSWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                });
                    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(Problem -> {
                        // If filter text is empty, display all problems
                        if (newValue == null || newValue.isEmpty()) { return true; }
                        // Compare first name and last name field in your object with filter
                        String lowerCaseFilter = newValue.toLowerCase();
                        // Filter matches first name
                        if (String.valueOf(Problem.getFname()+ " " + Problem.getLname()).toLowerCase().contains(lowerCaseFilter)) { 
                            return true;
                        // Filter matches last name
                        } else if (String.valueOf(Problem.getUsername()).toLowerCase().contains(lowerCaseFilter)) { 
                            return true;
                        // Filter matches id
                        } else if (Integer.toString(Problem.getProblemID()).contains(newValue)) { 
                            return true; 
                        }
                        // Does not match any
                        return false; 
                    });
                });
                //Bind the SortedList comparator to the TableView comparator
                sortedData.comparatorProperty().bind(CSTable.comparatorProperty());
                //Add sorted (and filtered) data to the table
                CSTable.setItems(sortedData);
            });
            dialog.setResizable(false);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner((Stage) CSTable.getScene().getWindow());
            dialog.setTitle("Customer Registration Form");
            BorderPane historyPane;
            try {
                historyPane = FXMLLoader.load(getClass().getResource("CustomerRegistration.fxml"));
                Scene dialogScene = new Scene(historyPane);
                dialog.setScene(dialogScene);
                dialog.show();
            } catch (IOException ex) {
                Logger.getLogger(CSWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        CSTable.setOnMouseClicked( event -> {
            if( event.getClickCount() == 2 && !CSTable.getSelectionModel().isEmpty() ) {
                //Set problem_id of double clicked tableview row
                pID = CSTable.getSelectionModel().getSelectedItem().getProblemID();
//                problem_update(pID, emp_id);
                final Stage dialog = new Stage();
//                dialog.setOnCloseRequest(e->e.consume());
                dialog.setOnHiding((WindowEvent e) -> {
                    Platform.runLater(() -> {
                        dialog.getScene().getWindow().hide();
                        try {
                            buildCSTable(startDate, endDate);
                        } catch (ClassNotFoundException | SQLException | ParseException ex) {
                            Logger.getLogger(CSWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    
                    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(Problem -> {
                        // If filter text is empty, display all problems
                        if (newValue == null || newValue.isEmpty()) { return true; }
                        // Compare first name and last name field in your object with filter
                        String lowerCaseFilter = newValue.toLowerCase();
                        // Filter matches first name
                        if (String.valueOf(Problem.getFname()+ " " + Problem.getLname()).toLowerCase().contains(lowerCaseFilter)) { 
                            return true;
                        // Filter matches last name
                        } else if (String.valueOf(Problem.getUsername()).toLowerCase().contains(lowerCaseFilter)) { 
                            return true;
                        // Filter matches id
                        } else if (Integer.toString(Problem.getProblemID()).contains(newValue)) { 
                            return true; 
                        }
                        // Does not match any
                        return false; 
                    });
                });
                //Bind the SortedList comparator to the TableView comparator
                sortedData.comparatorProperty().bind(CSTable.comparatorProperty());
                //Add sorted (and filtered) data to the table
                CSTable.setItems(sortedData);
                });
                dialog.setResizable(false);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner((Stage) CSTable.getScene().getWindow());
                String title = String.format("Problem Overview: %d", pID);
                dialog.setTitle(title);
                BorderPane problemPane;
                try {
                    problemPane = FXMLLoader.load(getClass().getResource("ProblemFormFXML.fxml"));
                    Scene dialogScene = new Scene(problemPane);
                    dialog.setScene(dialogScene);
                    dialog.show();
                } catch (IOException ex) {
                    Logger.getLogger(CSWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        historyBtn.setOnAction((ActionEvent event) -> {
            final Stage dialog = new Stage();
            dialog.setOnHiding((WindowEvent e) -> {
                Platform.runLater(() -> {
//                    dialog.getScene().getWindow().hide();
                        try {
                            buildCSTable(startDate, endDate);
                        } catch (ClassNotFoundException | SQLException | ParseException ex) {
                            Logger.getLogger(CSWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                });
                    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(Problem -> {
                        // If filter text is empty, display all problems
                        if (newValue == null || newValue.isEmpty()) { return true; }
                        // Compare first name and last name field in your object with filter
                        String lowerCaseFilter = newValue.toLowerCase();
                        // Filter matches first name
                        if (String.valueOf(Problem.getFname()+ " " + Problem.getLname()).toLowerCase().contains(lowerCaseFilter)) { 
                            return true;
                        // Filter matches last name
                        } else if (String.valueOf(Problem.getUsername()).toLowerCase().contains(lowerCaseFilter)) { 
                            return true;
                        // Filter matches id
                        } else if (Integer.toString(Problem.getProblemID()).contains(newValue)) { 
                            return true; 
                        }
                        // Does not match any
                        return false; 
                    });
                });
                //Bind the SortedList comparator to the TableView comparator
                sortedData.comparatorProperty().bind(CSTable.comparatorProperty());
                //Add sorted (and filtered) data to the table
                CSTable.setItems(sortedData);
            });
            dialog.setResizable(false);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner((Stage) CSTable.getScene().getWindow());
            dialog.setTitle("Complete Requests History");
            BorderPane historyPane;
            try {
                historyPane = FXMLLoader.load(getClass().getResource("CompleteRequestRecords.fxml"));
                Scene dialogScene = new Scene(historyPane);
                dialog.setScene(dialogScene);
                dialog.show();
            } catch (IOException ex) {
                Logger.getLogger(CSWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }    
    
    private void buildCSTable() {
        try {
            dbConnect = new DBConnection();
            conn = dbConnect.getConnection();
            data.clear();
            String sql = "SELECT * FROM csinfotable";
            cStmnt = conn.prepareCall(sql);
            rs = cStmnt.executeQuery();
            while(rs.next()) {
                Problem problem = new Problem();
                problem.setProblemID(rs.getInt("problem_id"));
                String str = rs.getString("fname") + " " + rs.getString("lname");
                problem.setFname(str);
//                problem.setLname(rs.getString("lname"));
                problem.setUsername(rs.getString("email"));
                problem.setSubmitDate(rs.getDate("submit_date").toLocalDate());
//                if(rs.getDate("review_date") == null) {
//                    
//                } else {
//                    problem.setExamineDate(rs.getDate("review_date").toLocalDate());
//                }
                problem.setStatus(rs.getString("status"));
                data.add(problem);
            }
            CSTable.setItems(data);
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex);
        }
    }
    
    private void buildCSTable(DatePicker b, DatePicker e) 
            throws ClassNotFoundException, SQLException, ParseException {
        try {
            dbConnect = new DBConnection();
            conn = dbConnect.getConnection();
            data.clear();
            String sql;
            if (b.getValue() == null && e.getValue() == null) {
                buildCSTable();
                return;
            } else if(b.getValue() == null) {
                sql = "select * from csinfotable where submit_date <= ? order by submit_date";
                cStmnt = conn.prepareCall(sql);
                java.sql.Date date = java.sql.Date.valueOf(e.getValue().format(df));
                cStmnt.setDate(1, date);
            } else if (e.getValue() == null) {
                sql = "select * from csinfotable where submit_date >= ? order by submit_date";
                cStmnt = conn.prepareCall(sql);
                java.sql.Date date = java.sql.Date.valueOf(b.getValue().format(df));
                cStmnt.setDate(1, date);
            } else {
                sql = "select * from csinfotable where submit_date >= ? and submit_date <= ? order by submit_date";
                cStmnt = conn.prepareCall(sql);
                java.sql.Date date1 = java.sql.Date.valueOf(b.getValue().format(df));
                java.sql.Date date2 = java.sql.Date.valueOf(e.getValue().format(df));
                cStmnt.setDate(1, date1);
                cStmnt.setDate(2, date2);
            }
            rs = cStmnt.executeQuery();
            while(rs.next()) {
                Problem problem = new Problem();
                problem.setProblemID(rs.getInt("problem_id"));
                String str = rs.getString("fname") + " " + rs.getString("lname");
                problem.setFname(str);
//                problem.setLname(rs.getString("lname"));
                problem.setUsername(rs.getString("email"));
                problem.setSubmitDate(rs.getDate("submit_date").toLocalDate());
//                problem.setExamineDate(rs.getDate("review_date").toLocalDate());
                problem.setStatus(rs.getString("status"));
                data.add(problem);
            }
            CSTable.setItems(data);
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.print(ex);
        }
    }
    
    protected int getPID() {
        return pID;
    }
    
    private void revertToOriginalProblem(int pid) throws ClassNotFoundException {
        try {
            dbConnect = new DBConnection();
            conn = dbConnect.getConnection();
            cStmnt = conn.prepareCall("{call revert_original_problem(?)}");
            cStmnt.setInt(1, pid);
            cStmnt.executeQuery();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
