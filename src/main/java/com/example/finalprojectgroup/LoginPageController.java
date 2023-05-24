package com.example.finalprojectgroup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class LoginPageController {
    @FXML
    private TextField Username;
    @FXML
    private PasswordField Password;
    @FXML
    private Label wrongPass;
    @FXML
    private Label wrongUser;

    ArrayList<Customer> cusList= CustomerDatabase.getRecord("src/main/resources/com/example/data/customer.txt");
    public void Login(ActionEvent e) throws IOException {

        if((Objects.equals(Username.getText(), "ADMIN")) && (Objects.equals(Password.getText(), "qwerty"))) {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminItem.fxml"));
            AnchorPane itemPage = loader.load();
            Scene scene = new Scene(itemPage);
            stage.setScene(scene);
            stage.setTitle("Rental App");
            stage.show();
        }
        Customer customer = check(Username.getText(), Password.getText());
        if(customer != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerRental.fxml"));
            Parent customerRentalPage = loader.load();

            CustomerRentalController customerRentalController = loader.getController();
            customerRentalController.setCustomer(customer, cusList);

            Scene scene = new Scene(customerRentalPage);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Rental App");
            stage.show();
        }
    }

    public Customer check(String Username, String Password){
        for(Customer c: cusList){
            if(c.getUsername().equals(Username)) {
                wrongUser.setVisible(false);
                wrongPass.setVisible(true);
            }else{
                wrongUser.setVisible(true);
                wrongPass.setVisible(false);
                continue;
            }
            if(c.getPassword().equals(Password)){
                    wrongPass.setVisible(false);
                    return c;
                } else {return null;}
            }
        return null;
    }

    public void signUp(ActionEvent e) throws IOException {
        Stage stage=(Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUpPage.fxml"));
        AnchorPane Customer_page = loader.load();

        Scene scene = new Scene(Customer_page);
        stage.setScene(scene);
        stage.show();
    }
    }


