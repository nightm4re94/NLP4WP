package de.nlp4wp.bandpeyobaidawilke.markup;

import java.util.ArrayList;

public abstract class Revision {

  private ArrayList<Symbol> revisionSymbols;
  private int sequentialNumber = 1;

  public Revision(final int sequentialNumber) {
    this.setSequentialNumber(sequentialNumber);
    this.revisionSymbols = new ArrayList<>();
  }

  public int getLastPosition() {
    if (this.getRevisionSymbols().size() == 0) {
      return -1;
    }
    return this.getRevisionSymbols().get(this.getRevisionSymbols().size() - 1).getPosition();
  }

  public int getFirstPosition() {
    if (this.getRevisionSymbols().size() == 0) {
      return -1;
    }
    return this.getRevisionSymbols().get(0).getPosition();
  }

  public ArrayList<Symbol> getRevisionSymbols() {
    return this.revisionSymbols;
  }

  public int getSequentialNumber() {
    return this.sequentialNumber;
  }

  private void setSequentialNumber(final int sequentialNumber) {
    this.sequentialNumber = sequentialNumber;
  }
}
