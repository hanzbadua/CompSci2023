// abstract class used to represent the values of a possible object drawn as a rectangle (rect())
// used for paddle, blocks

// abstract classes represent classes that can't be instantiated directly, but are instead intended to be a base class
// for other classes to inherit from
// in this case, Block and Paddle inherit Rectangle
abstract class Rectangle {
  // vectors store position/magnitude/direction
  PVector pos = new PVector(); // stores position
  int xside = 0; // width, named differently to prevent conflicts with the "width" global
  int yside = 0; // height, same reason above
}
