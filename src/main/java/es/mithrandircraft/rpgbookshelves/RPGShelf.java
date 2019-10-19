package es.mithrandircraft.rpgbookshelves;

import java.util.List;

public class RPGShelf {
    public int x;
    public int y;
    public int z;
    public String w;
    public List<String> bookContents;

    public RPGShelf(int X, int Y, int Z, String world, List<String> contents){
        x = X;
        y = Y;
        z = Z;
        w = world;
        bookContents = contents;
    }
}
