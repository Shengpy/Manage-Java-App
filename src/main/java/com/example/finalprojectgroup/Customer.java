package com.example.finalprojectgroup;

import java.util.ArrayList;
import java.util.Objects;

public class Customer implements java.io.Serializable {
    private String ID,name,address,phone,username,password,Type;
    private ArrayList<Item> rentals;
    public int Number_pay=0;
    public int Max_rent=0;
    public int promote_condition=0;

    int rewardPoints=0;
    int rent_free=0;

    public int getRewardPoints() {
        return rewardPoints;
    }

    public int getRent_free() {
        return rent_free;
    }

    private static int trackingId;
    public Customer(String ID, String name, String address, String phone, String username, String password) {
        if (!isValidID(ID)) {
            throw new IllegalArgumentException("Invalid ID format.");
        }
        this.ID=ID;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.rentals = new ArrayList<>();
        this.username = username;
        this.password = password;
    }
    public Customer(){}
    public String getID() {
        return ID;
    }
    public void SetID(){
        Integer pendingID = (CustomerDatabase.replaceID());
        if(pendingID != null){
            trackingId = pendingID;
            this.ID = String.format("C"+"%03d",trackingId);
        } else{
            trackingId = CustomerDatabase.getRecord("src/main/resources/com/example/data/customer.txt").size() + 1;
            this.ID = String.format("C"+"%03d",trackingId);
        }
    }
    public String getName() {
        return name;
    }
    public void SetName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void SetAddress(String address) {
        this.address = address;
    }
    public String getPhone() {
        return phone;
    }
    public void SetPhone(String phone) {this.phone = phone;}

    public ArrayList<Item> getRentals() {return rentals;}
    public void SetRentals(ArrayList<Item> rental){this.rentals=rental;}
    public String getUsername() {return username;}
    public void SetUsername(String a){username=a;}
    public String getPassword() {return password;}
    public void SetPassword(String a){password=a;}
    public String getType(){return Type;}
    public void SetType(String a){Type=a;}
    public static int getTrackingId() {
        return trackingId;
    }
    public static boolean isValidID(String ID) {return ID.matches("^C\\d{3}$");}
    public int addRental(Item rental){return 1;}
    public int removeRental(Item rental){return 1;}
    public void setNumber_pay(int number_pay) {
        this.Number_pay = number_pay;
    }
}
class GuestAccount extends Customer {

    public GuestAccount(String ID, String name, String address, String phone, String username, String password) {
        super(ID, name, address, phone, username, password);
        Max_rent=2;
        promote_condition=3;
        SetType("Guest");
    }
    public GuestAccount(Customer acc){
        super(acc.getID(),acc.getName(),acc.getAddress(),acc.getPhone(),acc.getUsername(),acc.getPassword());
        Max_rent=2;
        promote_condition=3;
        SetType("Guest");
    }
    public int addRental(Item rental) {
        for(Item i: getRentals()){
            if (Objects.equals(i.getID(), rental.getID())) {
                int checkCopy = i.getNumberOfCopies();
                if(checkCopy >= 1){
                    return 0;
                } else{
                    i.setNumberOfCopies(i.getNumberOfCopies() + rental.getNumberOfCopies());
                    return 1;
                }

            }
        }
        if (getRentals().size() == Max_rent){
            return 0;
            //System.out.println("Error: Guest account can only rent 1-day items.");
        }
        if (Objects.equals(rental.getLoanType(), "2-day")){
            return -1;
        }
        getRentals().add(rental);
        return 1;
    }
    //__2 promote ___ 0 false ____ 1 true
    @Override
    public int removeRental(Item rental){
        for (Item r : getRentals())
            if (Objects.equals(rental.getID(), r.getID())){
                r.setNumberOfCopies(r.getNumberOfCopies()-1);
                Number_pay+=1;
                if (r.getNumberOfCopies()==0){
                    getRentals().remove(r);
                }
                //promote account
                if(Number_pay > promote_condition){
                    return 2;
                }
                return 1;
            }
        return 0;
    }
}
class RegularAccount extends GuestAccount {
    public RegularAccount(String ID, String name, String address, String phone, String username, String password) {
        super(ID, name, address, phone, username, password);
        promote_condition=5;
        setNumber_pay(0);
        SetType("Regular");
    }
    public RegularAccount(Customer acc){
        super(acc.getID(),acc.getName(),acc.getAddress(),acc.getPhone(),acc.getUsername(),acc.getPassword());
        SetRentals(acc.getRentals());
        promote_condition=5;
        setNumber_pay(0);
        SetType("Regular");
    }
    public RegularAccount(GuestAccount acc){
        super(acc.getID(),acc.getName(),acc.getAddress(),acc.getPhone(),acc.getUsername(),acc.getPassword());
        SetRentals(acc.getRentals());
        promote_condition=5;
        setNumber_pay(0);
        SetType("Regular");
    }
    @Override
    public int addRental(Item rental) {
        for(Item i: getRentals()){
            if (Objects.equals(i.getID(), rental.getID())) {
                i.setNumberOfCopies(i.getNumberOfCopies() + rental.getNumberOfCopies());
                return 1;
            }
        }
        getRentals().add(rental);
        return 1;
    }
}
class VIPAccount extends RegularAccount {

    public VIPAccount(String ID, String name, String address, String phone, String username, String password) {
        super(ID, name, address, phone, username, password);
        SetType("VIP");
    }
    public VIPAccount(RegularAccount acc){
        super(acc.getID(),acc.getName(),acc.getAddress(),acc.getPhone(),acc.getUsername(),acc.getPassword());
        SetRentals(acc.getRentals());
        SetType("VIP");
    }
    public VIPAccount(Customer acc){
        super(acc.getID(),acc.getName(),acc.getAddress(),acc.getPhone(),acc.getUsername(),acc.getPassword());
        SetRentals(acc.getRentals());
        setNumber_pay(0);
        SetType("VIP");
    }
    public int addRental(Item rental) {
        boolean check=true;
        for(Item i: getRentals()){
            if (Objects.equals(i.getID(), rental.getID())) {
                i.setNumberOfCopies(i.getNumberOfCopies() + rental.getNumberOfCopies());
                check=false;
                break;
            }
        }
        if(check)
                getRentals().add(rental);
        this.rewardPoints += 10;
        //reward---------------------------
        if (this.rewardPoints >= 100) {
            rewardPoints=0;
            rent_free+=1;
            //System.out.println("You have enough reward points to rent 1 item for free.");
        }
        return 1;
    }
}