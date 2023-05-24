package com.example.finalprojectgroup;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ItemDatabase {
    private static final String filePath = "src/main/resources/com/example/data/item.txt";

    public static void addRecord(Item item) {
        ArrayList<Item> existingItems = getRecord(); // Load existing items

        // Add the new item to the existing items
        existingItems.add(item);

        // Remove duplicates from the list
        Set<Item> uniqueItems = new HashSet<>(existingItems);
        existingItems = new ArrayList<>(uniqueItems);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            for (Item existingItem : existingItems) {
                out.writeObject(existingItem); // Write all unique items back to the file
            }

        } catch (IOException e) {
            System.out.println("Error occurred while writing to the database file: " + e.getMessage());
        }
    }


    public static ArrayList<Item> getRecord() {
        ArrayList<Item> list = new ArrayList<>();
        Set<String> itemIds = new HashSet<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            while (true) {
                try {
                    Item obj = (Item) in.readObject();

                    if (!itemIds.contains(obj.getID())) {
                        list.add(obj);
                        itemIds.add(obj.getID());
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

    public static void deleteAllItems() {
        ArrayList<Item> emptyList = new ArrayList<>();

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            for (Item obj : emptyList) {
                out.writeObject(obj);
            }

        } catch (IOException e) {
            System.out.println("Error occurred while deleting items from the database file: " + e.getMessage());
        }
    }

    //use this method while removing item so that the deleted ID can be store in a text file,
    //the next adding will check the file and use the deleted one
    //if the file is empty, keep following the format itemList.size()+1
    public static void saveID(Item item){
        String ID = item.getID();
        String extractedPart = ID.substring(1, 4);
        int IDindex = Integer.parseInt(extractedPart);
        try {
            FileWriter writer = new FileWriter("src/main/resources/com/example/data/deletedItemID.txt",true);
            writer.write(String.valueOf(IDindex));
            writer.write("\n");
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //this method is put directly into the Item class where we create the ID
    //It will check if the file is empty or not, return null
    //IF it's not empty return a number and the method in Item class will take over
    public static Integer replaceID() {
        ArrayList<String> idList = new ArrayList<>();
        String line = null;

        try {
            FileReader reader = new FileReader("src/main/resources/com/example/data/deletedItemID.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);

            while ((line = bufferedReader.readLine()) != null) {
                idList.add(line);
            }

            bufferedReader.close();
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (idList.isEmpty()||(idList.get(0).equals(""))) {
            return null;
        } else {
            try {
                return Integer.parseInt(Objects.requireNonNull(reSaveId(idList)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    //this method take out and ID and rewrite the rest to make sure there are no data lost
    //this method also return an ID index for the replaceID method to return
    public static String reSaveId(ArrayList<String> idList) {
        if (idList.isEmpty()) {
            return null;
        } else {
            String idIndex = idList.get(0);
            idList.remove(0);
            try {
                FileWriter writer = new FileWriter("src/main/resources/com/example/data/deletedItemID.txt");
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