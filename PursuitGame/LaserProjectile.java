import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Actor that represents a laser projectile, which the player actor can fire at enemies to eliminate them
 * (the player can only fire them after collecting all powerups)
 */
public class LaserProjectile extends Actor
{
    // ref to actor img
    private GreenfootImage img = getImage();
    
    // constructor, called upon instantiation
    public LaserProjectile() {
        // set size to half 
        img.scale(img.getWidth() / 2, img.getHeight() / 2);
    }
    
    // called every frame, handles laser interactions with enemies
    public void act()
    {
        move(10);
        
        // check if colliding with enemy
        Actor enemy = getOneIntersectingObject(Enemy.class);
        World current = getWorld(); // ref to current world
        
        // if so do stuff
        if (enemy != null) {
            // remove enemy and projectile
            current.removeObject(enemy);
            current.removeObject(this);
        } else if (isAtEdge()) { // remove projectile if at edge
            current.removeObject(this);
        }
    }
}
