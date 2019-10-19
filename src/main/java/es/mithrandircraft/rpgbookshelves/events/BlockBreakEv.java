package es.mithrandircraft.rpgbookshelves.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakEv implements Listener {

    private final es.mithrandircraft.rpgbookshelves.RPGBookshelves mainClassAccess;

    public BlockBreakEv(es.mithrandircraft.rpgbookshelves.RPGBookshelves main) { this.mainClassAccess = main; }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if(event.getBlock().getType() == Material.BOOKSHELF)
        {
            for(int i = 0; i < mainClassAccess.getConfig().getStringList("FunctionalWorlds").size(); i++) {
                if (mainClassAccess.getConfig().getStringList("FunctionalWorlds").get(i).equals(event.getBlock().getWorld().getName())) {
                    mainClassAccess.mm.JSONRemoveRPGLibraryIfExists(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ(), event.getBlock().getWorld().getName());
                    break;
                }
            }
        }
    }
}
