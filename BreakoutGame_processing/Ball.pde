// class that represents the ball
class Ball {
  // white
  final color ballColor = color(255);
  
  // circle
  int diameter = 20;
  
  // to prevent faulty repeated collision
  int iframes = IFRAMES_ON_RESET;
  final static int IFRAMES_ON_RESET = 6; // 6 iframes means a tenth of a second at 60fps
  
  // stores position
  PVector pos = new PVector(); 
  
  // stores velocity
  PVector vel = new PVector(10, 10);
  
  Ball() {
    // default ball positions
    pos.x = 600;
    pos.y = 600;
  }
  
  void draw() {
    fill(ballColor);
    
    collision();
    
    circle(pos.x, pos.y, diameter);
    pos.add(vel); // movement based on velocity
    
    // decrement iframes every frame, but don't go negative
    if (iframes > 0)
      iframes--;
  }
  
  // handles all collision + bouncing physics, checks and logic, etc.
  void collision() {
    // only bounce if iframes isn't 0
    if (iframes > 0) 
      return;
    
    if (pos.x <= diameter || pos.x >= width - diameter) // left/right edge 
      vel.x *= -1; // flip
    else if (pos.y <= diameter || pos.y >= height - diameter) // up/down edge
      vel.y *= -1;
    // is colliding with paddle
    else if (ballCollidesWithRect(this, paddle)) // collides with paddle
      vel.y *= -1;  
    else if (ballCollidesWithAnyBlock(this)) // collide with any block
      vel.y *= -1;
    
      
    // reset iframes
    iframes = IFRAMES_ON_RESET;
  }
}
