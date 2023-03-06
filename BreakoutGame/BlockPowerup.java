import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * BlockPowerup is an enum that represents the different powerups a block might contain, and takes effect upon block collision
 * CREATEBALL = adds new ball to world - powerup-created balls have a fixed speed of GameWorld.BASE_YSPEED and doesn't increase for balance purposes-ish
 * PADDLESIZE = increases paddle size
 * EXTRALIFE = add one extra life
 */
public enum BlockPowerup  
{
    CREATEBALL,
    EXTRALIFE,
    PADDLESIZE; // (it's a bit hard to think of different powerup ideas...)
    
    // get a random BlockPowerup
    public static BlockPowerup random() {
        int r = Greenfoot.getRandomNumber(101); // 0...100
        
        if (r > 30) // 70% chance
            return PADDLESIZE;
        else if (r > 5) // 25% chance
            return CREATEBALL;
        else // 5% chance
            return EXTRALIFE;
    }
    
    // applies the powerup imagery on a designated block
    public void applyImagery(Block bl) {
        GreenfootImage img = bl.getImage();
        img.setColor(Color.BLUE); // set powerup img color 
        
        switch (this) {
            case CREATEBALL:
                // new ball = dot img
                img.fillOval(img.getWidth()/2 - 5, img.getHeight()/2 - 4, 8, 8);
                break;
            case PADDLESIZE:
                // paddle size = rect img
                img.fillRect(img.getWidth()/2 - 14, img.getHeight()/2 - 4, 24, 8);
                break;
            case EXTRALIFE:
                // two balls i guess, i'm not going to touch drawPolygon()
                img.fillOval(img.getWidth()/2 - 20, img.getHeight()/2 - 4, 8, 8);
                img.fillOval(img.getWidth()/2 + 10, img.getHeight()/2 - 4, 8, 8);
                break;
        }
    }
    
    // apply powerup effects to the given GameWorld, and use current block for reference purposes
    public void applyPowerupEffectsToWorld(GameWorld w, Block b) {
        switch (this) {
            case CREATEBALL:
                w.addObject(new Ball(false, true, GameWorld.BASE_YSPEED), b.getX(), b.getY() + 5);
                break;
            case PADDLESIZE:
                GreenfootImage pimg = w.player.getImage();
                pimg.scale(pimg.getWidth() + 5, pimg.getHeight());
                break;
            case EXTRALIFE:
                w.lives++;
                break;
        }
    }
}
