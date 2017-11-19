package com.github.baniuk.JsonTree;

import java.util.Collections;

/**
 * Redirect items to stdout.
 * 
 * @author p.baniukiewicz
 *
 */
public class TextViewer implements IView {
  private String delta = "|--";

  /*
   * (non-Javadoc)
   * 
   * @see com.github.baniuk.JsonTree.IView#viewItem(java.lang.String)
   */
  @Override
  public void viewLeaf(String item, int level) {
    System.out.println(repeat(delta, level) + item);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.github.baniuk.JsonTree.IView#viewBranch(java.lang.String)
   */
  @Override
  public void viewBranch(String item, int level) {
    System.out.println(repeat(delta, level) + "+" + item);
  }

  public String repeat(String str, int n) {
    if (n < 1) {
      return "";
    }
    return String.join("", Collections.nCopies(n, str));
  }

}
