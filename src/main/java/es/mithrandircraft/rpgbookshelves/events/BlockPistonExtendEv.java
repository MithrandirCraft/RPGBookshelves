package es.mithrandircraft.rpgbookshelves.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockPistonExtendEv implements Listener {

    private final es.mithrandircraft.rpgbookshelves.RPGBookshelves mainClassAccess;

    public BlockPistonExtendEv(es.mithrandircraft.rpgbookshelves.RPGBookshelves main) { this.mainClassAccess = main; }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event)
    {
        for(int i = 0; i < mainClassAccess.getConfig().getStringList("FunctionalWorlds").size(); i++) {
            if (mainClassAccess.getConfig().getStringList("FunctionalWorlds").get(i).equals(event.getBlock().getWorld().getName())) {
                List<Block> pushedBlocks = new ArrayList<Block>(event.getBlocks());
                for(Block block : pushedBlocks)
                {
                    if(block.getType() == Material.BOOKSHELF) mainClassAccess.mm.JSONRemoveRPGLibraryIfExists(block.getX(), block.getY(), block.getZ(), event.getBlock().getWorld().getName());
                }
                break;
            }
        }
    }
}
