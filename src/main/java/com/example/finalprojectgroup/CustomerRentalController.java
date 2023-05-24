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
    private static final int MAXIMUM = 0;
    private static final int loanViolate = -1;

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
    private Button infoButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label notificationLabel;
    @FXML
    private Label rwLabel;
    @FXML
    private Label freeRentalLabel;
    @FXML
    private Label freeLabel;
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
        rwLabel.setText("Reward Points:"+myAccount.rewardPoints);
        freeRentalLabel.setText("Free Rental:"+myAccount.rent_free);
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

    public void exit(ActionEvent e) throws IOException {
        Stage stage=(Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        AnchorPane root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Rental App");
        stage.show();
    }

    public void showInfo(ActionEvent e)throws IOException{
        Stage stage=(Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerInfo.fxml"));
        AnchorPane Customer_page = loader.load();
        CustomerInfoController controller = loader.getController();
        controller.setInfo(myAccount,customersList);
        Scene scene = new Scene(Customer_page);
        stage.setScene(scene);
        stage.show();
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
        freeLabel.setVisible(false);
        Item selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            // No item is selected
            return;
        }
        if (selectedItem.getNumberOfCopies() <= 0){
            notificationLabel.setText("ITEM IS CURRENTLY OUT OF STOCK!");
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

        int check = myAccount.addRental(copySelectedItem);
        if(check == MAXIMUM){
            notificationLabel.setText("YOU EXCEEDED THE MAXIMUM RENTAL");
            list.add(selectedItem);
            return;
        }
        if (check == loanViolate){
            notificationLabel.setText("YOU CAN NOT RENT 2-DAY ITEM TYPE(GUEST)");
            list.add(selectedItem);
            return;
        }
        if(myAccount.rent_free >= 1 && myAccount.rewardPoints == 10 ){
            myAccount.rent_free = myAccount.rent_free-1;
            freeLabel.setVisible(true);
        }
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
        notificationLabel.setText("ITEM RENTED SUCCESSFULLY!");
        rwLabel.setText("Reward Points:"+myAccount.rewardPoints);
        freeRentalLabel.setText("Free Rental:"+myAccount.rent_free);
    }
    public void returnItem(ActionEvent event){
        Item rentSelectedItem = rentTable.getSelectionModel().getSelectedItem();
        if (rentSelectedItem == null) {
            // No item is selected
            return;
        }
        customersList.remove(myAccount);
        int check = myAccount.removeRental(rentSelectedItem);
        if (check == 1){
            customersList.add(myAccount);
        } else if(check == 2){
            if(myAccount.getType().equals("Guest")){
                myAccount = new RegularAccount(myAccount);
                notificationLabel.setText("Upgraded from Guest to Regular!");
            } else if(myAccount.getType().equals("Regular")){
                myAccount = new VIPAccount(myAccount);
                notificationLabel.setText("Congrats VIP! start stacking your points!");
            }
            customersList.add(myAccount);
        } else{
            return;
        }
        rentedItem = myAccount.getRentals();

        for(Item i:list){
            if(i.getID().equals(rentSelectedItem.getID())){
                i.setNumberOfCopies(i.getNumberOfCopies()+1);
            }
        }

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
