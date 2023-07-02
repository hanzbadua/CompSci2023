import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Represents a row in the game world, consisting of varying randomly-generated obstacles & valid terrain for the frog to jump on (represented as Tiles)
 */
public class Terrain
{
    public static final int TILES_PER_TERRAIN = 15; // 0...14
    public static final int TERRAIN_ROWS = 8; // 0...7
    public static final int TILE_SQUARE_SIDE = 85;
    
    // contains all the tiles per terrain object
    public Tile[] tiles = new Tile[TILES_PER_TERRAIN]; 
    
    // set the streaming actor generator (ex: cars for roads) if necessary
    public StreamingActorGenerator sActGen = null;
    
    // y position is row*(TILE_SQUARE_SIDE) and a slight offset to leave space for score --> with the exception of row=0, see below
    public int y = 0;
    
    // game has 8 rows, so specify 0..7
    public Terrain(Tile[] tilesArg, int row, StreamingActorGenerator sActGen) {
        this.sActGen = sActGen;
        
        if (row == 0) {  
            y = TILE_SQUARE_SIDE/2;
        }
        else { 
            y = TILE_SQUARE_SIDE/2 + row*TILE_SQUARE_SIDE;
        }
        
        // slight y offset (hardcoded) to leave space for score and game information
        y += 40;
        
        // remember to count at 0
        for (int i = 0; i != TILES_PER_TERRAIN; i++) {
            tiles[i] = tilesArg[i];
        }
    }

    // add all tiles (and sActGen if possible) from this terrain object to the world
    public void addToWorld(World w) {
        int x = (TILE_SQUARE_SIDE+4)/2;
        for (Tile t: tiles) {
            w.addObject(t, x, y);
            
            x += TILE_SQUARE_SIDE;
        }
        
        if (sActGen != null) {
            // negative speed = right to left, left to right otherwise
            if (sActGen.speed < 0) {
                w.addObject(sActGen, FroggerWorld.WIDTH, y);
            } else {
                w.addObject(sActGen, 0, y);
            }
        }
    }
}
