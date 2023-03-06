import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class representing the actor that the player controls
 * (which is a rocket ship)
 * See the comments on the movement() method for possible keypresses
 */
public class PlayerChar extends Actor
{
    // max powerups the player can consume before they disappear and the player unlocks the weapon upgrade
    private int powerupLimit = 50;
    
    // base speed that the player is guaranteed to start with
    // 2 for all difficulties
    private static final int initialSpeed = 2;
    
    // lives the player starts with -- difficulty dependent, see constructor and SpaceWorld constructor
    private int startingLives = 3;
    
    // as soon as the class is instantiated
    // get a reference to the class image and store it in a variable
    private GreenfootImage img = getImage();
    
    // stores how much powerups have been consumed by the player actor
    private int powerupsConsumed = 0;
    
    // how fast the player actor moves
    private int speed = 0;
    
    // amount of lives - if the player collides with an enemy when their lives is 1, player loses and game ends
    // otherwise the player and enemy just bounce off each other, and the player loses a life
    private int lives = 3;
    
    // used to make projectile firing work properly
    private boolean projectileLock = false;
    
    // variables which store "width" and "height" as a double to prevent data loss when increasing by small amounts (1.6%) in powerupInteraction()
    private double expectantWidthVal = 0;
    private double expectantHeightVal = 0;
    
    // When the player spawns, they have some invincibility frames, decremented by 1 per act call
    // While colliding with an enemy while iframes > 0, don't die
    // this is to provide a grace period
    private int iframes = 120;
    
    // constructor
    // this is called whenever the class is instantiated
    // arguments: p = powerupLimit, s = startingLives <-- based on difficulty, handled in SpaceWorld
    public PlayerChar(int p, int s)
    {
        powerupLimit = p;
        startingLives = s;
        lives = startingLives;
        // The GreenfootImage.scale() method assigns a new width and height
        // to the GreenfootImage instance
        // Here, we get the reference to the image of this class, defined above
        // and its new width/height is its current width/height divided by 1.5
        img.scale((int)(img.getWidth() / 1.5), (int)(img.getHeight() / 1.5));
        
        // expectantWidthVal and expectantHeightVal are set to current width/height and will be used for storing data relevant to how powerups work
        expectantWidthVal = img.getWidth();
        expectantHeightVal = img.getHeight();
        
        // assign initial speed
        speed = initialSpeed;
    }
    
    // called every frame
    // include all class behavior here
    public void act()
    {
        // don't act if transparency == 0/actor is invisible
        // which means game over
        if (img.getTransparency() == 0)
            return; // return ends a void method early
        
        movement();
        powerupInteraction();
        enemyInteraction();
        
        // only allow firing projectiles when powerup limit is reached
        if (powerupsConsumed == powerupLimit)
            fireProjectile();
        
        // Do a full 180 turn when we reach the edge; we bounce I guess
        if (isAtEdge()) 
            turn(180);
    }
    
    // handles user input + movement stuff
    // left arrow keypress = turn left
    // right arrow keypress = turn right
    // holding d = decelerate to speed of 1 <-- added this because it is impossible to fire the laser otherwise
    private void movement()
    {
        if (Greenfoot.isKeyDown("left"))
            turn(-6);
        else if (Greenfoot.isKeyDown("right"))
            turn(6);
        
        if (Greenfoot.isKeyDown("d")) 
            move(1);
        else
            move(speed);
    }
    
    // handles projectile firing 
    // with key f (requires collecting all powerups)
    private void fireProjectile() {
        World current = getWorld();
        
        if (!Greenfoot.isKeyDown("f"))
            projectileLock = false;
        else if (projectileLock == false) // ^ isKeyDown("f") --> true
        {
            LaserProjectile proj = new LaserProjectile();
            proj.setRotation(getRotation());
            current.addObject(proj, getX(), getY());
            projectileLock = true;
        } 
    }
    
    // handles powerup interactions
    private void powerupInteraction()
    {
        // checks if we are touching a powerup
        if (isTouching(Powerup.class))
        {
            // if we are, remove the powerup, increase the powerupsConsumed var by 1
            removeTouching(Powerup.class);
            powerupsConsumed++;
            
            // and make our player actor bigger by setting its new width/height to current width/height plus 1.6%
            // we perform+store these calculations in two double variables which are initially assigned the initial width/height
            // to avoid initial data loss when increasing by 1.6%
            expectantWidthVal += (img.getWidth() * 0.016); // as a note: x += y --> x = x + y
            expectantHeightVal += (img.getHeight() * 0.016);
            
            // we lose data when casting to int at this point as we use them as method arguments
            // but that doesn't matter as long as our previous calculations were saved (which they were because we stored them in a double)
            // if we were to inline the calculations and avoid storing them in variables by doing the following:
            // --> img.scale(img.getWidth() + (int)(img.getWidth() * 0.016), img.getHeight() + (int)(img.getHeight() * 0.016));
            // the player actor would never grow as 1.6% of current width/height would always be result<1
            // rounding back down to original values due to cast data loss 
            img.scale((int)expectantWidthVal, (int)expectantHeightVal);
            
            // player speed is also dependent on powerups consumed
            speed = initialSpeed + (powerupsConsumed / 5);
            
            // get our current world object
            World world = getWorld();
            
            // generate a new powerup somewhere throughout the world to replace the old one being consumed
            world.addObject(new Powerup(), Greenfoot.getRandomNumber(SpaceWorld.WIDTH), Greenfoot.getRandomNumber(SpaceWorld.HEIGHT));
            
            // remove all powerups in the world once the player hits the powerup limit
            if (powerupsConsumed == powerupLimit)
                world.removeObjects(world.getObjects(Powerup.class));
            
        }
    }
    
    // handles what happens when the player collides with an enemy (Enemy.class)
    private void enemyInteraction()
    {
        // ignore collision/interaction if the player still has iframes
        if (iframes != 0)
        {
            iframes--;
            return;
        }
        
        // checks if the player is colliding with an enemy
        if (isTouching(Enemy.class))
        {
            // if so remove the enemy
            removeTouching(Enemy.class);
            
            // reset iframes just in case when the enemy respawns at the player position, the player isn't screwed
            iframes = 120;
            
            // decrement lives by 1
            lives--;
            
            // the player bounces after the collision
            turn(180);
            
            // current world object
            World world = getWorld();
            
            // spawn a new enemy in the game world randomly
            world.addObject(new Enemy(), Greenfoot.getRandomNumber(SpaceWorld.WIDTH), Greenfoot.getRandomNumber(SpaceWorld.HEIGHT));
            
            if (lives == 0) // if lives == 0, game over
            {
                // spawn six powerups at the players location to resemble the player actor "exploding" and
                // powerups being released or something idk
                for (int i = 0; i != 6; i++) 
                    world.addObject(new Powerup(), getX(), getY());
                
                // set transparency to 0, become invisible
                // then check if actor is invis at beginning of act(), if so return instantly (so act() doesnt do anything)
                // this is much safer than calling removeObject(this) on an essential game object thats referenced by so many different things
                img.setTransparency(0);
            }
        }
    }
    
    // public method to retrieve consumed powerups
    // we don't make the powerupsConsumed variable itself public to prevent that var being edited from outside of the class
    public int getPowerupsConsumed()
    {
        return powerupsConsumed;
    }
    
    // same thing as above but with lives
    public int getLives()
    {
        return lives;
    }
    
    // and again
    public int getPowerupLimit()
    {
        return powerupLimit;
    }
    
    // and again
    // i wish java had property syntax
    public int getStartingLives()
    {
        return startingLives;
    }
    
}
