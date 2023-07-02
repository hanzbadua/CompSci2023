import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class which helps with handling data of a streaming actor (required methods/fields etc.)
 */
public abstract class StreamingActor extends GameActor
{
    // define in inheriting classes
    // this is a method because java doesn't have properties
    public abstract boolean isGlideable();
    
    // assign via constructor
    public final int speed;
    
    // remember to make a constructor that calls super() in inheriting classes for compilation purposes
    public StreamingActor(int speed) {
        super();
        this.speed = speed;
    }
    
    // if reaching appropriate edge, destruct
    // returns true upon destruct, false otherwise
    protected boolean checkForDestruction() {     
        World w = getWorld();
        // negative
        if (speed < 0) {
            // left edge
            if (getX() == 0) {
                w.removeObject(this);
                return true;
            }
        // positive
        } else {
            // right edge
            if (getX() == FroggerWorld.WIDTH) {
               w.removeObject(this);
               return true;
            }
        }
        
        return false;
    }
    
    @Override
    public void act()
    {
        setLocation(getX() + speed, getY());
        checkForDestruction();
    }
}
