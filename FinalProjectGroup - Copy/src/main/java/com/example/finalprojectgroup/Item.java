package com.example.finalprojectgroup;

import java.util.Arrays;

/*
Creating abstract class Item, implements Serializable for reading and writing the whole object in the file
Item will have 3 inheritors: DVD, VideoGame, OldMovieRecord. The key difference between them is the genre, the VideoGame does not have Genre.
But for easier work later on, we choose the
*/
public abstract class Item implements java.io.Serializable {
    //Declare Item's field
    private int numberOfCopies;
    private double rentalFee;
    private String ID, title, rentType, loanType, rentalStatus;
    private static int trackingId;
    private int year;

    // Contructor for Item
    // year to make ID,
    public Item(Integer year, String title, String rentType, String loanType, Integer numberOfCopies, double rentalFee){
        this.year = year;
        this.title = title;
        setRentType(rentType);
        this.loanType = loanType;
        this.numberOfCopies = numberOfCopies;
        this.rentalFee = rentalFee;
        setRentalStatus();
        setID();
    }

    //default constructor
    public Item() {

    }

    public double getRentalFee() {
        return rentalFee;
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public String getID() {
        return ID;
    }

    public String getLoanType() {
        return loanType;
    }

    public String getRentalStatus() {
        return rentalStatus;
    }

    public String getRentType() {
        return rentType;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public static int getTrackingId() {
        return trackingId;
    }

    public void setID(){
        this.ID = formatID();
    }

    private String formatID(){
        Integer pendingID = (ItemDatabase.replaceID());
        if(pendingID != null){
            trackingId = pendingID;
            System.out.println(pendingID);
        } else{
            trackingId = ItemDatabase.getRecord().size() + 1;
        }
        return String.format("I"+"%03d"+"-"+getYear(),trackingId);
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
        setRentalStatus();
    }

    public void setRentalFee(double rentalFee) {
        this.rentalFee = rentalFee;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setRentalStatus() {
        if(getNumberOfCopies() <= 0){
            this.rentalStatus = "borrowed";
        } else {
            this.rentalStatus = "available";
        }
    }

    public void setRentType(String rentType) {
        String[] availableRentType = {"DVD", "VideoGame", "OldMovieRecord"};
        if (Arrays.asList(availableRentType).contains(rentType)) {
            this.rentType = rentType;
        } else{
            throw new IllegalArgumentException("rent type is not valid, 3 available rent type are DVD, VideoGame, OldMovieRecord");
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class VideoGame extends Item implements java.io.Serializable {
        String genre = "";

        public VideoGame(){

        }
        public VideoGame(Integer year, String title, String rentType, String loanType, Integer numberOfCopies, double rentalFee) {
            super(year, title, rentType, loanType, numberOfCopies, rentalFee);
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        @Override
        public String toString() {
            return String.format("%s,%s,%s,%s,%d,%.2f,%s",
                    getID(), getTitle(), getRentType(), getLoanType(), getNumberOfCopies(), getRentalFee(), getRentalStatus());
        }


    }

    public static class DVD extends Item implements java.io.Serializable {
        private String genre;

        public DVD(){
        }

        public DVD(Integer year, String title, String rentType, String loanType, Integer numberOfCopies, double rentalFee, String genre) {
            super(year, title, rentType, loanType, numberOfCopies, rentalFee);
            setGenre(genre);
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            String[] availableGenre = {"Action", "Horror", "Drama", "Comedy"};
            if (Arrays.asList(availableGenre).contains(genre)) {
                this.genre = genre;
            } else {
                throw new IllegalArgumentException("Genre is not valid, 4 available genres are Action, Horror, Drama, Comedy ");
            }
        }

        @Override
        public String toString() {
            return String.format("%s,%s,%s,%s,%d,%.2f,%s,%s",
                    getID(), getTitle(), getRentType(), getLoanType(), getNumberOfCopies(), getRentalFee(), getRentalStatus(), getGenre());
        }
    }

    public static class OldMovieRecord extends Item implements java.io.Serializable {
        private String genre;

        public OldMovieRecord(){
        }

        public OldMovieRecord(Integer year, String title, String rentType, String loanType, Integer numberOfCopies, double rentalFee, String genre) {
            super(year, title, rentType, loanType, numberOfCopies, rentalFee);
            setGenre(genre);
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            String[] availableGenre = {"Action", "Horror", "Drama", "Comedy"};
            if (Arrays.asList(availableGenre).contains(genre)) {
                this.genre = genre;
            } else {
                throw new IllegalArgumentException("Genre is not valid, 4 available genres are Action, Horror, Drama, Comedy ");
            }
        }

        @Override
        public String toString() {
            return String.format("%s,%s,%s,%s,%d,%.2f,%s,%s",
                    getID(), getTitle(), getRentType(), getLoanType(), getNumberOfCopies(), getRentalFee(), getRentalStatus(), getGenre());
        }
    }
}

