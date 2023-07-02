import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Lunar Lander
 * Hopefully physics work properly
 * Remember: positive y = down, negative y = up
 * Also not going to lie, I kinda of just guessed how to use trig...
 */
public class LunarLander extends SmoothMover
{
    // hopefully this value is good enough
    private static final double GRAVITY = 0.15;
    
    // image cache
    private GreenfootImage idleimg = null;
    private GreenfootImage accelimg = null;
    
    // self-explanatory
    private double xvel = 0.0;
    private double yvel = 0.0;
    private boolean dead = false;
    private boolean landed = false;
    
    public LunarLander() {
        // init image cache
        idleimg = new GreenfootImage(".\\images\\Lander0.png");
        accelimg = new GreenfootImage(".\\images\\Lander1.png");
        
        setImage(idleimg);
    }
    
    @Override
    public void act()
    {
        // handles restart prompts
        if (dead) {
            World w = getWorld();
            getImage().setTransparency(0);
            w.showText("Press R to restart", LunarLanderWorld.WIDTH/2, LunarLanderWorld.HEIGHT/2);
            if (Greenfoot.isKeyDown("R")) {
                w.showText(null, LunarLanderWorld.WIDTH/2, LunarLanderWorld.HEIGHT/2);
                w.addObject(new LunarLander(), LunarLanderWorld.WIDTH/2, (LunarLanderWorld.HEIGHT/8)*2);
                w.removeObject(this);
                return;
            }
        }
        
        // hitting edge causes death
        if (isAtEdge()) {
            dead = true;
            return;
        }
        
        controls();
        physics();
    }
    
    // Handles all physics such as gravity, platform collision
    private void physics() {  
        // Platform collision
        Platform p = (Platform) getOneIntersectingObject(Platform.class);
        if (p != null) {
            if (getY() < p.getY() + 10) {
                if (yvel >= 2.0) {
                    dead = true;
                }
            } else if (yvel <= -2.0) {
                dead = true;
            }
            
            if (Greenfoot.isKeyDown("space")) {
                setLocation(getX() + xvel, getY() + yvel);
            }
            
            landed = true;
            return;
        } else {
            yvel += GRAVITY; // accel due to gravity
        }
        
        setLocation(getX() + xvel, getY() + yvel);
        landed = false;
    }
    
    // Handles all controls such as turning and thrust
    private void controls() {
        setIdleImg();
        
        // deceleration/acceleration
        if (Greenfoot.isKeyDown("space")) {
            double cos = Math.cos(Math.toRadians(getRotation()));
            
            // up/down accel
            // accel slower if already going up,
            // faster otherwise
            // makes less sense but feels better
            if (yvel <= 0) {
                yvel -= 0.36 * cos;
            } else {
                yvel -= 0.32 * cos;
            } 
                
            // left/right accel 
            xvel += 0.2 * (Math.sin(Math.toRadians(getRotation())) / 2);
            
            setAccelImg();
        }
        else {
            // if space is not pressed and currently going up 
            // approach accel=0 rapidly
            if (yvel <= 0) {
                yvel /= 1.08;
            } 
            
            // same thing for xvel but it happens any time space is not being pressed
            // although to a lesser extent
            xvel /= 1.008;
        }
    
        
        if (Greenfoot.isKeyDown("left") && landed == false) {
            turn(-2);
        }
        
        if (Greenfoot.isKeyDown("right") && landed == false) {
            turn(2);
        }
    }
    
    // self-explanatory
    private void setIdleImg() {
        setImage(idleimg);
    }
    
    private void setAccelImg() {
        setImage(accelimg);
    }
}
