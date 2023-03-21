// represents blockstrength

enum BlockStrength {
  ONE, // green
  TWO, // orange
  THREE; // red
  
  
  // lower block strength to the next appropriate enumeration value
  // if strength is already one, remove the block from the game world (by returning null as a signal)
  BlockStrength downgrade() {
    switch (this) {
      case THREE:
        return TWO;
      case TWO:
        return ONE;
      case ONE:
      default:
        return null;
    }
  }
}
