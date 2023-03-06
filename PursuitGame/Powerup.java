import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class representing the "Powerup" actor
 * When colliding with the PlayerChar actor, the powerup is consumed and some PlayerChar attributes change (such as speed/size)
 * (this behavior is defined in the PlayerChar.powerupInteraction() method)
 */
public class Powerup extends Actor
{
    // ref to class image
    private GreenfootImage img = getImage();
    
    // only stuff in act is to make the powerups move randomly
    public void act()
    {
        if (Greenfoot.getRandomNumber(9) == 0) 
            turn(Greenfoot.getRandomNumber(25));
        
        move(2);
        
        if (isAtEdge()) 
            turn(180); // also bounce when hitting the edge
    }
    
    // constructor, called upon instantiation
    public Powerup()
    {
        // set initial size to half
        img.scale(img.getWidth() / 2 , img.getHeight() / 2);
    }
}
