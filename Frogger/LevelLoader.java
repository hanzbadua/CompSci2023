import greenfoot.*;
import java.io.*;
import java.util.Scanner;

/**
 * Loads levels from files (with extension .lvl in the levels directory properly formatted)
 * Level file format documentation soon
 * NOTE: ensure space 7,7 is a safe tile; that is prolly gonna be the starting position for each level for the frog
 */
public class LevelLoader  
{
    public String[] levelsToPlay = {
        "level1", "level2", "level3"
    };
    
    private int levelIndex = 0;
    
    public int getLevelIndex() {
        return levelIndex;
    }
    
    // get the next level to play
    public Terrain[] getNextLevel() {
        if (levelIndex >= levelsToPlay.length) {
            Greenfoot.setWorld(new WinWorld());
            return null;
        }
        Terrain[] toLoad = loadLevel(levelsToPlay[levelIndex]);
        levelIndex++;
        return toLoad;
    }
    
    // Load a level file --> deserialize the data and generate terrain accordingly
    // NOTE: the 15th tile entry per row is to specify any streamingactorgenerators
    private Terrain[] loadLevel(String levelname) {
        int tileIndex = 0; // 16 entries, from index 0 to 15 (index 15 is streamingactorgen)
        int rowIndex = 0; // 8 entries, index 0 to 7
        // exceptions; instantiate ahead of time to prevent unnecessary instantiations/copies during scanner loops
        final LevelLoadException LLEX_0 = new LevelLoadException("Invalid tile entry when loading level " + levelname + ": invalid id");
        final LevelLoadException LLEX_1 = new LevelLoadException("Failed level load: No corresponding level file for level of name " + levelname + " found in the \"levels\" directory");
        final LevelLoadException LLEX_2 = new LevelLoadException("Invalid tile entry when loading level " + levelname + ": no valid tile entry starting character [");
        
        Terrain[] retval = new Terrain[Terrain.TERRAIN_ROWS];
        Tile[] buffer = new Tile[Terrain.TILES_PER_TERRAIN];
        
        // get the proper level file to load via name (no extension, no path! just a name, make sure file is in the levels folder)
        File lvlFile = new File(".\\levels\\" + levelname + ".lvl");
        
        // Split entries using ] character
        Scanner reader;
        try {
            reader = new Scanner(lvlFile).useDelimiter("\\]");
        } catch(FileNotFoundException ex) {
            // failed level load exception: no valid level file
            throw LLEX_1;
        }
        
        // check if there is a token entry left to parse
        while (reader.hasNext()) {
            // while there is, keep building terrain based on data
            // get the token string to parse and remove all whitespace using regex (regular expression)
            String p = reader.next().replaceAll("\\s", "");
            
            // all tokens must start with [ character (enforced by if check) and end with ] (enforced by delimiter)
            // if not, throw error
            if (!p.startsWith("[")) {
                // failed level load: invalid tile entry (no starting char)
                throw LLEX_2;
            }
            
            // remove starting char for parsing purposes
            // must be escaped to avoid regex interpretation
            p = p.replaceAll("\\[", "");
            
            int ttid = 0;
            TileType tt = null;
            
            try {
                // try parsing the tile entry string as a valid tileid integer to use for Tile.getTileTypeByNumber()
                ttid = Integer.parseInt(p);
            } catch (NumberFormatException ex) { // string is not a valid int
                // id is not valid because not int
                throw LLEX_0;
            }
            
            tt = Tile.getTileTypeByNumber(ttid);
            
            // exception for id 15 because that is for the sactorgen
            if (tt == null && tileIndex != 15) {
                // id is not valid because id is out of bounds/not a proper id (see Tile.getTileTypeByNumber())
                throw LLEX_0;
            }
            
            // final entry - sactgen + prepare for next terrain row
            if (tileIndex == 15) {
                // 0 --> nothing for sactgen, invalid id --> throw err
                // (level load exceptions for sactorgen is handled in getSActGenPresetById(), not this method
                retval[rowIndex] = new Terrain(buffer, rowIndex, StreamingActorGenerator.getSActGenPresetById(ttid));
                
                // clear the buffer by reinit-ing it, should be fine as java arrays pass-by-value
                buffer = new Tile[Terrain.TILES_PER_TERRAIN];
                tileIndex = 0;
                rowIndex++;
                continue; // continue statement goes to the next iteration of the loop, skipping all other code ahead of the continue statement
            }
            
            buffer[tileIndex] = new Tile(tt, rowIndex, tileIndex);
            tileIndex++;
        }
        
        return retval;
    }
}
