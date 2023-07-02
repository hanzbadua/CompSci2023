import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.*;
import java.util.concurrent.*;

/**
 * Up-down-left-right arrow keys to move, R to restart if required
 * See test.lvl to see basic level template
 * The right-most tile entry per row in the level file format
 * is NOT a tile, its sActGen (see proper ids in Tile.getTileTypeByNumber(), and StreamingActorGenerator.getSActGenPresetById())
 * 
 * Levels that play are ordered in LevelLoader.levelsToPlay
 * 
 * Sprites from:
 * https://opengameart.org/content/top-down-view-cars-and-trucks-racing-sprites
 * https://pixelartmaker.com/art/c3adfe62554ddcc
 * 
 * @author (Hanz Badua)
 * @version (15 May 2023)
 */
public class FroggerWorld extends World
{
    // At this resolution (720p), there should be 15 tiles per terrain row, and 8 terrain rows total
    // with each tile being 85*85
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    
    // executor for stuff that needs to run on time-based intervals (such as sActGen spawns)
    public ScheduledExecutorService scheduledExec;
    
    // player
    public Frog frog;
    
    // all terrain rows
    public Terrain[] terrain;
    
    // level loader instance
    public LevelLoader ll;
    
    // game starts here!
    public FroggerWorld()
    {    
        // set res, unbounded world
        super(WIDTH, HEIGHT, 1, false); 
        
        // ensure speed is 50
        Greenfoot.setSpeed(50);
        
        // sets what order actors should appear first, if they are overlaying
        // highest priority = first listed
        // next priority = anything listed later
        // last priority = anything not listed
        setPaintOrder(TileHighlighter.class, Car.class, Frog.class, PepeDancing.class, LilyPad.class);
        
        // init terrain array
        terrain = new Terrain[Terrain.TERRAIN_ROWS];
        
        // instantiate our executor (we will use a corepoolsize of one for now)
        scheduledExec = Executors.newScheduledThreadPool(1);
        
        ll = new LevelLoader();
        
        terrain = ll.getNextLevel();
        for (int i = 0; i != Terrain.TERRAIN_ROWS; i++) {
            terrain[i].addToWorld(this);
        }
        
        // row 7 tile 7 = should be last row, middle tile
        frog = new Frog();
        addObject(frog, 0, 0);
        
        // Start instantly to prevent executor service timing issues at the start
        Greenfoot.start();
    }
    
    // get a tile based on row and tile index
    // no bound checks, use properly
    public Tile getTile(int _r, int _t) {
        return terrain[_r].tiles[_t];
    }
    
    // properly unloads the current level (calling cancel on executors, etc.)
    // next level needs to be loaded right after, hence the argument
    public void unloadCurrentLevel(Terrain[] nextLevel) {
        if (nextLevel == null) {
            return;
        }
        
        for (Terrain t: terrain) {
            if (t.sActGen != null) {
                t.sActGen.stop();
                removeObject(t.sActGen);
                t.sActGen = null;
            }
            
            for (GameActor a: getObjects(GameActor.class)) {
                if (a instanceof PepeDancing b) {
                    b.stopTimer();
                    removeObject(b);
                }
                removeObject(a);
                a = null;
            }
        }
        
        terrain = nextLevel;
        for (int i = 0; i != Terrain.TERRAIN_ROWS; i++) {
            terrain[i].addToWorld(this);
        }
        
        // uhh this will cause crashes - the proper way to do this if we need to is "queue" the frog removal, after all frog methods are done running
        // for the current frame
        //removeObject(frog);
        //frog = new Frog();
        //addObject(frog, 0, 0);
        
        frog.setToTile(7, 7);
        
    }
    
    // ???
    private boolean hasWon(LevelLoader ll) {
        return ll.getLevelIndex() >= ll.levelsToPlay.length;
    }
}
