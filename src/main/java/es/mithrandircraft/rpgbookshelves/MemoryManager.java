package es.mithrandircraft.rpgbookshelves;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import es.mithrandircraft.rpgbookshelves.callbacks.*;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MemoryManager {

    private final es.mithrandircraft.rpgbookshelves.RPGBookshelves mainClassAccess;

    MemoryManager(es.mithrandircraft.rpgbookshelves.RPGBookshelves main) { this.mainClassAccess = main; }

    //Java File I.O. functions

    public boolean JSONFileCreateIfNotExists() //Returns true if file was created within directory
    {
        try {
            //Create file (will do nothing if it already exists):
            if(new File(mainClassAccess.getDataFolder().getAbsolutePath() + "/rpgshelves.json").createNewFile()){ JSONStoreInFile("[]"); } //Store empty JSON array in file
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private void JSONStoreInFile(String toStore) //Writes json content as string to file
    {
        try {
            FileWriter writer = new FileWriter(mainClassAccess.getDataFolder().getAbsolutePath() + "/rpgshelves.json");
            writer.write(toStore);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private BufferedReader JSONGetFromFile() //Gets json content from file in BufferedReader format
    {
        try {
            return new BufferedReader(new FileReader(mainClassAccess.getDataFolder().getAbsolutePath() + "/rpgshelves.json")); //Return buffered file contents
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //GSON Serialization functions

    private String JSONSerializeRPGShelves(List<RPGShelf> RPGShelves) //Serializes class ArrayList to json String
    {
        return new Gson().toJson(RPGShelves);
    }

    private List<RPGShelf> JSONDeserializeRPGShelves(BufferedReader RPGShelves) //Serializes from BufferedReader to ArrayList.
    {
        return new Gson().fromJson(RPGShelves, new TypeToken<ArrayList<RPGShelf>>(){}.getType()); //TypeToken gets the type of an arraylist of RPGShelves
    }

    //File data manipulation and querying using GSON

    public List<RPGShelf> JSONGetShelves() //Gets deserialized rpg shelves stored in JSON file
    {
        return JSONDeserializeRPGShelves(JSONGetFromFile());
    }

    public void JSONAddLibraryIfNotExists(int X, int Y, int Z, String world, List<String> content, LibraryAddCallback callback) //Adds an rpg library with specified book content to the JSON list if it didn't already exist.
    {
        //Get the whole list of shelves:
        List<RPGShelf> shelves = JSONGetShelves();
        //Look for possible rpg library registered with coordinates:
        boolean libraryFound = false;
        for(RPGShelf shelf : shelves)
        {
            if(shelf.x == X && shelf.y == Y && shelf.z == Z && shelf.w.equals(world)) //Shelf is already registered at coordinates and world
            {
                libraryFound = true;
                break;
            }
        }
        if(!libraryFound){ //Shelf didn't already exist, add it, and store list back to JSON file:
            shelves.add(new RPGShelf(X, Y, Z, world, content));
            JSONStoreInFile(JSONSerializeRPGShelves(shelves));
            Bukkit.getScheduler().runTask(mainClassAccess, new Runnable() { //Callback to main thread
                @Override
                public void run() {
                    callback.onAdded();
                }
            });
        }
    }

    public void JSONRemoveRPGLibraryIfExists(int X, int Y, int Z, String world)
    {
        //Get the whole list of shelves:
        List<RPGShelf> shelves = JSONGetShelves();
        //Look for possible rpg library registered with coordinates:
        for(RPGShelf shelf : shelves)
        {
            if(shelf.x == X && shelf.y == Y && shelf.z == Z && shelf.w.equals(world)) //Shelf is registered at coordinates and world
            {
                shelves.remove(shelf); //Remove shelf from list
                JSONStoreInFile(JSONSerializeRPGShelves(shelves)); //Sotre back
                break;
            }
        }
    }

    public void JSONGetPagesIfRPGLibraryExists(int X, int Y, int Z, String world, LibraryReadCallback callback)
    {
        //Get the whole list of shelves:
        List<RPGShelf> shelves = JSONGetShelves();
        //Look for possible rpg library registered with coordinates:
        for(RPGShelf shelf : shelves)
        {
            if(shelf.x == X && shelf.y == Y && shelf.z == Z && shelf.w.equals(world)) //Shelf is registered at coordinates and world
            {
                Bukkit.getScheduler().runTask(mainClassAccess, new Runnable() { //Callback to main thread
                    @Override
                    public void run() {
                        callback.onQueryDone(shelf.bookContents);
                    }
                });
            }
        }
    }
}
