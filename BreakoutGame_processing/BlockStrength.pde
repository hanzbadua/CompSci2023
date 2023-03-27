// represents blockstrength

enum BlockStrength {
  ONE, // green
  TWO, // orange
  THREE; // red
  
  
  // lower block strength to the next appropriate enumeration value
  // if strength is already one, return null instead (which is a signal to remove the block)
  BlockStrength downgrade() {
    switch (this) {
      case THREE:
        return BlockStrength.TWO;
      case TWO:
        return BlockStrength.ONE;
      case ONE:
      default:
        return null;
    }
  }
}
