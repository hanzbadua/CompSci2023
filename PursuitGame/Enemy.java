import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Actor class representing the main enemy of the game
 * Collision with the player causes in a life loss
 * Collision when player lives is 1 --> death/game over
 */
public class Enemy extends Actor
{
    // ref to class image
    private GreenfootImage img = getImage();
    
    // called every frame, random movement stuff
    public void act()
    {
        // In the line below, we get our current world object with the same type as our game world
        // Ensuring the retrieved object type is SpaceWorld and not just World is crucial
        // so we can access the player object reference (which is a feature of our own class) and store it
        // this will CRASH if Enemy is instantiated in a world that isn't SpaceWorld
        // don't do that I guess!
        SpaceWorld current = getWorldOfType(SpaceWorld.class);
        PlayerChar player = current.player;
        Difficulty diff = current.getDifficulty();
        
        // affects how often the enemy rotation updates to turn towards the player
        // lower value --> smaller random range --> turn more --> harder
        int rotationUpdate = 260;
        
        // change move speed & rotation update based on difficulty
        if (diff == Difficulty.EASY) {
            move(1 + Greenfoot.getRandomNumber(2)); // 0-1, so basically switching between move 1 and move 2
            // easy has the same rotationUpdate as hard or else it would literally be too easy
            rotationUpdate = 220;
        }
        else if (diff == Difficulty.NORMAL) {
            move(1 + Greenfoot.getRandomNumber(2)); // same as easy
            rotationUpdate = 260;
        }
        else { // hard
            move(1 + Greenfoot.getRandomNumber(3)); // 0-2, switching between move 1 and move 3
            rotationUpdate = 220;
        }
        // ^ remember the max for getRandomNumber() is exclusive, so the number below the arg max is the actual possible max
        
        // chance to change rotation to player, when random number from range 0...rotationUpdate - 1 == 0
        if (Greenfoot.getRandomNumber(rotationUpdate) == 0)
            turnTowards(player.getX(), player.getY());
        
        if (isAtEdge()) 
            turn(180); // bounce when hitting the edge
    }
    
    // called upon instantiation
    public Enemy()
    {
        // set initial size to 66.67%
        img.scale((int)(img.getWidth() / 1.5), (int)(img.getHeight() / 1.5));
    }
}
