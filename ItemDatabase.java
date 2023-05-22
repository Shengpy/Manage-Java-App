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

        if (idList.isEmpty()) {
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