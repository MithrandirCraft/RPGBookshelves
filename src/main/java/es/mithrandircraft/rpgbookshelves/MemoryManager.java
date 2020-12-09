package es.mithrandircraft.rpgbookshelves;

import com.google.gson.Gson;
import es.mithrandircraft.rpgbookshelves.callbacks.*;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.List;

public class MemoryManager {

    private final es.mithrandircraft.rpgbookshelves.RPGBookshelves mainClassAccess;

    MemoryManager(es.mithrandircraft.rpgbookshelves.RPGBookshelves main) { this.mainClassAccess = main; }

    //Java File I.O. functions

    public boolean CreateBaseSaveDirectoryIfNotExists()
    {
        File directory = new File(mainClassAccess.getDataFolder().getAbsolutePath() + "/ShelfData");
        if (!directory.exists()){
            return directory.mkdir();
        }
        else return false;
    }
    //Returns true if file was created within directory
    public boolean CreateFileIfNotExists(String filename)
    {
        try {
            //Create file (will do nothing if it already exists):
            return new File(mainClassAccess.getDataFolder().getAbsolutePath() + "/ShelfData/" + filename).createNewFile(); //Creates empty json file with adequate naming
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void JSONStoreInFile(String filename, String store) //Writes json content as string to file
    {
        try {
            FileWriter writer = new FileWriter(mainClassAccess.getDataFolder().getAbsolutePath() + "/ShelfData/" + filename);
            writer.write(store);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private BufferedReader JSONGetFromFile(File file) //Gets json content from file in BufferedReader format
    {
        try {
            return new BufferedReader(new FileReader(file)); //Return buffered file contents
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //GSON Serialization functions

    private String JSONSerializeRPGShelf(RPGShelf rpgShelf) //Serializes class ArrayList to json String
    {
        return new Gson().toJson(rpgShelf);
    }

    private RPGShelf JSONDeserializeRPGShelf(BufferedReader rpgShelf) //Deserializes from BufferedReader to ArrayList.
    {
        return new Gson().fromJson(rpgShelf, RPGShelf.class);
        //TypeToken would get the type of an arraylist of RPGShelves (for the second parameter):
        //new TypeToken<ArrayList<RPGShelf>>(){}.getType()
    }

    //File data manipulation and querying using GSON

    public RPGShelf JSONGetShelf(File file) //Gets deserialized rpg shelf stored in JSON file
    {
        return JSONDeserializeRPGShelf(JSONGetFromFile(file));
    }

    public void JSONAddLibraryIfNotExists(int X, int Y, int Z, String world, List<String> content, LibraryAddCallback callback) //Adds an rpg library with specified book content to the JSON list if it didn't already exist.
    {
        //Look for possible rpg library registered with coordinates:
        String filename = SaveFileNameManager.JSONConstructFilename(world, X, Y, Z);

        boolean done = false;
        if(CreateFileIfNotExists(filename))
        {
            JSONStoreInFile(filename, JSONSerializeRPGShelf(new RPGShelf(content)));
            done = true;
        }

        if(done)
        {
            Bukkit.getScheduler().runTask(mainClassAccess, new Runnable() { //Callback to main thread
                @Override
                public void run() {
                    callback.done(true);
                }
            });
        }
        else
        {
            Bukkit.getScheduler().runTask(mainClassAccess, new Runnable() { //Callback to main thread
                @Override
                public void run() {
                    callback.done(false);
                }
            });
        }
    }

    public void JSONRemoveRPGLibraryIfExists(int X, int Y, int Z, String world)
    {
        String filename = SaveFileNameManager.JSONConstructFilename(world, X, Y, Z);
        new File(mainClassAccess.getDataFolder().getAbsolutePath() + "/ShelfData/" + filename).delete();
    }

    public void JSONGetPagesIfRPGLibraryExists(int X, int Y, int Z, String world, LibraryReadCallback callback)
    {
        //Look for possible rpg library registered with coordinates:
        String filename = SaveFileNameManager.JSONConstructFilename(world, X, Y, Z);
        File file = new File(mainClassAccess.getDataFolder().getAbsolutePath() + "/ShelfData/" + filename);
        if(file.exists()) {
            //Get book data from file:
            RPGShelf shelf = JSONGetShelf(file);
            Bukkit.getScheduler().runTask(mainClassAccess, new Runnable() { //Callback to main thread
                @Override
                public void run() {
                    callback.onQueryDone(shelf.bookContents);
                }
            });
        }
    }
}