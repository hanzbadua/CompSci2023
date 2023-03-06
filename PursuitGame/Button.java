import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class that represents a main menu button
 */
public class Button extends Actor
{
    // image ref
    private GreenfootImage img = getImage();
    
    // store button xy position values
    private int xpos = 0;
    private int ypos = 0;
    
    // called upon instantiation
    // parameters represent position to place the button
    public Button(int x, int y) {
        xpos = x;
        ypos = y;
        setLocation(x, y);
        
        // set button size
        img.scale(200, 50);
    }
    
    // display button text, call this after instantiating button+adding to world
    // can't be done in constructor or else you will get a null reference since text is based on the world not the actor
    // so getWorld() returns null if the actor is not in a world
    public void displayText(String text) {
        getWorld().showText(text, xpos, ypos);
    }
    
    // the methods below are to expose the values of xpos/ypos while preventing them from being changed from outside the class
    // expectant is in the method names as these are the expected positions for the buttons, from the constructor arguments
    // and the Actor class implements the method names getX and getY already, and those functions return
    // the actual positions once in a world
    public int getExpectantX() {
        return xpos;
    }
    
    public int getExpectantY() {
        return ypos;
    }
}
