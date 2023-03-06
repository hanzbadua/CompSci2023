import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math; // math stuff
import java.util.List; // list type

/**
 * Class which represents a ball bouncing around the game world, between the paddle and blocks and the edges etc.
 */
public class Ball extends Actor
{
    // is true if the ball is being held by a paddle, false otherwise
    private boolean held = true; 
    
    // is true if ball is spawned by powerup, false otherwise
    public boolean powerup = false;
    
    // if iframes > 0 when colliding with something, ignore collision code
    // decrements by 1 per act call
    // this helps with some collision quirks (less getting stuck, helps prevent blocks getting "downgraded" more than once upon collision, etc)
    private int iframes = 6;
    
    // speed of the ball in the x and y direction is stored separately
    private int xspeed = 0;
    public int yspeed = 0;

    // specify held state, yspeed, and if it is a "powerup" ball
    public Ball(boolean held, boolean powerup, int yspeed) {
        // use "this" keyword to refer to members of a class
        // required to differentiate between class members and local variables/method parameters, if they share the same name
        // in this case this.held refers to the class private member, and held refers to the constructor parameter
        this.held = held;
        
        // same thing as above
        this.powerup = powerup;
        this.yspeed = yspeed;
        
        // if powerup ball set color to yellow
        if (powerup) {
            GreenfootImage img = getImage();
            img.setColor(Color.YELLOW);
            img.fill();
        }
    }
    
    // behaviour is defined in act()
    public void act()
    {
        // only run act() if in world by returning if world is null
        GameWorld w = getWorldOfType(GameWorld.class);
             
        if (w == null)
            return;
        
        // update yspeed (but stay the same if powerup ball, for balancing purposes)
        // remember to respect a ball's current direction when updating speed (aka preserve positive/negative values properly)
        // we do this by checking if the current value is positive, if so preserve a positive new speed value, vice versa
        // note - ternary operator usage: condition ? if condition is true use this expression : or else use this expression
        if (!powerup)
            yspeed = yspeed > 0 ? w.globalYspeed : -w.globalYspeed;
        else // is powerup, this is required (but seems redundant) as main ball uses GameWorld.globalYspeed and powerup balls don't, to properly update yspeed in cases that xspeed changes (see below)
            yspeed = yspeed > 0 ? GameWorld.BASE_YSPEED : -GameWorld.BASE_YSPEED;
            
        // increase yspeed by 1 if theres no xspeed, for balancing purposes
        if (xspeed == 0) {
            yspeed += yspeed > 0 ? 1 : -1;
        }
        
        // movement behavior, set location to current xy location values+slight offsets specified in xspeed and yspeed
        setLocation(getX() + xspeed, getY() + yspeed);
        
        // handles collision stuff
        collisionPhysics(w);
        
        // handles behavior when a ball is held by the paddle
        heldBehaviour(w);

        // see appropriate method, checks if the player has won
        checkForWinCondition(w);
        
        // decrement iframes but prevent iframes from going negative
        if (iframes > 0) 
            iframes--; 
    }
    
