import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class representing the player-controlled "paddle" of the Breakout game
 */
public class Paddle extends Actor
{
    // all necessary class behavior belongs/is called in act()
    public void act()
    {
        controls();
    }
        
    // defines player controls
    // the paddle position is based on the mouse cursor position
    private void controls() {
        // get the current MouseInfo instance of the game
        // which is a class used to access mouse information
        MouseInfo info = Greenfoot.getMouseInfo();
        
        // is null if the mouse is outside the world/game boundary
        if (info != null)
            // set x to mouse x, y stays the same
            setLocation(info.getX(), getY());
    }
}
