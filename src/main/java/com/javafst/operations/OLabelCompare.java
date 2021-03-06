package com.javafst.operations;

import com.javafst.Arc;

import java.util.Comparator;

/**
 * Comparator used in {@link com.javafst.operations.ArcSort} for sorting
 * based on output labels.
 */
public class OLabelCompare implements Comparator<Arc> {

  @Override
  public int compare(final Arc o1, final Arc o2) {
    if (o1 == null) {
      return 1;
    }
    if (o2 == null) {
      return -1;
    }
    return (o1.getOlabel() < o2.getOlabel()) ? -1 : ((o1.getOlabel() == o2
        .getOlabel()) ? 0 : 1);
  }

}
