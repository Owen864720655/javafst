package com.javafst.operations;

import static com.javafst.Convert.importFst;
import static com.javafst.operations.Connect.apply;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.annotations.Test;

import com.javafst.Convert;
import com.javafst.Fst;
import com.javafst.semiring.TropicalSemiring;


public class ConnectTest {

  @Test
  public void testConnect() throws NumberFormatException, IOException, ClassNotFoundException, URISyntaxException {
    String path = "algorithms/connect/fstconnect.fst.txt";
    URL url = getClass().getResource(path);
    File parent = new File(url.toURI()).getParentFile();

    path = new File(parent, "A").getPath();
    Fst fst = importFst(path, new TropicalSemiring());
    path = new File(parent, "fstconnect").getPath();
    Fst connectSaved = importFst(path, new TropicalSemiring());
    apply(fst);
    Convert.export(fst, "fstconnect.test");
    // TODO - This one is not passing...
    assertThat(fst, equalTo(connectSaved));
  }

}
