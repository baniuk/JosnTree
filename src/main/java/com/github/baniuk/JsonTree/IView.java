package com.github.baniuk.JsonTree;

/**
 * Interface for viewing items extracted from Json.
 * 
 * @author p.baniukiewicz
 *
 */
public interface IView {
  /**
   * Process and show leaf.
   * 
   * @param item
   */
  public void viewLeaf(String item, int level);

  /**
   * Process and show branch.
   * 
   * @param item
   */
  public void viewBranch(String item, int level);
}
