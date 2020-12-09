package es.mithrandircraft.rpgbookshelves;

import es.mithrandircraft.rpgbookshelves.events.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class RPGBookshelves extends JavaPlugin {

    //Persistent memory storage manager:
    public MemoryManager mm = new MemoryManager(this);

    @Override
    public void onEnable() {

        //Config load:
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Create save directory:
        if(mm.CreateBaseSaveDirectoryIfNotExists()) System.out.println("[RPGBookshelves]: Save folder created.");

        //Event registring:
        getServer().getPluginManager().registerEvents(new BlockBreakEv(this), this);
        getServer().getPluginManager().registerEvents(new BlockBurnEv(this), this);
        getServer().getPluginManager().registerEvents(new BlockExplodeEv(this), this);
        getServer().getPluginManager().registerEvents(new BlockPistonExtendEv(this), this);
        getServer().getPluginManager().registerEvents(new EntityExplodeEv(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEv(this), this);
    }
}