import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Represents the base image of the game world, including barebone textures such as valid tower area and valid enemy pathing
 * should be 960 by 720 (720 with 1/4 of the width removed), if not a forced resize will occur
 */
public class Canvas extends Actor
{
    // init, load map image as the canvas
    public Canvas() {
        setImage(new GreenfootImage(".\\images\\map.png"));
        getImage().scale(960, 720);
    }
    
    // add the canvas actor to the world, at a fixed location 
    // also load the navnode collection for the world
    public void loadToWorld(World w, Navnode[] collection) {
        // x=480, y=360, because that ensures a canvas of size 960*720 will fit the left 3/4 quarters of a 1280*720 world
        w.addObject(this, 480, 360);
        
        for(Navnode c: collection) {
            // navnodes can be added to 0,0 because they will be
            // moved to appropriate spots afterwards anyways
            // (see Navnode.addedToWorld() override)
            w.addObject(c, 0, 0);
        }
    }
}
