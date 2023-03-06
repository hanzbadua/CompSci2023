// class that represents the paddle
class Paddle {
  // width can be changed via powerup
  int width = 200;
  
  // but height is always fixed
  static final int HEIGHT = 20;
  static final int YPOS = 900; // the paddle yposition is fixed
  
  // paddle color, light blue
  final color paddleColor = color(30, 144, 255);
  
  void draw() {
    fill(paddleColor); // set color
    pushMatrix(); // push the current transform matrix onto matrix "stack"
    translate(mouseX, 0);
    rect(0, YPOS, width, HEIGHT); // draw paddle - x is 0 because that will be specified in the translate above 
    popMatrix(); // pop the matrix stack and restore the previous pushed transform matrix
  }
}
