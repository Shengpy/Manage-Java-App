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
import java.util.ResourceBundle;

import javafx.scene.layout.AnchorPane;

public class CustomerRentalController implements Initializable {

    @FXML
    private TableView<Item> rentTable;
    @FXML
    private TableColumn<Item, String> rentedTitleColumn;
    @FXML
    private TableColumn<Item, Integer> rentedCopiesColumn;

    @FXML
    private TableView<Item> table;
    @FXML
    private TableColumn<Item, String> idColumn;
    @FXML
    private TableColumn<Item, String> titleColumn;
    @FXML
    private TableColumn<Item, String> rentalTypeColumn;
    @FXML
    private TableColumn<Item, String> loanTypeColumn;
    @FXML
    private TableColumn<Item, Integer> numberOfCopiesColumn;
    @FXML
    private TableColumn<Item, Double> rentalFeeColumn;
    @FXML
    private TableColumn<Item, String> rentalStatusColumn;
    @FXML
    private TableColumn<Item, String> genreColumn;
    @FXML
    private Button exitButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button alphaButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label stockLabel;
    @FXML
    private Label rentedLabel;
    private Customer myAccount;
    private ArrayList<Item> rentedItem = new ArrayList<>();
    ArrayList<Customer> customersList;
    ObservableList<Item> rentedItemList;
    public void setCustomer(Customer c,ArrayList<Customer> cusList){
        customersList = cusList;
        myAccount = c;
        rentedItem = myAccount.getRentals();
        ObservableList<Item> rentedItemList =  FXCollections.observableArrayList(rentedItem);
        rentTable.setItems(rentedItemList);
        rentTable.refresh();
    }
    ArrayList<Item> list  = ItemDatabase.getRecord();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Comparator<Item> idComparator = Comparator.comparing(Item::getID);
        list.sort(idComparator);
        ObservableList<Item> itemList = FXCollections.observableArrayList(list);


        idColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("ID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("title"));
        rentalTypeColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("rentType"));
        loanTypeColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("loanType"));
        numberOfCopiesColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("numberOfCopies"));
        rentalFeeColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("rentalFee"));
        rentalStatusColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("rentalStatus"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("genre"));
        table.setItems(itemList);

        rentedTitleColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("title"));
        rentedCopiesColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("numberOfCopies"));
        rentTable.setItems(rentedItemList);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterItems(newValue); // Call the method to filter items based on the search text
        });
    }

    public void exit(ActionEvent e){
        System.exit(0);
    }

    public void sortAlpha(ActionEvent e) {
        ObservableList<Item> alphaList = table.getItems();
        Comparator<Item> alphaComparator = Comparator.comparing(Item::getTitle, String.CASE_INSENSITIVE_ORDER);
        alphaList.sort(alphaComparator);
        table.setItems(FXCollections.observableArrayList(alphaList));
        table.refresh();
    }


    public void reset(ActionEvent e){
        Comparator<Item> idComparator = Comparator.comparing(Item::getID);
        list.sort(idComparator);
        table.setItems(FXCollections.observableArrayList(list));
    }


    private void filterItems(String searchText) {
        ObservableList<Item> filteredItems = FXCollections.observableArrayList();

        for (Item item : list) {
            if ((item.getTitle().toLowerCase().contains(searchText.toLowerCase())) ||(item.getID().toLowerCase().contains(searchText.toLowerCase()))) {
                filteredItems.add(item);
            }
        }
        Comparator<Item> idComparator = Comparator.comparing(Item::getID);
        filteredItems.sort(idComparator);
        table.setItems(filteredItems);
    }

    public void rent(ActionEvent e) {
        Item selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            // No item is selected
            return;
        }
        if (selectedItem.getNumberOfCopies()<=0){
            rentedLabel.setVisible(false);
            stockLabel.setVisible(true);
            return;
        }
        Item copySelectedItem = null;
        String type = selectedItem.getRentType();
        if (type.matches("VideoGame")) {
            copySelectedItem = new Item.VideoGame(selectedItem);
        } else if (type.matches("OldMovieRecord")) {
        copySelectedItem = new Item.OldMovieRecord(selectedItem);
        } else {
        copySelectedItem = new Item.DVD(selectedItem);
        }


        list.remove(selectedItem);

        customersList.remove(myAccount);

        copySelectedItem.setNumberOfCopies(1);

        myAccount.addRental(copySelectedItem);

        customersList.add(myAccount);

        selectedItem.setNumberOfCopies(selectedItem.getNumberOfCopies()-1);

        rentedItem = myAccount.getRentals(); // Update the rentedItem

        list.add(selectedItem);

        ItemDatabase.deleteAllItems();
        for(Item i: list){
            ItemDatabase.addRecord(i);
        }

        Comparator<Item> idComparator = Comparator.comparing(Item::getID);
        list.sort(idComparator);

        CustomerDatabase.deleteAllCustomers();
        for (Customer c: customersList){
            CustomerDatabase.addRecord("src/main/resources/com/example/data/customer.txt",c);
        }


        rentTable.setItems(FXCollections.observableArrayList(rentedItem));// Refresh the rentTable
        table.setItems(FXCollections.observableArrayList(list));
        rentTable.refresh();
        table.refresh();
        stockLabel.setVisible(false);
        rentedLabel.setVisible(true);
    }



    public void delete(Item item) {
        // Remove the selected item from the table
        table.getItems().remove(item);
        // Remove the selected item from the list
        list.remove(item);
        // Delete the selected item from the database file
        ItemDatabase.deleteAllItems();
        for(Item i: list ){
            ItemDatabase.addRecord(i);
        }
        // Update the table view
        table.refresh();
        ItemDatabase.saveID(item);
    }

}
