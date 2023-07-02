import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Shown if the player wins (beats all levels)
 */
public class WinWorld extends World
{
    public WinWorld()
    {    
        super(FroggerWorld.WIDTH, FroggerWorld.HEIGHT, 1); 
        showText("You won!", getWidth()/2, getHeight()/2);
    }
}