    // handles what happens when the ball collides with anything
    // such as the paddle, the game edges, or blocks
    private void collisionPhysics(GameWorld w) {
        // if iframes > 0 then we ignore all collision stuff and exit the method
        if (iframes > 0)
            return;
        
        // get current ball xy vals
        int x = getX();
        int y = getY();
        
        // attempt to get the ref to the paddle when the ball is colliding with paddle 
        // if they are colliding the ref is retrieved successfully, otherwise null
        Actor paddle = getOneIntersectingObject(Paddle.class);
        
        // same thing as above but with a block
        Actor block = getOneIntersectingObject(Block.class);
        
        // ref is valid --> ball is colliding with paddle
        if (paddle != null) {
            // invert y speed (bounce)
            yspeed = -yspeed; 
            
            // new x direction is based on how far away the ball is from the paddle center upon collision
            // farther away = more extreme x direction change, closer to center = less extreme x direction change
            
            // new x direction based on the ball x pos and paddle x pos
            // max is 40/-40ish (not taking paddle powerups into account...), divide by 8 so max dir/speed is 5 (?)
            // center hit = xspeed is 0 = straight
            // Math.ceil(p) rounds p upwards to the nearest integer (but still returns double so must cast to int)
            // ex: Math.ceil(4.1) --> 5
            xspeed = (int)Math.ceil((x - paddle.getX()) / 8);
            
            // reset iframes
            iframes = 6;
        } else if (block != null) { // colliding with a block
            // bounce
            yspeed = -yspeed;
            
            // increment score 
            w.score++;
            
            // see appropriate method
            speedCalculation(w);
            
            // reset iframes
            iframes = 6;
        }
        // instead of using isAtEdge(), have separate if/else cases for each edge
        // (required behavior depends on edge)
        else if (x <= 1 || x >= GameWorld.WIDTH - 1) // left + right edge
            // flip x
            xspeed = -xspeed;
        else if (y <= 1)  // upper edge
            // flip y
            yspeed = -yspeed;
        // handles what happens when the player "loses" the current "turn" / "session"
        // when the ball hits the lower edge (see loseTurn() method below)
        // note: powerup-spawned balls can't "lose", only the ball you start with (see loseTurn())
        else if (y >= GameWorld.HEIGHT - 1) 
            loseTurn(w);
    }
    
    // handles what happens when the ball goes past the paddle and hits the lower edge (ends current "turn" / "session")
    private void loseTurn(GameWorld w) {
        // if powerup remove ball, don't "lose" turn
        if (powerup) {
            w.removeObject(this);
            return;
        }
        
        // decrement lives
        w.lives--;
        // remove all balls from gameworld
        w.removeObjects(w.getObjects(Ball.class));
        
        if (w.lives > 0) 
            // if theres more lives add a new held ball to start with
            w.addObject(new Ball(true, false, -GameWorld.BASE_YSPEED), 0, 0);
        else // no more lives 
        {
            // prompt restart for the player
            w.showText("Game over - you ran out of lives! Press R to restart", 400, 300);
            
            // allow restart keybind
            w.promptingRestart = true;
        }
    }
    
    // handles the behavior of the ball if being held
    private void heldBehaviour(GameWorld w) {
        // ! inverts booleans, here we check if the ball is actually being held, if not then exit method
        if (!held)
            return;
            
        // get paddle/paddle img ref
        Paddle p = w.player;
        GreenfootImage pimg = p.getImage();
        
        // pin ball location to right above paddle
        setLocation(p.getX(), p.getY() - pimg.getHeight());
        
        // release the ball if space is pressed or mouse is clicked
        // the Greenfoot.mouseClicked(x) parameter is what actor/world object to check for mouse clicks on
        // if x is null then check for ANY mouse click regardless of what the player is clicking on
        if (Greenfoot.isKeyDown("space") || Greenfoot.mouseClicked(null)) 
            held = false;
    }
    
    // handles recalculating yspeed for all balls based on the score, only runs upon collision with a block
    // should be called right after score is incremented upon collision with a block
    private void speedCalculation(GameWorld w) {
        w.countdownToYspeedIncrease--; 
        
        // increment speed if countdown -> 0
        // also set new countdown to speed calc threshold, and then increment speed calc threshold by 2
        if (w.countdownToYspeedIncrease == 0) {
            w.countdownToYspeedIncrease = w.speedCalculationThreshold;
            w.speedCalculationThreshold += 2;
            w.globalYspeed++;
        } 
    }
    
    // check if the player has possibly won (when all blocks are destroyed)
    private void checkForWinCondition(GameWorld w) {
        // World.getObjects(cls) returns all the objects of type cls in the world in a List<cls> (List is a generic type)
        List<Block> blocks = w.getObjects(Block.class);
        
        // List.size() returns the amount of things in the list, so 0 --> empty
        // no blocks left = win
        if (blocks.size() == 0) {
            // remove all balls in the world
            w.removeObjects(w.getObjects(Ball.class));
            
            // you won text!
            w.showText("All the blocks have been destroyed, you won! Press R to restart", 400, 300);
            
            // allow restarting
            w.promptingRestart = true;
            
        }
    }
}
