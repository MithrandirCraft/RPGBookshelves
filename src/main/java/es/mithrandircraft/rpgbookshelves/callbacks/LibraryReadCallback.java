package es.mithrandircraft.rpgbookshelves.callbacks;

import java.util.List;

public interface LibraryReadCallback {
    void onQueryDone(List<String> pages);
}
