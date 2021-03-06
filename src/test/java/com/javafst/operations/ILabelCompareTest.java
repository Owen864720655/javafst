package com.javafst.operations;

import static org.junit.Assert.assertEquals;

import com.javafst.Arc;
import com.javafst.State;
import java.util.Comparator;
import org.testng.annotations.Test;


public class ILabelCompareTest {
  
  @Test
  public void testCompare () {
    Comparator<Arc> comparator = new ILabelCompare();
    Arc arc1 = new Arc(0, 0, 0.0f, new State(0.0f));
    Arc arc2 = new Arc(1, 1, 0.0f, new State(0.0f));
    
    assertEquals(-1, comparator.compare(arc1, arc2));
    assertEquals(0, comparator.compare(arc1, arc1));
    assertEquals(1, comparator.compare(arc2, arc1));
    assertEquals(1, comparator.compare(null, arc2));
    assertEquals(-1, comparator.compare(arc1, null));
  }

}
