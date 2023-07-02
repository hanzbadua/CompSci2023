import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Invisible actor used as reference position points for enemy actors to move to
 * Works by having a collection of navnodes for each map, in a proper order
 * Moving enemy actors turn towards each navnode - upon collision, turn towards the next navnode in order
 * Until they reach the end of the nap
 */
public class Navnode extends Actor
{
    private int px = 0;
    private int py = 0;  
    
    public Navnode(int x, int y) {
        px = x;
        py = y;
        // new transparent image for collision purposes
        // probably not necessary but
        setImage(new GreenfootImage(1, 1));
        getImage().setTransparency(0);
    }
    
    // set to intended location upon being added
    @Override
    protected void addedToWorld(World w) {
        setLocation(px, py);
    }
}
