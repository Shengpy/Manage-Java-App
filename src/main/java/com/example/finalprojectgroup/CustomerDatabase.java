
package com.example.finalprojectgroup;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CustomerDatabase {
    public static void addRecord(String filePath, Customer cus) {
    ArrayList<Customer> existingRecords = getRecord(filePath); // Load existing records

    // Add the new record to the existing records
    existingRecords.add(cus);

    // Remove duplicates from the list
    Set<Customer> uniqueRecords = new HashSet<>(existingRecords);
    existingRecords = new ArrayList<>(uniqueRecords);

    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
        for (Customer existingRecord : existingRecords) {
            if (existingRecord instanceof GuestAccount)
                out.writeObject("Guest");
            else if (existingRecord instanceof RegularAccount)
                out.writeObject("Regular");
            else if (existingRecord instanceof VIPAccount)
                out.writeObject("VIP");
            out.writeObject(existingRecord); // Write all unique records back to the file
        }
        System.out.println("Record added successfully.");
    } catch (IOException e) {
        System.out.println("Error occurred while writing to the database file: " + e.getMessage());
        }
    }
    public static ArrayList<Customer> getRecord(String filePath) {
        ArrayList<Customer> list = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            while (true) {
                try {
                    String accountType = (String) in.readObject();
                    if (accountType.equals("Guest")){
                        GuestAccount obj = (GuestAccount) in.readObject();
                        list.add(obj);
                    }
                    if (accountType.equals("Regular")){
                        RegularAccount obj = (RegularAccount) in.readObject();
                        list.add(obj);
                    }
                    if (accountType.equals("VIP")){
                        VIPAccount obj = (VIPAccount) in.readObject();
                        list.add(obj);
                    }
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error occurred while reading the database file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public static void deleteAllCustomers() {
        String filePath = "src/main/resources/com/example/data/customer.txt";
        ArrayList<Customer> emptyList = new ArrayList<>();

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            for (Customer obj : emptyList) {
                out.writeObject(obj);
            }
        } catch (IOException e) {
            System.out.println("Error occurred while deleting cuss from the database file: " + e.getMessage());
        }
    }
    public static void saveID(Customer cus){
        String ID = cus.getID();
        String extractedPart = ID.substring(1, 4);
        int IDindex = Integer.parseInt(extractedPart);
        try {
            FileWriter writer = new FileWriter("src/main/resources/com/example/data/deletedCustomerID.txt",true);
            writer.write(String.valueOf(IDindex));
            writer.write("\n");
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static Integer replaceID() {
        ArrayList<String> idList = new ArrayList<>();
        String line = null;

        try {
            FileReader reader = new FileReader("src/main/resources/com/example/data/deletedCustomerID.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);

            while ((line = bufferedReader.readLine()) != null) {
                idList.add(line);
            }

            bufferedReader.close();
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (idList.isEmpty()) {
            return null;
        } else {
            try {
                return Integer.parseInt(reSaveId(idList));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    public static String reSaveId(ArrayList<String> idList) {
        if (idList.isEmpty()) {
            return null;
        } else {
            String idIndex = idList.get(0);
            idList.remove(0);
            try {
                FileWriter writer = new FileWriter("src/main/resources/com/example/data/deletedCustomerID.txt");
                for (String s : idList) {
                    writer.write(s);
                    writer.write("\n");
                }
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return idIndex;
        }
    }
}
