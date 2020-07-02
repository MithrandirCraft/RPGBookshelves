package es.mithrandircraft.rpgbookshelves.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockExplodeEv implements Listener {

    private final es.mithrandircraft.rpgbookshelves.RPGBookshelves mainClassAccess;

    public BlockExplodeEv(es.mithrandircraft.rpgbookshelves.RPGBookshelves main) { this.mainClassAccess = main; }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event)
    {
        for(int i = 0; i < mainClassAccess.getConfig().getStringList("FunctionalWorlds").size(); i++) {
            if (mainClassAccess.getConfig().getStringList("FunctionalWorlds").get(i).equals(event.getBlock().getWorld().getName())) {
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
