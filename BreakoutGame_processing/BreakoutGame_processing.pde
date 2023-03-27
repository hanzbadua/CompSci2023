/*
 * Breakout game port to Processing  
 */

// concurrent collection type which allows operations that would normally throw ConcurrentModificationException (such as removing an item from a collection whilst said collection is currently in use/being iterated over)
// at a slight performance cost, but much safer than trying to make non-concurrent programming work with unintended operations
import java.util.concurrent.ConcurrentLinkedQueue; // alternative type that can be used is CopyOnWriteArrayList, which has faster reads but much more expensive write operations
 
// game constants
// blocksX --> rows 
// blocksY --> columns
final int blocksX = 13;
final int blocksY = 6;

// main game stuff (variables, main objects, etc.)
Paddle paddle = new Paddle();
Ball ball = new Ball();
int lives = 2;

// we must either use a dummy variable to mark block deletions
// or use a concurrent collection type to avoid ConcurrentModificationException 
// we are using the latter option which is slightly less performant but much safer
ConcurrentLinkedQueue<Block> blocks = new ConcurrentLinkedQueue<Block>(); // stores all game blocks

color green = color(16, 255, 16); // green
color orange = color(255, 172, 0); // orange
color red = color(255, 0, 0); // red

// initialize stuff
void setup() {
  // p2d is faster than default 2d renderer (java2d)
  // fullScreen = set width/height to whole screen = assume 1920 * 1080
  fullScreen(P2D);
  
  // ensure framerate is 60fps, any other framerate = unintended game speed (because frametiming/deltatiming everything is too much work)
  frameRate(60);
  
  // in Processing by default, the x/y values of a rectangle represent the top-left corner of a rectangle
  // using the function rectMode() with the argument CENTER changes the behavior so that
  // the x/y values represent the center of a rectangle (similar to Greenfoot)
  rectMode(CENTER);
  
  // initialize blocks
  generateBlocks();
}

// main game loop
void draw() {
  background(0); // black background
  
  // draw everything (paddle, ball, blocks)
  paddle.draw();
  ball.draw();
  
  blockLogic();
}

// contains ball->block collision code, and block drawing code
// this code is then called in the main draw() method directly, rather than being part of the Ball or Block class
// for performance reasons
void blockLogic() {
  for (Block b: blocks) {
    b.draw();
    
    if (ballCollidesWithRect(ball, b))
    {
      // flip y velocity of the ball
      ball.vel.y *= -1;
      
      // based on the velocity being positive or negative, after the flip adjust the y position itself slightly to avoid unintended repeated collision issues
      ball.pos.y += ball.vel.y > 0 ? 10 : -10;
      
      // remove powerup 
      b.powerup = null;
      
      b.strength = b.strength.downgrade();
      if (b.strength == null) {
        blocks.remove(b);
      }
    }
  }
}

// Code which allows us to check if a ball (circle) is colliding with a rectangle (Paddle/Block)
// Code taken from https://stackoverflow.com/a/1879223 because geometry is too much work
boolean ballCollidesWithRect(Ball c, Rectangle r) {
  float closestX = constrain(c.pos.x, r.pos.x - r.xside/2, r.pos.x + r.xside/2);
  float closestY = constrain(c.pos.y, r.pos.y - r.yside/2, r.pos.y + r.yside/2);
  
  float distX = c.pos.x - closestX;
  float distY = c.pos.y - closestY;
  
  float distSquared = (distX * distX) + (distY * distY);
  
  float radiusSquared = (c.diameter/2) * (c.diameter/2);
  
  return distSquared < radiusSquared;
}

// get a random powerup for block generation
// 80% chance to get a paddle size powerup
// 15% chance to get an extra ball
// 5% chance to get an extra life powerup
BlockPowerup getRandomPowerup() {
  int r = int(random(101)); /// 0...100  
    
  if (r > 20) // 80% chance
    return BlockPowerup.PADDLESIZE;
  else if (r > 5) // 15% chance
    return BlockPowerup.EXTRABALL;
  else // 5% chance
    return BlockPowerup.EXTRALIFE;
}

// get a random block strength for block generation
// 50% chance for strength one (green)
// 25% chance for strength two (orange)
// 25% chance for strength three (red)
BlockStrength getRandomStrength() {
  int r = int(random(101)); /// 0...100  
    
  if (r > 50) // 50% chance
    return BlockStrength.ONE;
  else if (r > 25) // 25% chance
    return BlockStrength.TWO;
  else // 25% chance
    return BlockStrength.THREE;
}

// get proper color based on block strength 
// (one --> green)
// (two --> orange)
// (three --> red)
color getBlockStrengthColor(BlockStrength b) {
  switch (b) {
    case ONE:
      return green;
    case TWO:
      return orange;
    case THREE:
      return red;
    default: // compile requirement
      return 0; // note: colors are just ints technically!
  }
}


// generate blocks
void generateBlocks() {
  blocks.clear();
  for (int x = 0; x != blocksX; x++) {
    for (int y = 0; y != blocksY; y++) {
      Block b = new Block(getRandomStrength());
      // (x+1)*a where a is x spacing
      // (y+1)*b where b is y spacing
      // and overall x/y offset for all blocks appended to the end (- 150, +150, etc.)
      b.pos.x = ((x+1)*145) - 50;
      b.pos.y = ((y+1)*35);
      blocks.add(b);
    }
  }
}

// get respective powerup shape drawing
PShape drawPowerupShape(Block b) {  
  fill(0, 0, 255); // blue
  
  switch (b.powerup) {
    case EXTRALIFE:
      return createShape(TRIANGLE, b.pos.x - 15, b.pos.y + 15, b.pos.x, b.pos.y - 15, b.pos.x + 15, b.pos.y + 15);
    case PADDLESIZE:
      return createShape(RECT, b.pos.x, b.pos.y, 40, 15); 
    case EXTRABALL:
      return createShape(ELLIPSE, b.pos.x, b.pos.y, 15, 15);
  }
  
  return null; 
}
