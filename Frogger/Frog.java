import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Represents the main player-controlled character (frog)
 */
public class Frog extends Actor
{   
    // image cache
    private GreenfootImage frogimg = null;
    private GreenfootImage frogimgdead = null;
    
    // stores current row/tile the Frog is located in
    public int row = 0;
    public int tile = 0; 

    // used to only register one keypress at a time (prevents holding down controls to move repeatedly, unintended)
    // see controls() method
    // note: uninitialized booleans in a boolean array have a default value of false
    private boolean[] keyLock = new boolean[5];
    
    // all tilehighlighter objects
    // index 0 = up
    // index 1 = left
    // index 2 = right
    // private TileHighlighter[] tilehls = new TileHighlighter[3];

    // self-explanatory
    private boolean isDead = false;
    
    public Frog() {
        // init image cache
        frogimg = new GreenfootImage(".\\images\\Frog0.png");
        frogimg.scale(frogimg.getWidth() * 4, frogimg.getHeight() * 4);
        
        frogimgdead = new GreenfootImage(".\\images\\FrogDead.png");
        frogimgdead.scale(frogimgdead.getWidth() * 4, frogimgdead.getHeight() * 4);
        
        // set image
        setImage(frogimg);
        
        // instantiate tilehiglighter objects
        /*
        for (int i = 0; i < tilehls.length; i++) {
            tilehls[i] = new TileHighlighter();
        }
        */
    }
    
    // set the frog position to a specific terrain row + tile
    // terrainRow: 0...7
    // tileIndex: 0...14
    public void setToTile(int terrainRow, int tileIndex) {
        tile = tileIndex;
        row = terrainRow;
    
        FroggerWorld w = getWorldOfType(FroggerWorld.class);
        
        Tile target = w.getTile(terrainRow, tileIndex);
        
        setLocation(target.getX(), target.getY());
    }
    
    // move up one tile, uses setToTile()
    private void moveUp() {
        FroggerWorld w = getWorldOfType(FroggerWorld.class);    
        // if moving up when on highest row, move to next level
        if (row == 0) {
            w.unloadCurrentLevel(w.ll.getNextLevel());
            return;

        }
        
        setRotation(0);    
        
        if (isGliding()) { // if we are gliding we know retrieving intersecting streamingactor will never be null
            // StreamingActor detection - technically hacky, but really there's no super correct way to do thats easy
            // one way to do it would be to index all streamingactors and move via index, but thats a lot of work as streamingactors,
            // unlike tiles, are constantly created/removed, and indexes would have to be reassigned all the time
            // not to mention proper index->index movement code
            
            // World.getObjectsAt() gets all objects of type T found at the specified position
            // if the list isn't empty, you know theres an object of that type at that position
            // y - 85 = approximate streamingactor above (85 is tile side value)
            StreamingActor refsa = (StreamingActor) getOneIntersectingObject(StreamingActor.class);
            List<StreamingActor> a = w.getObjectsAt(refsa.getX(), refsa.getY() - 85, StreamingActor.class);
            
            if (!a.isEmpty()) {
                row--;
                setLocation(refsa.getX(), refsa.getY() - 85);
                return;
            }
            
            // Tile detection - no need to null check, we know there are tiles everywhere
            Tile refTile = (Tile) getOneIntersectingObject(Tile.class);
            
            // if tile is closed don't allow movement
            if (w.getTile(refTile.row - 1, refTile.id).isClosed()) {
                return;
            }
            
            setToTile(refTile.row - 1, refTile.id);
        } else {            
            if (w.getTile(row - 1, tile).isClosed()) {
                return;
            }
            
            setToTile(row - 1, tile);
        }
    }
    
    private void moveDown() {
        // can't move down if already on the lowest row
        if (row == 7) // index 7, 8th row (last)
            return;
            
        setRotation(180);
        FroggerWorld w = getWorldOfType(FroggerWorld.class);
        
        if (isGliding()) { 
            StreamingActor refsa = (StreamingActor) getOneIntersectingObject(StreamingActor.class);
            List<StreamingActor> a = w.getObjectsAt(refsa.getX(), refsa.getY() + 85, StreamingActor.class);
            
            if (!a.isEmpty()) {
                row++;
                setLocation(refsa.getX(), refsa.getY() + 85);
                return;
            }
            
            
            Tile refTile = (Tile) getOneIntersectingObject(Tile.class);
            
            // if tile is closed don't allow movement
            if (w.getTile(refTile.row + 1, refTile.id).isClosed()) {
                return;
            }
            
            setToTile(refTile.row + 1, refTile.id);
        } else {
            if (w.getTile(row + 1, tile).isClosed()) {
                return;
            }
            
            setToTile(row + 1, tile);
        }
    }
    
    private void moveLeft() {
        // stop left/right movement when gliding
        if (isGliding())    
            return;
        
        // can't move left if already on the left edge
        if (tile == 0)
            return;
        
        setRotation(270);
        FroggerWorld w = getWorldOfType(FroggerWorld.class);
        
        if (w.getTile(row, tile - 1).isClosed()) {
            return;
        }
            
        setToTile(row, tile - 1);
    }
    
    private void moveRight() {
        if (isGliding())    
            return;
        
        // can't move right if already on the right edge
        // 14 is the highest possible tile index (15th tile)
        if (tile == 14)
            return;
            
        setRotation(90);    
        FroggerWorld w = getWorldOfType(FroggerWorld.class);
        
        if (w.getTile(row, tile + 1).isClosed()) {
            return;
        }
        
        setToTile(row, tile + 1);
    }
    
