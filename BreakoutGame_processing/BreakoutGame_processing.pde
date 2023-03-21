/*
 * Breakout game port to Processing  
 */
// game constants
// blocksX --> rows 
// blocksY --> columns
final int blocksX = 13;
final int blocksY = 6;

// main game stuff
Paddle paddle = new Paddle();
Ball ball = new Ball();

// stores all game blocks
ArrayList<Block> blocks = new ArrayList<Block>();

color green = color(16, 255, 16); // green
color orange = color(255, 172, 0); // orange
color red = color(255, 0, 0); // red

// initialize stuff
void setup() {
  // p2d is faster than default 2d renderer (java2d)
  // fullScreen = set width/height to whole screen = assume 1920 * 1080
  fullScreen(P2D);
  
  // ensure framerate is 60fps
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
  for (Block b: blocks) {
    b.draw();
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
// 90% chance to get paddlesize powerup
// 10% chance to get an extra life powerup
BlockPowerup getRandomPowerup() {
  int r = int(random(101)); /// 0...100  
    
  if (r > 90) // 90% chance
    return BlockPowerup.PADDLESIZE;
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
      b.pos.y = ((y+1)*50);
      blocks.add(b);
    }
  }
}

// checks if ball is colliding with any block
// by using the ball/rectangle collision method and looping over every block every frame (should be fast enough)
boolean ballCollidesWithAnyBlock(Ball ball) {
  for (Block b: blocks) {
    if (ballCollidesWithRect(ball, b))
      return true;
  }
  
  return false;
}
