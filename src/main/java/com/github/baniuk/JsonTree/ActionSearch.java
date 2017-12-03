package com.github.baniuk.JsonTree;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * TODO Must search start from last clicked node
 * TODO Add status box next to search in UI
 * 
 * @author p.baniukiewicz
 * 
 */
@SuppressWarnings("serial")
public abstract class ActionSearch extends JsonTreeAbstractAction {

  /**
   * Index of last found entry (index in List of all entries).
   * 
   * @see #getSearchNodes(DefaultMutableTreeNode)
   */
  protected int bookamrk = -1;

  /**
   * @param name
   * @param desc
   * @param jsonTree
   */
  public ActionSearch(String name, String desc, JsonTree jsonTree) {
    super(name, desc, jsonTree);
  }

  /**
   * @param name
   * @param desc
   * @param mnemonic
   * @param jsonTree
   */
  public ActionSearch(String name, String desc, Integer mnemonic, JsonTree jsonTree) {
    super(name, desc, mnemonic, jsonTree);
  }

  /**
   * @author p.baniukiewicz
   *
   */
  public static class ActionSearchForward extends ActionSearch {

    /**
     * @param name
     * @param desc
     * @param mnemonic
     * @param jsonTree
     */
    public ActionSearchForward(String name, String desc, Integer mnemonic, JsonTree jsonTree) {
      super(name, desc, mnemonic, jsonTree);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.github.baniuk.JsonTree.JsonTreeAbstractAction#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      String text = jt.tf_search.getText().trim();
      DefaultMutableTreeNode foundNode = null;
      JsonTree.LOGGER.debug(e.getActionCommand());
      if (text.length() > 0) {
        List<DefaultMutableTreeNode> searchNodes =
                getSearchNodes((DefaultMutableTreeNode) jt.tree.getModel().getRoot());
        for (int i = bookamrk + 1; i < searchNodes.size(); i++) {
          if (searchNodes.get(i).toString().startsWith(text)) {
            bookamrk = i;
            foundNode = searchNodes.get(i);
            break;
          }
        }
        if (foundNode != null) {
          JsonTree.LOGGER.debug(foundNode.toString());
          TreePath p = new TreePath(foundNode.getPath());
          jt.tree.setSelectionPath(p);
          jt.tree.scrollPathToVisible(p);
        }
      }
    }
  }

  /**
   * @author p.baniukiewicz
   *
   */
  public static class ActionSearchBackward extends ActionSearch {

    /**
     * @param name
     * @param desc
     * @param mnemonic
     * @param jsonTree
     */
    public ActionSearchBackward(String name, String desc, Integer mnemonic, JsonTree jsonTree) {
      super(name, desc, mnemonic, jsonTree);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.github.baniuk.JsonTree.JsonTreeAbstractAction#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub - similar to forward
      super.actionPerformed(e);
    }

  }

  protected final List<DefaultMutableTreeNode> getSearchNodes(DefaultMutableTreeNode root) {
    List<DefaultMutableTreeNode> searchNodes = new ArrayList<DefaultMutableTreeNode>();

    Enumeration<?> e = root.preorderEnumeration();
    while (e.hasMoreElements()) {
      searchNodes.add((DefaultMutableTreeNode) e.nextElement());
    }
    return searchNodes;
  }
}
