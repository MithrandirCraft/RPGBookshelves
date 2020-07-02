package es.mithrandircraft.rpgbookshelves.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityExplodeEv implements Listener {

    private final es.mithrandircraft.rpgbookshelves.RPGBookshelves mainClassAccess;

    public EntityExplodeEv(es.mithrandircraft.rpgbookshelves.RPGBookshelves main) { this.mainClassAccess = main; }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event)
    {
        for(int i = 0; i < mainClassAccess.getConfig().getStringList("FunctionalWorlds").size(); i++) {
            if (mainClassAccess.getConfig().getStringList("FunctionalWorlds").get(i).equals(event.getLocation().getWorld().getName())) {
                List<Block> blocks = new ArrayList<>(event.blockList());
                for(Block block : blocks)
                {
                    if(block.getType() == Material.BOOKSHELF)
                    {
                        Bukkit.getScheduler().runTaskAsynchronously(mainClassAccess, () -> mainClassAccess.mm.JSONRemoveRPGLibraryIfExists(block.getX(), block.getY(), block.getZ(), block.getWorld().getName()));
                    }
                }
                break;
            }
        }
    }
}