    // registers player control logic by binding keybinds to their proper actions
    private void controls() {
        if (isDead)
            return;
        // () -> expression is lambda expression syntax
        registerKeypressAction(0, "space", () -> moveUp());
        registerKeypressAction(1, "up", () -> moveUp());
        registerKeypressAction(2, "left", () -> moveLeft());
        registerKeypressAction(3, "right", () -> moveRight()); 
        registerKeypressAction(4, "down", () -> moveDown());  
    }
    
    // handles what to do upon collision with all objects (dangerous or helpful, etc) 
    private void collisions() {
        if (isAtEdge()) {
            onDangerous();
        }
        
        StreamingActor b = (StreamingActor) getOneIntersectingObject(StreamingActor.class);
        
        if (b != null) {
            if (b.isDangerous()) {
                onDangerous();
                return;
            }
            
            if (b.isGlideable() && !isDead) {
                setLocation(getX() + b.speed, getY());
                return;
            }
        }
        
        Tile a = (Tile) getOneIntersectingObject(Tile.class);
        
        if (a != null && a.isDangerous()) {
            onDangerous();
        }
    }
    
    // called when frog collides with dangerous object
    private void onDangerous() {
        isDead = true;
        setImage(frogimgdead);
        // put frog under lilypad
        getWorld().setPaintOrder(TileHighlighter.class, Car.class, LilyPad.class, PepeDancing.class, Frog.class);
    }
    
    // ezpz function to register keypress + respective actions easily
    // keylockIndex --> index of array keyLock (see above) to use, use a different one for each registered keypress
    // key --> keypress to register, only register one keypress per action to prevent bugs obviously
    // action --> lambda expression (anonymous function) to execute upon keypress
    // (Runnable is an interface which represents a method of type void and no arguments, see FunctionalInterfaces for more info)
    private void registerKeypressAction(int keyLockIndex, String key, Runnable keypressInterface) 
    {   
        if (!Greenfoot.isKeyDown(key)) {
            keyLock[keyLockIndex] = false; 
        } else if (keyLock[keyLockIndex] == false) {
            keypressInterface.run();
            keyLock[keyLockIndex] = true;
        }
    }
    
    // updates all the tilehighlighter positions/states
    /*
    private void updateHighlighters() {
        FroggerWorld w = getWorldOfType(FroggerWorld.class);
        
        Tile refTile = (Tile) getOneIntersectingObject(Tile.class);
        int refTileRow = refTile.row;
        int refTileId = refTile.id;
        
        // upper tile
        if (refTileRow - 1 >= 0) {
            Tile t = w.terrain[refTileRow - 1].tiles[refTileId];
            tilehls[0].setLocation(t.getX(), t.getY()); 
        } else {
            tilehls[0].hide();
        }
        
        if (isGliding()) {
            tilehls[1].hide();
            tilehls[2].hide();
            return;
        }    
            
        // left tile
        if (refTileId - 1 >= 0) {
            Tile t = w.terrain[refTileRow].tiles[refTileId - 1];
            tilehls[1].setLocation(t.getX(), t.getY());
            tilehls[1].show();
        } else {
            tilehls[1].hide(); 
        }
        
        // right tile
        if (refTileId + 1 <= 14) {
            Tile t = w.terrain[refTileRow].tiles[refTileId + 1];
            tilehls[2].setLocation(t.getX(), t.getY()); 
            tilehls[2].show();
        } else {
            tilehls[2].hide(); 
        }
    } */
    
    private boolean isGliding() {
        StreamingActor a = (StreamingActor) getOneIntersectingObject(StreamingActor.class);
        return a != null && a.isGlideable();
    }
    
    @Override
    public void act()
    {
        controls();
        collisions();
        //updateHighlighters();
        
        // prompt restart if frog is dead
        if (isDead) {
            FroggerWorld w = getWorldOfType(FroggerWorld.class);
            w.showText("Press R to restart the level", 250, 25);
            
            if (Greenfoot.isKeyDown("r")) {
                isDead = false;
                setImage(frogimg);
                setToTile(7, 7);
                w.showText(null, 250, 25);
                w.setPaintOrder(TileHighlighter.class, Car.class, Frog.class, PepeDancing.class, LilyPad.class);
            }
        }
    }
    
    // set frog starting position and 
    // initialize the tilehighlighters and add them to the world
    @Override
    public void addedToWorld(World _w) {
        // initial pos
        setToTile(7, 7);
        
        // highlighter body
        /*
        FroggerWorld w = (FroggerWorld) _w;
        Tile refTile = w.terrain[row].tiles[tile];
        
        // get upper tile
        if (row - 1 >= 0) {
            Tile t = w.terrain[row - 1].tiles[tile];
            w.addObject(tilehls[0], t.getX(), t.getY());
        }
        
        // get left tile
        if (tile - 1 >= 0) {
            Tile t = w.terrain[row].tiles[tile - 1];
            w.addObject(tilehls[1], t.getX(), t.getY());
        }
        
        // get right tile
        if (tile + 1 <= 14) {
            Tile t = w.terrain[row].tiles[tile + 1];
            w.addObject(tilehls[2], t.getX(), t.getY());   
        }*/
    }
    
    private void checkForWin() {
    
    }
}
