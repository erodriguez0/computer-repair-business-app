/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erodriguez6.db;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author esteban
 */
public class CompRepairGUI extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Computer Repair GUI");
        
        GridPane grid = FXMLLoader.load(getClass().getResource("loginFXML.fxml"));

//        BorderPane grid = FXMLLoader.load(getClass().getResource("ManagerWindowFXML.fxml"));

//        BorderPane grid = FXMLLoader.load(getClass().getResource("CSWindowFXML.fxml"));

//        BorderPane grid = FXMLLoader.load(getClass().getResource("CompleteRequestRecords.fxml"));

//        BorderPane grid = FXMLLoader.load(getClass().getResource("CreateTicketWindowFXML.fxml"));

//        BorderPane grid = FXMLLoader.load(getClass().getResource("EditExistingRecord.fxml"));

//        BorderPane grid = FXMLLoader.load(getClass().getResource("CustomerRegistration.fxml"));



        
        Scene scene = new Scene(grid);
        
        stage.setScene(scene);
        
        stage.setResizable(false);
        
        stage.sizeToScene();
        
//        stage.getIcons().add(new Image(getClass().getResource("icon.png").toString()));
        
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
