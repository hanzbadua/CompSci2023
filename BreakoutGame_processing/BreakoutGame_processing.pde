/*
 * Breakout game port to Processing  
 * Similar to the Greenfoot version:
 * Click to launch ball, use mouse cursor to move paddle, higher scores will increase your yspeed, proper win/game over screen and restart keybinds, etc.
 *
 * But lacks two main features: powerups and adjustable block generation 
 * Mainly because I want to move on to other projects, but also because things such as extra balls would be even more performance-intensive as we lack a proper physics/collision engine
 * and therefore every single block would have to loop over every single ball every frame in order to perform collision checks
 *
 * Known bugs: Balls clip into rectangles upon certain weird rectangle corner collisions, we try to prevent this by performing a position offset right after any collision
 * but it's not perfect and the bug still happens from time to time
 *
 * @author (Hanz Badua)
 * @version (04 April 2023)
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
int score = 0;
int countdownToNextYIncrement = 4;
int countdownToNextCountdown = 5;
boolean onGameOver = false;
boolean won = false;

// we must either use a dummy variable to mark block deletions
// or use a concurrent collection type to avoid ConcurrentModificationException 
// we are using the latter option which is slightly less performant but much safer (and the former option would also cause flickering due to non-concurrent code --> incomplete render cycles)
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
  
  fill(255); // for white text
  textSize(30);
  // text such as score, lives etc
  text("Score: " + score, 250, 1000);
  text("Extra balls: " + lives, 500, 1000);
  
  if (ball.held) // held ball --> give instructions
  {
    text("Left click to start", 850, 600);
  }
  
  if (onGameOver || won) {
    if (onGameOver)
      text("Game over -- you lost with no extra balls remaining\nPress any key to restart", 600, 600);
      
    if (won) 
      text("You won!\nPress any key to restart", 600, 600);
      
    if (keyPressed == true) {
      ball = new Ball();
      lives = 2;
      score = 0;
      countdownToNextYIncrement = 4;
      countdownToNextCountdown = 5;
      onGameOver = false;
      won = false;
      generateBlocks();
    }
  }
}

// contains ball->block collision code, and block drawing code
// this code is then called in the main draw() method directly, rather than being part of the Ball or Block class
// for performance reasons
void blockLogic() {
  for (Block b: blocks) {
    b.draw();
    
    if (ballCollidesWithRect(ball, b))
    {
      // collision = score increment
      score++;
      
      // flip y velocity of the ball
      ball.vel.y *= -1;
      
      // based on the velocity being positive or negative, after the flip adjust the y position itself slightly to avoid unintended repeated collision issues
      ball.pos.y += ball.vel.y > 0 ? 10 : -10;
      
      b.strength = b.strength.downgrade();
      if (b.strength == null) {
        blocks.remove(b);
      }
      
      if (blocks.size() == 0) {
        won = true; 
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
