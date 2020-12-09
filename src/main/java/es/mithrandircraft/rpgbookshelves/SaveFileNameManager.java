package es.mithrandircraft.rpgbookshelves;

import org.bukkit.World;

public class SaveFileNameManager {
    public static String JSONConstructFilename(String world, int X, int Y, int Z)
    {
        return world + "," + String.valueOf(X) + "," + String.valueOf(Y) + "," + String.valueOf(Z) + ".json";
    }

    /*
    //Getting filename may work somewhat like this I guess. Code isn't included since it's unnecessary, but I wanted a
    //small challenge

    public static void GetFilenameData(String filename, String world, int X, int Y, int Z)
    {
        int segment = 0;
        StringBuilder segmentRead = new StringBuilder();
        int stringPos = 0;
        while(stringPos < filename.length())
        {
            stringPos++;

            if(filename.charAt(stringPos) == ',')
            {
                switch(segment)
                {
                    case 0:
                    {
                        world = segmentRead.toString();
                    }
                    case 1:
                    {
                        X = Integer.parseInt(segmentRead.toString());
                    }
                    case 2:
                    {
                        Y = Integer.parseInt(segmentRead.toString());
                    }
                    case 3:
                    {
                        Z = Integer.parseInt(segmentRead.toString());
                    }
                }

                segment++;
                continue;
            }
            else if(filename.charAt(stringPos) == '.') break;

            segmentRead.append(filename.charAt(stringPos));
        }
    }
    */
}
