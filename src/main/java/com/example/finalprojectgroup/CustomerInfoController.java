package com.example.finalprojectgroup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

public class CustomerInfoController implements Initializable {

    @FXML
    private Label IdText;
    @FXML
    private Label NameText;
    @FXML
    private Label AddrText;
    @FXML
    private Label PhoneText;
    @FXML
    private Label UsernameText;
    @FXML
    private Label PasswordText;
    @FXML
    private Label typeLabel;

    ArrayList<Item> list;
    Customer acc;
    ArrayList<Customer> cusList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    public void setInfo(Customer cus,ArrayList<Customer> cusListInfo){
        acc=cus;
        list=acc.getRentals();
        IdText.setText(cus.getID());
        NameText.setText(cus.getName());
        AddrText.setText(cus.getAddress());
        PhoneText.setText(cus.getPhone());
        typeLabel.setText(cus.getType());
        UsernameText.setText((cus.getUsername()));
        PasswordText.setText(cus.getPassword());

        cusList = cusListInfo;
    }
    public void change_page(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerRental.fxml"));
        Parent customerRentalPage = loader.load();

        CustomerRentalController customerRentalController = loader.getController();
        customerRentalController.setCustomer(acc, cusList);

        Scene scene = new Scene(customerRentalPage);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Rental App");
        stage.show();
    }
    public void exit(ActionEvent e) throws IOException {
        Stage stage=(Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        AnchorPane root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Rental App");
        stage.show();
    }

}


