package es.mithrandircraft.rpgbookshelves.events;

import es.mithrandircraft.rpgbookshelves.BookUtil;
import es.mithrandircraft.rpgbookshelves.callbacks.LibraryAddCallback;
import es.mithrandircraft.rpgbookshelves.callbacks.LibraryReadCallback;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class PlayerInteractEv implements Listener {

    private final es.mithrandircraft.rpgbookshelves.RPGBookshelves mainClassAccess;

    public PlayerInteractEv(es.mithrandircraft.rpgbookshelves.RPGBookshelves main) { this.mainClassAccess = main; }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event){
        if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.BOOKSHELF) { //Check if bookshelf was clicked
                if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK) { //Check if the player is holding a written book
                    for(int i = 0; i < mainClassAccess.getConfig().getStringList("FunctionalWorlds").size(); i++) {
                        if (mainClassAccess.getConfig().getStringList("FunctionalWorlds").get(i).equals(event.getClickedBlock().getWorld().getName())) {
                            if (event.getPlayer().hasPermission("rpgbookshelf.store")) { //Player has permission
                                if(event.getPlayer().getGameMode() != GameMode.CREATIVE) { //Player isn't in creative mode (prevents bug: bookshelf being created but also instantly manually destroyed in creative mode without deletion from memory)
                                    //create rpg bookshelf if it isn't already registered:
                                    final BookMeta bookMeta = (BookMeta) event.getPlayer().getInventory().getItemInMainHand().getItemMeta();
                                    Bukkit.getScheduler().runTaskAsynchronously(mainClassAccess, () -> mainClassAccess.mm.JSONAddLibraryIfNotExists(event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ(), event.getClickedBlock().getWorld().getName(), bookMeta.getPages(), new LibraryAddCallback() {
                                        @Override
                                        public void onAdded() {
                                            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_PAINTING_PLACE, 1, -5);
                                        }
                                    }));
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } else if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.BOOKSHELF) {//Check if bookshelf was clicked
                for (int i = 0; i < mainClassAccess.getConfig().getStringList("FunctionalWorlds").size(); i++) {
                    if (mainClassAccess.getConfig().getStringList("FunctionalWorlds").get(i).equals(event.getClickedBlock().getWorld().getName())) {
                        if (event.getPlayer().hasPermission("rpgbookshelf.read")) {
                            Bukkit.getScheduler().runTaskAsynchronously(mainClassAccess, () -> mainClassAccess.mm.JSONGetPagesIfRPGLibraryExists(event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ(), event.getClickedBlock().getWorld().getName(), new LibraryReadCallback(){
                                @Override
                                public void onQueryDone(List<String> pages){
                                    //Display book with it's content to a player:
                                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                                    BookMeta bookmeta = (BookMeta) book.getItemMeta();
                                    bookmeta.setPages(pages);
                                    book.setItemMeta(bookmeta);
                                    BookUtil.openBook(book, event.getPlayer());
                                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_PAINTING_BREAK, 1, -5);
                                }
                            }));
                        }
                        break;
                    }
                }
            }
        }
    }
}

