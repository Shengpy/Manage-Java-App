package com.example.finalprojectgroup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginPageController {
    @FXML
    private TextField Username;
    @FXML
    private PasswordField Password;
    @FXML
    private Label wrongPass;

    public void Login(ActionEvent e) throws IOException {

        if((Objects.equals(Username.getText(), "ADMIN")) && (Objects.equals(Password.getText(), "qwerty"))){
            Stage stage=(Stage)((Node) e.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminItem.fxml"));
            AnchorPane itemPage = loader.load();

            Scene scene = new Scene(itemPage);
            stage.setScene(scene);
            stage.setTitle("Rental App");
            stage.show();
        } else{
            wrongPass.setVisible(true);
        }
    }

}
