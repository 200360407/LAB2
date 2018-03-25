/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package car;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class DoneViewController implements Initializable {

    @FXML ImageView image;
    
    // This method initialises and sets image in Image View
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Image img= SwingFXUtils.toFXImage(ImageIO.read(new File("./src/image/logo.png")), null);
            image.setImage(img);
        } catch (IOException ex) {
        }
    }    
    // This button changes scene back to carTableView
    public void backButtonClicked(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("carTableView.FXML"));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        stage.setTitle("Happy Making Image");
        stage.setScene(scene);
        stage.show();
    
    }
    
}
