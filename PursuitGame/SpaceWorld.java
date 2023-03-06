import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List; // use the built in list class

/**
 * IMPORTANT NOTE: 
 * This class (SpaceWorld) is the main class and is the world in where all the gameplay takes place/executes and therefore has the header comment
 * But do NOT instantiate this class to start playing, INSTEAD instantiate the MainMenuWorld class to access the main menu and play from there
 * 
 * A "pursuit game" taking place in space
 * Has a main menu and different difficulties you can choose from
 * You take control of the rocket ship, with enemies constantly pursuing you
 * Colliding with the enemies makes you lose one life (extra note: on Hard difficulty, you just die instantly upon one collision)
 * Losing all your lives means you lose the game
 * You can win the game by successfully avoiding the enemies whilst colliding powerups (yellow circles)
 * Powerups will constantly make you bigger, and in certain incremements will also increase your speed (both of which likely make the game harder but whatever)
 * Once you collect the required powerups (displayed at the top, required amount changes based on difficulty), you unlock the laser weapon and can no longer collect powerups
 * Successfully firing the laser at an enemy permanently eliminates them from the game
 * Eliminating all enemies with the laser means you win the game (!!!)
 * 
 * Controls:
 * Left/right arrow keys to turn left/right, respectively
 * Hold the "D" key to decelerate to minimum speed, useful when you become too fast and need to manuever, also useful for aiming the laser
 * Press the "F" key to fire your laser (once all required powerups are collected)
 * Once you win or lose, you can press "R" to return back to the main menu
 * And the main menu buttons need to be clicked on
 * 
 * another note:
 * should probably play on normal difficulty
 * easy difficulty is cartoonishly easy
 * and hard difficulty is... a bit frustrating
 * 
 * @author (Hanz Badua) 
 * @version (13 Feb. 2023)
 */

public class SpaceWorld extends World
{
    // static = not from an instance, don't need an instance to access <-- instead can be qualified directly from the type name and belongs to a "static" object
    // final = non-changeable after assignment
    
    // var which defines how many powerups exist at a time in the world
    private int powerupsInWorld = 5;
    // how many enemies to spawn in the world
    private int enemyCount = 2;
    
    // constants which define game width/height
    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;
    
    // game difficulty
    private Difficulty diff;
    
    // object storing player character
    public PlayerChar player;
    
    // Constructor for the SpaceWorld class
    // this is called upon instantiation
    // difficulty required as an argument! --> certain game attributes/constants change based on the difficulty
    // see the difficulty enum file comments to see what each difficulty does
    public SpaceWorld(Difficulty d)
    {    
        // Create a new world with WIDTH*HEIGHT (see constants above) cells with a cell size of 1*1 pixels
        super(WIDTH, HEIGHT, 1); 
        
        // assign the difficulty
        diff = d;
        
        int poweruplmt = 0; // <-- param for PlayerChar
        int slives = 0; // <-- and another one
        
        // change playerchar params/constants based on difficulty
        switch(diff) {
            case EASY:
                poweruplmt = 10;
                slives = 3;
                powerupsInWorld = 6;
                enemyCount = 2;
                break;
            case NORMAL:
                poweruplmt = 15;
                slives = 2;
                powerupsInWorld = 5;
                enemyCount = 3;
                break;
            case HARD:
                poweruplmt = 20;
                slives = 1;
                powerupsInWorld = 4;
                enemyCount = 4;
                break;
        }
        
        // Instantiate the player
        player = new PlayerChar(poweruplmt, slives);
        
        // add the player to the world
        addObject(player, 100, 200);
        
        // generate powerups/enemies
        randomlyGenerateInitialObjects();
    }

    // called every frame
    // handles text display/win condition checking/prompt to restart if player loses
    public void act()
    { 
        int consumed = player.getPowerupsConsumed(); // get powerups consumed by the player
        int lives = player.getLives(); // lives
        int slives = player.getStartingLives(); // starting lives
        
        // checks if the player has won, if so skip the remaining code (unnecessary if the player won)
        if (checkForWinCondition()) return;
        
        // display lives text
        if (lives != 0) 
            // if the player is on hard difficulty
            // remind them they only have one life :D
            if (diff == Difficulty.HARD) 
                showText("Don't mess up!", 150, 30);
            else // otherwise display their lives count
                showText("Lives: " + lives + "/" + slives, 150, 30);
        else { // game over --> allow going to main menu after
            showText("Game over! Press R to go to the Main Menu", 300, 30);
            // remove powerups text
            // passing null as a parameter to showText() removes the text at specified position
            showText(null, 450, 30);
            showText(null, 150, 30);
            
            // if r is pressed go to main menu
            if (Greenfoot.isKeyDown("r")) {
                // instantiate new main menu and change current world to new main menu object
                Greenfoot.setWorld(new MainMenuWorld());
            }
            return; // exit method early
        }
        
        // display powerups consumed text 
        if (consumed < player.getPowerupLimit())
            showText("Powerups consumed: " + consumed + "/" + player.getPowerupLimit(), 450, 30);
        else // if the player reached the powerups consumed limit, change the text
            showText("Press F to fire your laser!", 450, 30);
    }
    
    // handles initial random object generation
    private void randomlyGenerateInitialObjects()
    {
        // Add an amount of powerup objects to the world equal to powerupsInWorld
        for (int i = 0; i != powerupsInWorld; i++)
            addObject(new Powerup(), Greenfoot.getRandomNumber(WIDTH), Greenfoot.getRandomNumber(HEIGHT));
                
        for (int i = 0; i != enemyCount; i++)
            addObject(new Enemy(), Greenfoot.getRandomNumber(WIDTH), Greenfoot.getRandomNumber(HEIGHT));
    }
    
    // checks for win condition, if true the player wins
    // ...
    // returns true if won otherwise false
    private boolean checkForWinCondition() {
        // get all enemy objects in world as list
        List enemiesInWorld = getObjects(Enemy.class);
        // get list count
        int count = enemiesInWorld.size();
        
        // no more enemies == win
        if (count == 0) {
            showText("You won! Press R to go to the Main Menu", 300, 30);
            showText(null, 150, 30);
            showText(null, 450, 30);
            
            // if r is pressed go to main menu
            if (Greenfoot.isKeyDown("r")) {
                // instantiate new main menu and change current world to new main menu object
                Greenfoot.setWorld(new MainMenuWorld());
            }
            return true;
        } else 
            return false;
    }
    
    // get difficulty
    public Difficulty getDifficulty() {
        return diff;
    }
    
}
