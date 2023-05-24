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
import java.util.ResourceBundle;

public class AdminCustomerInfoController implements Initializable {
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
    private ChoiceBox<String> AccTypeBox = new ChoiceBox<>();
    private final String[] AccTypeList = {"Guest","Regular","VIP"};
    @FXML
    private TextField searchTextField;
    ArrayList<Item> list;
    Customer acc;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AccTypeBox.getItems().addAll(AccTypeList);

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
    public void setInfo(Customer cus){
        acc=cus;
        list=acc.getRentals();
        IdText.setText(cus.getID());
        NameText.setText(cus.getName());
        AddrText.setText(cus.getAddress());
        PhoneText.setText(cus.getPhone());
        AccTypeBox.setValue(cus.getType());
        UsernameText.setText((cus.getUsername()));
        PasswordText.setText(cus.getPassword());
//Item loading
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
    }
    public void UpdateAcc() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminCustomer.fxml"));
        AnchorPane Customer_page = loader.load();
        AdminCustomerController controller = loader.getController();
        controller.UpdateAccountType(acc,AccTypeBox.getValue());
    }
    public void sortAlpha() {
        ObservableList<Item> alphaList = table.getItems();
        Comparator<Item> alphaComparator = Comparator.comparing(Item::getTitle, String.CASE_INSENSITIVE_ORDER);
        alphaList.sort(alphaComparator);
        table.setItems(FXCollections.observableArrayList(alphaList));
        table.refresh();
    }
    public void reset(){
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
    public void change_page(ActionEvent e) throws IOException {
        Stage stage=(Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminCustomer.fxml"));
        AnchorPane Customer_page = loader.load();

        Scene scene = new Scene(Customer_page);
        stage.setScene(scene);
        stage.show();
    }
}


