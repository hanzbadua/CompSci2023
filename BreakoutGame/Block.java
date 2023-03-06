import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Represents a block that can be destroyed/"downgraded" upon collision with a Ball
 * Grants one point upon collision with a ball
 */
public class Block extends Actor
{
    // image ref
    private GreenfootImage img = getImage();
    
    // BlockStrength.random() method returns a random value of enum BlockStrength
    // store the block blockstrength
    private BlockStrength strength = BlockStrength.random();
    
    // stores the block powerup
    // null --> no powerup
    private BlockPowerup powerup = null;
    
    // called upon instantiation
    public Block() 
    {   
        // set color based on blockstrength:
        img.setColor(strength.getColor()); // set img draw color to respective strength color
        img.fill(); // fill img with img draw color
    }
    
    // define behaviour here
    public void act()
    {
        ballCollision();
    }
    
    // defines what happens upon collision with a ball
    private void ballCollision() {
        // get colliding ball ref
        Actor ball = getOneIntersectingObject(Ball.class);
        
        // if valid do stuff
        if (ball != null) {
            // downgrade, see appropriate method in BlockStrength
            strength = strength.downgrade();
            
            // make powerup do its thing if it exists
            if (powerup != null) 
            {
                // do stuff
                powerup.applyPowerupEffectsToWorld(getWorldOfType(GameWorld.class), this);
            }
            
            // remove powerup if it exists
            powerup = null;
            
            // if applicable lower the block strength + change color appropriately, or else remove it
            if (strength != null) {
                img.setColor(strength.getColor());
                img.fill();
            }
            else {
                // null strength = removed block, so there should never be an existing block with a null strength
                getWorld().removeObject(this);
            }
        }
    }
    
    // set the block powerup and update imagery
    // make sure arg isn't null
    public void setPowerup(BlockPowerup p) {
        powerup = p;
        powerup.applyImagery(this);
    }
}
