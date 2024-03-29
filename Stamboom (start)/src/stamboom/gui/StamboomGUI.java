/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author frankpeeters
 */
public class StamboomGUI extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
       System.setProperty("glass.accessible.force", "false");
        Parent root = FXMLLoader.load(getClass().getResource("StamboomGUI.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
   

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}