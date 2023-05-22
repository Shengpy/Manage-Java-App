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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.scene.layout.AnchorPane;

public class AdminCustomerController implements Initializable {
    @FXML
    private TableView<Customer> table;
    @FXML
    private TableColumn<Customer, String> idColumn;
    @FXML
    private TableColumn<Customer, String> NameColumn;
    @FXML
    private TableColumn<Customer, String> AccTypeColumn;
    @FXML
    private TableColumn<Customer, String> PhoneColumn;
    @FXML
    private TableColumn<Customer, String> UsernameColumn;
    @FXML
    private TableColumn<Customer, String> PasswordColumn;
    @FXML
    private ChoiceBox<String> AccTypeBox = new ChoiceBox<>();
    private final String[] AccTypeList = {"Guest","Regular","VIP"};
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
    private Label addNoti;
    @FXML
    private Label removeNoti;
    @FXML
    private Label clearNoti;
    @FXML
    private TextField searchTextField;
    ArrayList<Customer> list  = CustomerDatabase.getRecord("src/main/resources/com/example/data/customer.txt");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Comparator<Customer> idComparator = Comparator.comparing(Customer::getID);
        list.sort(idComparator);
        ObservableList<Customer> CusList = FXCollections.observableArrayList(list);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        AccTypeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        UsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        PasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        table.setItems(CusList);

        AccTypeBox.getItems().addAll(AccTypeList);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterItems(newValue); // Call the method to filter items based on the search text
        });
    }

    public void exit(){
        System.exit(0);
    }
    public void sortAlpha() {
        ObservableList<Customer> alphaList = table.getItems();
        Comparator<Customer> alphaComparator = Comparator.comparing(Customer::getName, String.CASE_INSENSITIVE_ORDER);
        alphaList.sort(alphaComparator);
        table.setItems(FXCollections.observableArrayList(alphaList));
        table.refresh();
    }
    public void VIPButton(){
        table.getItems().clear();
        ArrayList<Customer> zeroCopiesList = new ArrayList<>();
        for (Customer item : list) {
            if (Objects.equals(item.getType(), "VIP")) {
                zeroCopiesList.add(item);
            }
        }
        table.setItems(FXCollections.observableArrayList(zeroCopiesList));
    }
    public void GuestButton(){
        table.getItems().clear();
        ArrayList<Customer> zeroCopiesList = new ArrayList<>();
        for (Customer item : list) {
            if (Objects.equals(item.getType(), "Guest")) {
                zeroCopiesList.add(item);
            }
        }
        table.setItems(FXCollections.observableArrayList(zeroCopiesList));
    }
    public void RegularButton(){
        table.getItems().clear();
        ArrayList<Customer> zeroCopiesList = new ArrayList<>();
        for (Customer item : list) {
            if (Objects.equals(item.getType(), "Regular")) {
                zeroCopiesList.add(item);
            }
        }
        table.setItems(FXCollections.observableArrayList(zeroCopiesList));
    }
    public void reset(){
        Comparator<Customer> idComparator = Comparator.comparing(Customer::getID);
        list.sort(idComparator);
        table.setItems(FXCollections.observableArrayList(list));
    }
    public void delete() {
        Customer selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            // No item is selected
            return;
        }

        // Remove the selected item from the table
        table.getItems().remove(selectedItem);

        // Remove the selected item from the list
        list.remove(selectedItem);

        // Delete the selected item from the database file
        CustomerDatabase.deleteAllCustomers();

        for(Customer i: list ){
            CustomerDatabase.addRecord("src/main/resources/com/example/data/customer.txt",i);
        }

        // Update the table view
        table.refresh();

        addNoti.setVisible(false);
        removeNoti.setVisible(true);
        clearNoti.setVisible(false);
        CustomerDatabase.saveID(selectedItem);
    }

    public void clear(){
        clearTextFields();
        addNoti.setVisible(false);
        removeNoti.setVisible(false);
        clearNoti.setVisible(true);
    }
    private void setCommonItemProperties(Customer cus) {
        cus.SetID();
        cus.SetName(NameText.getText());
        cus.SetAddress(AddrText.getText());
        cus.SetPhone(PhoneText.getText());
        cus.SetUsername(UsernameText.getText());
        cus.SetPassword(PasswordText.getText());
    }
    public void add() {
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
        updateTable();
        addNoti.setVisible(true);
        removeNoti.setVisible(false);
        clearNoti.setVisible(false);
    }
    private void updateTable() {
        Comparator<Customer> idComparator = Comparator.comparing(Customer::getID);
        list.sort(idComparator);
        ObservableList<Customer> itemList = FXCollections.observableArrayList(list);
        table.setItems(itemList);
    }
    private void clearTextFields() {
        NameText.clear();
        AddrText.clear();
        PhoneText.clear();
        UsernameText.clear();
        PasswordText.clear();
    }
    private void filterItems(String searchText) {
        ObservableList<Customer> filteredItems = FXCollections.observableArrayList();

        for (Customer item : list) {
            if ((item.getName().toLowerCase().contains(searchText.toLowerCase())) ||(item.getID().toLowerCase().contains(searchText.toLowerCase()))) {
                filteredItems.add(item);
            }
        }
        Comparator<Customer> idComparator = Comparator.comparing(Customer::getID);
        filteredItems.sort(idComparator);
        table.setItems(filteredItems);
    }
    public void AdminItem_redirect(ActionEvent e) throws IOException {
        Stage stage=(Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminItem.fxml"));
        AnchorPane Customer_page = loader.load();

        Scene scene = new Scene(Customer_page);
        stage.setScene(scene);
        stage.show();
    }
    public void AdminCustomerInfo_redirect(ActionEvent e)throws IOException{
        Stage stage=(Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminCustomerInfo.fxml"));
        AnchorPane Customer_page = loader.load();
        AdminCustomerInfoController controller = loader.getController();
        Customer selected = table.getSelectionModel().getSelectedItem();
        controller.setInfo(selected);

        Scene scene = new Scene(Customer_page);
        stage.setScene(scene);
        stage.show();
    }
    public void UpdateAccountType(Customer cus,String Type){
        for (Customer i: list){
            if(Objects.equals(cus.getID(), i.getID())){
                if(Objects.equals(Type, "VIP")) {
                    VIPAccount newAcc= new VIPAccount(cus);
                    list.remove(i);
                    list.add(newAcc);
                    break;
                }
                if(Objects.equals(Type, "Regular")){
                    {
                        RegularAccount newAcc= new RegularAccount(cus);
                        list.remove(i);
                        list.add(newAcc);
                        break;
                    }
                }
                if(Objects.equals(Type, "Guest")){
                    {
                        GuestAccount newAcc= new GuestAccount(cus);
                        list.remove(i);
                        list.add(newAcc);
                        break;
                    }
                }
            }
        }
        CustomerDatabase.deleteAllCustomers();
        for (Customer i : list) {
            CustomerDatabase.addRecord("src/main/resources/com/example/data/customer.txt", i);
        }
    }
}
