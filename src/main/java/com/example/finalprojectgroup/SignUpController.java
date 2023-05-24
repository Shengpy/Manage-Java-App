package com.example.finalprojectgroup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private ChoiceBox<String> AccTypeBox = new ChoiceBox<>();
    private final String[] AccTypeList = {"Guest"};
    @FXML
    private TextField NameText;
    @FXML
    private TextField AddrText;
    @FXML
    private TextField PhoneText;
    @FXML
    private TextField UsernameText;
    @FXML
    private TextField PasswordText;
    @FXML
    private Label notiLabel;
    ArrayList<Customer> list  = CustomerDatabase.getRecord("src/main/resources/com/example/data/customer.txt");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AccTypeBox.getItems().addAll(AccTypeList);
    }

    public void clear(){
        clearTextFields();
    }
    private void setCommonItemProperties(Customer cus) {
        cus.SetID();
        cus.SetName(NameText.getText());
        cus.SetAddress(AddrText.getText());
        cus.SetPhone(PhoneText.getText());
        cus.SetUsername(UsernameText.getText());
        cus.SetPassword(PasswordText.getText());
    }
    public void add(ActionEvent e) throws IOException {
        for(Customer c: list){
            if(c.getUsername().equals(UsernameText.getText())){
                notiLabel.setVisible(true);
                return;
            }
        }
        String type = AccTypeBox.getValue();
        Customer newCus = new Customer();
        setCommonItemProperties(newCus);

        if (type.matches("Guest")) {
            newCus = new GuestAccount(newCus);
        } else if (type.matches("Regular")) {
            newCus = new RegularAccount(newCus);
        } else {
            newCus = new VIPAccount(newCus);
        }

        CustomerDatabase.addRecord("src/main/resources/com/example/data/customer.txt", newCus);
        list.add(newCus);
        Stage stage=(Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        AnchorPane Customer_page = loader.load();

        Scene scene = new Scene(Customer_page);
        stage.setScene(scene);
        stage.show();
    }
    private void clearTextFields() {
        NameText.clear();
        AddrText.clear();
        PhoneText.clear();
        UsernameText.clear();
        PasswordText.clear();
    }

}
