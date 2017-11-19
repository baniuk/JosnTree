package com.github.baniuk.JsonTree;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Show json as tree.
 * 
 * @author p.baniukiewicz
 *
 */
public class TreeViewer implements IView {
  // keep all nodes in map <level, node>
  // because JsonTree.print expands json from most outward class and print it line by line
  // nodes at given level can be overridden when print finishes processing one nested object and
  // moves to next one level above.
  private HashMap<Integer, DefaultMutableTreeNode> nodes = new HashMap<>();

  /**
   * Set root of document. Note that this is not the first node from json but its parent.
   * 
   * @param tn root node
   */
  public TreeViewer(DefaultMutableTreeNode tn) {
    nodes.put(-1, tn); // JsonTree.print starts from 0 for root of json that is not the same as root
                       // of document passed here. We need -1 here to make other method
                       // working when calling previous node (level passed from print is also 0
                       // based and related to root of JSON not the whole document)
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.github.baniuk.JsonTree.IView#viewLeaf(java.lang.String)
   */
  @Override
  public void viewLeaf(String item, int level) {
    DefaultMutableTreeNode category = new DefaultMutableTreeNode(item);
    nodes.get(level - 1).add(category);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.github.baniuk.JsonTree.IView#viewBranch(java.lang.String)
   */
  @Override
  public void viewBranch(String item, int level) {
    DefaultMutableTreeNode category = new DefaultMutableTreeNode(item);

    nodes.put(level, category);
    nodes.get(level - 1).add(category); // there is always one level above because root of document
                                        // is hardcoded and root of json is on level 1 (0 in this
                                        // class)
  }

}
