import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Breakout, kinda
 * Move the paddle using the mouse, launch a ball by clicking
 * Other instructions will be displayed via text ingame (such as block column configuration)
 * 
 * The goal/win condition is to destroy all the blocks using the paddle+balls
 * However if your main (white) ball goes out of bounds by missing the paddle and hitting the bottom, you lose a life and all balls disappear
 * If you have a life remaining it is used up to summon a new ball, if you have no more lives though then you lose
 * 
 * (you start with two lives I think, can be configured in code below if it's too hard)
 * 
 * Your main ball vertical speed is based on your score, your score increments for every ball/block collision (well not exactly 1:1, see
 * Horizontal speed is based on the ball colliding with the paddle, the further away from the paddle center = the faster, respecting direction
 * Blocks have different strengths, the hierarchy being red->orange->green blocks
 * Hitting a block with a ball causes the block to downgrade one strength, however if the block is already at the lowest strength (green)
 * The block is destroyed instead and disappears
 * 
 * Blocks also contain different powerups, consumed upon collision and corresponding to images/shapes on the blocks
 * Rectangle = increases paddle size
 * Single circle = adds a yellow/"powerup" ball; powerup balls will NOT cause you to lose a life when it goes out of bounds, and will instead just disappear (for game balance purposes)
 * Powerup balls also have a fixed, unchangeable speed for once again, game balance purposes (high score + multiple balls = too fast and too much balls and you will lose)
 * Double circle = gives you an extra life
 * 
 * Quirks: No horizontal speed (occurs when ball hits center of paddle) = vertical speed is temporarily faster as a difficulty compensation
 * And probably more that I forgot about
 * 
 * Bugs: Colliding with two blocks at once might only increment score by one
 * Certain block corner collision angles might make the ball goofy, or something like that
 * 
 * And I think that's it, who knows
 * 
 * @author (Hanz Badua) 
 * @version (28 February 2023)
 */
public class GameWorld extends World
{
    // Constants for game width/height and maybe other game stuff
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int BASE_YSPEED = 4; // base yspeed every ball is guaranteed to have
    
    // default amounts of rows/columns of blocks
    private static final int DEFAULT_XBLOCK_ITERATIONS = 13;
    private static final int DEFAULT_YBLOCK_ITERATIONS = 5;
    
    // yblock generation offset, configurable in menu (see menu())
    // offsets how many columns of blocks there are relative to DEFAULT_YBLOCK_ITERATIONS
    private int yblockGenerationOffset = 0;
    
    // ball yspeed increases by 1 for every certain score interval, the interval itself being countdownToYspeedIncrease
    // and the upcoming interval afterwards being speedCalculationThreshold
    
    // (see Ball.speedCalculation() for more info)
    // this number itself increments by 2 every time speed increases by 1
    public int speedCalculationThreshold = 5;
    
    // score required to next yspeed increment
    public int countdownToYspeedIncrease = 3;
    
    // current y speed of all balls
    public int globalYspeed = BASE_YSPEED;
    
    // max powerups that can exist
    // decrements by 1 every single time a new powerup block is created
    // if == 0 then no more powerup blocks can be made
    private int powerupsRemaining = 18;
    // used to prevent powerup blocks from being right beside each other
    private int powerupCreationBuffer = 0;   
    
    // true if currently in "menu" mode, false otherwise
    private boolean menu = true;
    
    // if true allow the restart keybind
    public boolean promptingRestart = false;
    
    // stores ref to the paddle object the player will control
    public Paddle player;
    
    // score
    public int score = 0;
    
    // how much lives the player has (starting at 2) 
    public int lives = 2;
    
    // if true, enable debug features
    private boolean debug = false;
    
    // used to ensure keys only do their certain functions when pressed individually, not being called constantly whilst key is held
    private boolean keyLock = false;
    private boolean keyLock0 = false; // theres prolly a better way but ugh
    private boolean keyLock1 = false;
    
    // Constructor - called upon class instantiation
    public GameWorld()
    {    
        // set world width/height/cell size
        super(WIDTH, HEIGHT, 1); 
        
        // ensure game speed is normal (range is 0...100)
        Greenfoot.setSpeed(50);
        
        // instantiate the player (type Paddle)
        player = new Paddle();
        
        // add the paddle to the world
        addObject(player, 400, 500);
        
        // add initial ball
        addObject(new Ball(true, false, -GameWorld.BASE_YSPEED), player.getX(), player.getY() - player.getImage().getHeight());
        
        // generate blocks
        generateBlocks();
    }
    
    // behavior is stored here
    public void act() {
        // if menu do menu stuff
        if (menu) menu();
        
        // restart keybind
        if (Greenfoot.isKeyDown("r") && promptingRestart) {
            Greenfoot.setWorld(new GameWorld());
        }
        
        // display score text
        showText("Score: " + score, 100, 550);
        
        // display lives text
        showText("Lives: " + lives, 570, 550);
        
        // see appropriate method below
        debugModeStuff();
    }
    
    // generates the blocks
    private void generateBlocks() {
        // x = x iterations, y = y iterations
        // in this case 13x * 6y blocks
        // note: make sure values being used in conditions in for loops below aren't negative, or else the loop
        // will go on forever and greenfoot will freeze and more
        for (int x = 0; x != 13; x++)
            for (int y = 0; y != 6 + yblockGenerationOffset; y++) {
                Block b = new Block();
                // spacings/offsets between each new block
                // basically for x direction this says: in the x, spawn a block every 61 pixels and shift all blocks 27 to the left
                // in the y: spawn a block every 16 pixels and shift all blocks 75 down
                addObject(b, ((x+1)*61) - 27, (y+1)*16 + 75);
                // only spawn powerup if: there's enough space between blocks, passes rng test, and if we're still allowed "remaining" powerups to spawn
                if (powerupsRemaining > 0 && Greenfoot.getRandomNumber(3) == 0 && powerupCreationBuffer == 0) {
                    b.setPowerup(BlockPowerup.random());
                    powerupsRemaining--;
                    // buffer of 4 --> the next soonest powerup that can appear is on the 4th block away from the current powerup block
                    // (blocks are ordered from up->down then left->right, going right by 1 and back up once hitting the bottom block)
                    powerupCreationBuffer = 4;
                }
                
                // decrement the powerup creation buffer for every block created
                if (powerupCreationBuffer > 0) {
                    powerupCreationBuffer--;
                }
            }
    }
    
    // allows enabling debug mode by pressing q+p together, and handles all debug code
    // debug mode = just extra stuff that helps with developing
    private void debugModeStuff() {
        // press q+p same time to enable debug mode until game reset
        if (Greenfoot.isKeyDown("q") && Greenfoot.isKeyDown("p"))
            debug = true;
            
        if (debug) {
            // display exact yspeed
            showText("Yspeed: " + globalYspeed, 200, 550);
            
            // press c key to spawn a new ball (not held, powerup) at paddle
            if (!Greenfoot.isKeyDown("c")) {
                keyLock = false;
            }
            else if (!keyLock) { // --> and c key is down if we get here
                addObject(new Ball(false, true, -GameWorld.BASE_YSPEED), player.getX(), player.getY());
                keyLock = true;
            }
        }
    }
    
    // menu stuff such as appropriate keybinds/text
    private void menu() {
        showText("Click to start - move the mouse to move the paddle", 400, 300);
        showText("Press W to reroll your blocks", 400, 325);
        showText("Press Q/E to remove/add block columns", 400, 350);
        showText("Block column offset: " + yblockGenerationOffset + " | NOTE: min/max is -4/+4", 400, 375);
        
        
        // allow changing amount of block columns, and rerolling blocks
        // keylock pattern is repetitive but it works, ill make it look neater one day
        if (!Greenfoot.isKeyDown("W")) { // reroll blocks
            keyLock = false;
        } else if (!keyLock) {
            // remove all blocks then regen them
            removeObjects(getObjects(Block.class));
            powerupsRemaining = 18 + (2 * yblockGenerationOffset); // powerups remaining is dependent on yblockGenerationOffset
            generateBlocks();
            keyLock = true;
        }
        
        if (!Greenfoot.isKeyDown("Q")) { // remove block column
            keyLock0 = false;
        } else if (!keyLock0) {
            // remove all blocks then regen them
            removeObjects(getObjects(Block.class));
            if (yblockGenerationOffset > -4) 
                yblockGenerationOffset -= 1;
            powerupsRemaining = 18 + (2 * yblockGenerationOffset);
            generateBlocks();
            keyLock0 = true;
        }
        
        if (!Greenfoot.isKeyDown("E")) { // add block column
            keyLock1 = false;
        } else if (!keyLock1) {
            // remove all blocks then regen them
            removeObjects(getObjects(Block.class));
            if (yblockGenerationOffset < 4)
                yblockGenerationOffset += 1;
            powerupsRemaining = 18 + (2 * yblockGenerationOffset);
            generateBlocks();
            keyLock1 = true;
        }
        
        // on click, disable menu stuff
        if (Greenfoot.mouseClicked(null)) {
            // null text = remove text at position
            showText(null, 400, 300);
            showText(null, 400, 325);
            showText(null, 400, 350);
            showText(null, 400, 375);
            menu = false;
        }
    }
}
