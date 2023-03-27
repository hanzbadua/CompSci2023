// class that represents the ball
class Ball {
  // white
  final color ballColor = color(255);
  
  // circle
  int diameter = 20;
  
  // stores position
  PVector pos = new PVector(); 
  
  // stores velocity
  PVector vel = new PVector(12, 12);
  
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
  }
  
  // handles all collision + bouncing physics, checks and logic, etc.
  void collision() {
    if (pos.x <= diameter || pos.x >= width - diameter) // left/right edge 
      vel.x *= -1; // flip
    else if (pos.y <= diameter || pos.y >= height - diameter) { // up/down edge
      vel.y *= -1;
      pos.y += ball.vel.y > 0 ? 10 : -10;
    }
    // is colliding with paddle
    else if (ballCollidesWithRect(this, paddle)) { // collides with paddle
      vel.y *= -1;  
      pos.y += ball.vel.y > 0 ? 10 : -10;
    }
  }
}
