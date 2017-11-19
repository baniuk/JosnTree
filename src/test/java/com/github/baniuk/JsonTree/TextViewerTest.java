package com.github.baniuk.JsonTree;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

/**
 * @author p.baniukiewicz
 *
 */
public class TextViewerTest {

  /**
   * Test method for {@link com.github.baniuk.JsonTree.TextViewer#repeat(java.lang.String, int)}.
   */
  @Test
  public void testRepeat() throws Exception {
    TextViewer obj = new TextViewer();
    assertThat(obj.repeat("|--", 0), is(""));
    assertThat(obj.repeat("|--", 3), is("|--|--|--"));
  }

}
