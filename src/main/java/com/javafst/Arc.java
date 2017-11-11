package com.javafst;

/**
 * The fst's arc implementation.
 */
public class Arc {
  
  private static float EQUAL_TOLERANCE = 1e-7f;

  // Arc's weight
  private float weight;

  // input label
  private int inputLabel;

  // output label
  private int outputLabel;

  // next state's id
  private State nextState;

  public Arc() {
  }

  /**
   * Arc Constructor.
   * 
   * @param inputLabel        The input label's id.
   * @param outputLabel       The output label's id.
   * @param weight            The arc's weight.
   * @param nextState         The arc's next state.
   */
  public Arc(int inputLabel, int outputLabel, float weight, State nextState) {
    this.weight = weight;
    this.inputLabel = inputLabel;
    this.outputLabel = outputLabel;
    this.nextState = nextState;
  }

  /**
   * Get the arc's weight.
   * @return                  Arc weight.
   */
  public float getWeight() {
    return weight;
  }

  /**
   * Set the arc's weight.
   * @param weight            Arc weight.
   */
  public void setWeight(float weight) {
    this.weight = weight;
  }

  /**
   * Get the input label's id.
   * @return                  Label id.
   */
  public int getIlabel() {
    return inputLabel;
  }

  /**
   * Set the input label's id.
   * 
   * @param inputLabel        The input label's id to set.
   */
  public void setIlabel(int inputLabel) {
    this.inputLabel = inputLabel;
  }

  /**
   * Get the output label's id.
   * @return output           Label id.
   */
  public int getOlabel() {
    return outputLabel;
  }

  /**
   * Set the output label's id.
   * 
   * @param outputLabel       The output label's id to set.
   */
  public void setOlabel(int outputLabel) {
    this.outputLabel = outputLabel;
  }

  /**
   * Get the next state.
   * @return                  Next state.
   */
  public State getNextState() {
    return nextState;
  }

  /**
   * Set the next state.
   * 
   * @param nextState         The next state to set.
   */
  public void setNextState(State nextState) {
    this.nextState = nextState;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Arc other = (Arc) obj;
    if (inputLabel != other.inputLabel) {
      return false;
    }
    if (nextState == null) {
      if (other.nextState != null) {
        return false;
      }
    } else if (nextState.getId() != other.nextState.getId()) {
      return false;
    }
    if (outputLabel != other.outputLabel) {
      return false;
    }
    if (weight != other.weight) {  
      return (Math.abs(weight - other.weight) < EQUAL_TOLERANCE);
    }
    return true;
  }

  @Override
  public int hashCode() {
    return 31 * (inputLabel 
       + (31 * (outputLabel 
           + (31 * (nextState == null ? 0 : nextState.getId())
                + Float.floatToIntBits(weight)))));
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "(" + inputLabel + ", " + outputLabel + ", " + weight + ", " + nextState
        + ")";
  }
}
