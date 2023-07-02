import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Highlights the tiles/spaces the frog can go on from its current position
 * Mostly useful when the frog is on a gliding streamingactor
 * Most of the code in this class is visual, the actual logic is located in the Frog class itself
 * Note: this was used for debugging purposes! if you want to see in action go to the Frog class
 * and uncomment the addedToWorld() method body, and the updateHighlighters() call in act()
 */
public class TileHighlighter extends Actor
{
    public TileHighlighter() {
        setImage(".\\images\\TileHighlightBlue.png");
    }
    
    public void show() {
        // transparency is a byte, so 0...255
        getImage().setTransparency(255);
    }
    
    public void hide() {
        getImage().setTransparency(0);
    }
    
    @Override
    public void act()
    {
    }
}
