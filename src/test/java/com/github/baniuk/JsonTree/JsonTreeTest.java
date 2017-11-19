package com.github.baniuk.JsonTree;

import org.junit.Test;

/**
 * JsonTree test class. Contain also pure runners.
 * 
 * @author p.baniukiewicz
 *
 */
public class JsonTreeTest {

  /**
   * Show help message for cli call.
   * 
   * @throws Exception
   */
  @Test
  public void testJsonTreeStringArray() throws Exception {
    @SuppressWarnings("unused")
    JsonTree obj = new JsonTree(new String[] { "JsonString", "-help" });
  }

  /**
   * Show QCONF called from CLI.
   * 
   * @throws Exception
   */
  @Test
  public void testJsonTreeStringArray_1() throws Exception {
    @SuppressWarnings("unused")
    JsonTree obj = new JsonTree(new String[] { "JsonString", "-f",
        "C:\\Users\\baniu\\Documents\\Repos\\QuimP.git\\src\\test\\Resources-static"
                + "\\FormatConverter\\templates\\Paqp-Q to QCONF\\test.QCONF" });
  }

}
