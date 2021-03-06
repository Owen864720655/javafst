package com.javafst.operations;

import static com.javafst.Convert.importFst;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.annotations.Test;

import com.javafst.Fst;
import com.javafst.operations.NShortestPaths;
import com.javafst.semiring.TropicalSemiring;


public class NShortestPathsTest {

  @Test
  public void testNShortestPaths() throws NumberFormatException, IOException, URISyntaxException {
    String path = "algorithms/shortestpath/A.fst";
    URL url = getClass().getResource(path);
    File parent = new File(url.toURI()).getParentFile();

    path = new File(parent, "A").getPath();
    Fst fst = importFst(path, new TropicalSemiring());
    path = new File(parent, "nsp").getPath();
    Fst nsp = importFst(path, new TropicalSemiring());

    Fst fstNsp = NShortestPaths.get(fst, 6, true);
    assertThat(nsp, equalTo(fstNsp));

    fstNsp = NShortestPaths.get(fst, 6, false);
    assertThat(nsp, equalTo(fstNsp));
    assertNull(NShortestPaths.get(null, 6, true));

    fst.setSemiring(null);
    assertNull(NShortestPaths.get(fst, 6, true));
  }
}
