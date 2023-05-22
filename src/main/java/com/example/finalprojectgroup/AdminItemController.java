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

public class AdminItemController implements Initializable {
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
    private ChoiceBox<String> genreBox = new ChoiceBox<>();
    private final String[] availableGenre = {"Horror", "Drama", "Comedy", "Action"};

    @FXML
    private ChoiceBox<String> rentalTypeBox = new ChoiceBox<>();
    private final String[] rentalList = {"VideoGame","OldMovieRecord","DVD"};

    @FXML
    private ChoiceBox<String> loanTypeBox= new ChoiceBox<>();
    private final String[] loanTypeList = {"2-day","1-week"};
    @FXML
    private TextField yearText;
    @FXML
    private TextField titleText;
    @FXML
    private TextField copiesText;
    @FXML
    private TextField feeText;
    @FXML
    private Label addNoti;
    @FXML
    private Label removeNoti;
    @FXML
    private Label clearNoti;
    @FXML
    private TextField stockTextField;
    @FXML
    private TextField searchTextField;
    ArrayList<Item> list  = ItemDatabase.getRecord("src/main/resources/com/example/data/item.txt");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Comparator<Item> idComparator = Comparator.comparing(Item::getID);
        list.sort(idComparator);
        ObservableList<Item> itemList = FXCollections.observableArrayList(list);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        rentalTypeColumn.setCellValueFactory(new PropertyValueFactory<>("rentType"));
        loanTypeColumn.setCellValueFactory(new PropertyValueFactory<>("loanType"));
        numberOfCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfCopies"));
        rentalFeeColumn.setCellValueFactory(new PropertyValueFactory<>("rentalFee"));
        rentalStatusColumn.setCellValueFactory(new PropertyValueFactory<>("rentalStatus"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        table.setItems(itemList);

        genreBox.getItems().addAll(availableGenre);
        rentalTypeBox.getItems().addAll(rentalList);
        loanTypeBox.getItems().addAll(loanTypeList);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterItems(newValue); // Call the method to filter items based on the search text
        });
    }

    public void exit(){
        System.exit(0);
    }

    public void updateStock() {
        Item selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        list.remove(selectedItem);
        selectedItem.setNumberOfCopies(Integer.parseInt(stockTextField.getText()));
        list.add(selectedItem);
        for (Item i : list) {
            ItemDatabase.addRecord("src/main/resources/com/example/data/item.txt", i);
        }
        Comparator<Item> idComparator = Comparator.comparing(Item::getID);
        list.sort(idComparator);
        table.setItems(FXCollections.observableArrayList(list));
        table.refresh(); // Refresh the table view
    }
    public void sortAlpha() {
        ObservableList<Item> alphaList = table.getItems();
        Comparator<Item> alphaComparator = Comparator.comparing(Item::getTitle, String.CASE_INSENSITIVE_ORDER);
        alphaList.sort(alphaComparator);
        table.setItems(FXCollections.observableArrayList(alphaList));
        table.refresh();
    }
    public void setOutOfStockButton(){
        table.getItems().clear();
        ArrayList<Item> zeroCopiesList = new ArrayList<>();
        for (Item item : list) {
            if (item.getNumberOfCopies() == 0) {
                zeroCopiesList.add(item);
            }
        }
        table.setItems(FXCollections.observableArrayList(zeroCopiesList));
    }

    public void reset(){
        Comparator<Item> idComparator = Comparator.comparing(Item::getID);
        list.sort(idComparator);
        table.setItems(FXCollections.observableArrayList(list));
    }


    public void delete() {
        Item selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            // No item is selected
            return;
        }

        // Remove the selected item from the table
        table.getItems().remove(selectedItem);

        // Remove the selected item from the list
        list.remove(selectedItem);

        // Delete the selected item from the database file
        ItemDatabase.deleteAllItems();

        for(Item i: list ){
        ItemDatabase.addRecord("src/main/resources/com/example/data/item.txt",i);
        }

        // Update the table view
        table.refresh();

        addNoti.setVisible(false);
        removeNoti.setVisible(true);
        clearNoti.setVisible(false);
        ItemDatabase.saveID(selectedItem);
    }

    public void clear(){
        clearTextFields();
        addNoti.setVisible(false);
        removeNoti.setVisible(false);
        clearNoti.setVisible(true);
    }

    public void add() {
        String type = rentalTypeBox.getValue();
        Item newItem;

        if (type.matches("VideoGame")) {
            newItem = createVideoGame();
        } else if (type.matches("OldMovieRecord")) {
            newItem = createOldMovieRecord();
        } else {
            newItem = createDVD();
        }

        ItemDatabase.addRecord("src/main/resources/com/example/data/item.txt", newItem);
        list.add(newItem);
        updateTable();
        addNoti.setVisible(true);
        removeNoti.setVisible(false);
        clearNoti.setVisible(false);
    }

    private Item.VideoGame createVideoGame() {
        Item.VideoGame game = new Item.VideoGame();
        setCommonItemProperties(game);
        return game;
    }

    private Item.OldMovieRecord createOldMovieRecord() {
        Item.OldMovieRecord movie = new Item.OldMovieRecord();
        setCommonItemProperties(movie);
        movie.setGenre(genreBox.getValue());
        return movie;
    }

    private Item.DVD createDVD() {
        Item.DVD dvd = new Item.DVD();
        setCommonItemProperties(dvd);
        dvd.setGenre(genreBox.getValue());
        return dvd;
    }

    private void setCommonItemProperties(Item item) {
        item.setYear(Integer.parseInt(yearText.getText()));
        item.setID();
        item.setRentType(rentalTypeBox.getValue());
        item.setYear(Integer.parseInt(yearText.getText()));
        item.setTitle(titleText.getText());
        item.setLoanType(loanTypeBox.getValue());
        item.setNumberOfCopies(Integer.parseInt(copiesText.getText()));
        item.setRentalFee(Double.parseDouble(feeText.getText()));
        item.setRentalStatus();
    }

    private void updateTable() {
        Comparator<Item> idComparator = Comparator.comparing(Item::getID);
        list.sort(idComparator);
        ObservableList<Item> itemList = FXCollections.observableArrayList(list);
        table.setItems(itemList);
    }

    private void clearTextFields() {
        yearText.clear();
        titleText.clear();
        copiesText.clear();
        feeText.clear();
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
    public void change_page(ActionEvent e) throws IOException {
        Stage stage=(Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminCustomer.fxml"));
        AnchorPane Customer_page = loader.load();

        Scene scene = new Scene(Customer_page);
        stage.setScene(scene);
        stage.show();
    }

}


